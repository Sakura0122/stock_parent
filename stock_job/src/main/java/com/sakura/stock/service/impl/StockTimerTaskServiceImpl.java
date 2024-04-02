package com.sakura.stock.service.impl;

import cn.hutool.core.collection.ListUtil;
import com.sakura.stock.constant.ParseType;
import com.sakura.stock.face.StockCacheFace;
import com.sakura.stock.mapper.StockBusinessMapper;
import com.sakura.stock.mapper.StockMarketIndexInfoMapper;
import com.sakura.stock.mapper.StockRtInfoMapper;
import com.sakura.stock.pojo.entity.StockMarketIndexInfo;
import com.sakura.stock.pojo.entity.StockRtInfo;
import com.sakura.stock.pojo.vo.StockInfoConfig;
import com.sakura.stock.service.StockTimerTaskService;
import com.sakura.stock.utils.DateTimeUtil;
import com.sakura.stock.utils.IdWorker;
import com.sakura.stock.utils.ParserStockInfoUtil;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author: sakura
 * @date: 2024/3/21 22:38
 * @description:
 */
@Service
@Slf4j
public class StockTimerTaskServiceImpl implements StockTimerTaskService {

    @Resource
    private RestTemplate restTemplate;

    @Resource
    private StockInfoConfig stockInfoConfig;

    @Resource
    private IdWorker idWorker;

    @Resource
    private StockMarketIndexInfoMapper stockMarketIndexInfoMapper;

    @Resource
    private ParserStockInfoUtil parserStockInfoUtil;

    @Resource
    private StockBusinessMapper stockBusinessMapper;

    @Resource
    private StockRtInfoMapper stockRtInfoMapper;

    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * 注入线程池对象
     */
    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Resource
    private StockCacheFace stockCacheFace;

    /**
     * 必须保障该对象无状态
     */
    private HttpEntity<Object> httpEntity;

    /**
     * 获取国内大盘的实时数据信息
     */
    @Override
    public void getInnerMarketInfo() {
        // 1.采集原始数据

        // 1.1 组装url地址
        String url = stockInfoConfig.getMarketUrl() + String.join(",", stockInfoConfig.getInner());

        // 发送请求
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
        int statusCodeValue = responseEntity.getStatusCodeValue();
        if (statusCodeValue != 200) {
            log.error("当前时间点：{}，采集数据失败，状态码为：{}", DateTime.now().toString("yyyy-MM-dd HH:mm:ss"), statusCodeValue);
            // 发送企业微信 钉钉 给运维人员提醒
            return;
        }

        // 获取js格式数据
        String jsData = responseEntity.getBody();
        log.info("当前时间点：{}，采集数据成功，js格式数据为：{}", DateTime.now().toString("yyyy-MM-dd HH:mm:ss"), jsData);

        // 2，java正则解析原始数据
        // var hq_str_sh000001="上证指数,3267.8103,3283.4261,3236.6951,3290.2561,3236.4791,0,0,402626660,398081845473,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2022-04-07,15:01:09,00,";
        // var hq_str_sz399001="深证成指,12101.371,12172.911,11972.023,12205.097,11971.334,0.000,0.000,47857870369,524892592190.995,0,0.000,0,0.000,0,0.000,0,0.000,0,0.000,0,0.000,0,0.000,0,0.000,0,0.000,0,0.000,2022-04-07,15:00:03,00";

        // 2.1 定义正则表达式
        String reg = "var hq_str_(.+)=\"(.+)\";";
        // 2.2 表达式编译
        Pattern pattern = Pattern.compile(reg);
        // 2.3 匹配字符串
        Matcher matcher = pattern.matcher(jsData);

        // 3.解析数据封装实体对象
        ArrayList<StockMarketIndexInfo> list = new ArrayList<>();
        while (matcher.find()) {
            // 1.获取大盘编码
            String marketCode = matcher.group(1);
            // 2.获取其他信息
            String other = matcher.group(2);
            // 将other字符串以逗号切割 获取大盘详细信息
            String[] splitArr = other.split(",");
            // 大盘名称
            String marketName = splitArr[0];
            // 获取当前大盘的开盘点数
            BigDecimal openPoint = new BigDecimal(splitArr[1]);
            // 前收盘点
            BigDecimal preClosePoint = new BigDecimal(splitArr[2]);
            // 获取大盘的当前点数
            BigDecimal curPoint = new BigDecimal(splitArr[3]);
            // 获取大盘最高点
            BigDecimal maxPoint = new BigDecimal(splitArr[4]);
            // 获取大盘的最低点
            BigDecimal minPoint = new BigDecimal(splitArr[5]);
            // 获取成交量
            Long tradeAmt = Long.valueOf(splitArr[8]);
            // 获取成交金额
            BigDecimal tradeVol = new BigDecimal(splitArr[9]);
            // 时间
            Date curTime = DateTimeUtil.getDateTimeWithoutSecond(splitArr[30] + " " + splitArr[31]).toDate();
            // 组装entity对象
            StockMarketIndexInfo info = StockMarketIndexInfo.builder()
                    .id(idWorker.nextId())
                    .marketCode(marketCode)
                    .marketName(marketName)
                    .curPoint(curPoint)
                    .openPoint(openPoint)
                    .preClosePoint(preClosePoint)
                    .maxPoint(maxPoint)
                    .minPoint(minPoint)
                    .tradeVolume(tradeVol)
                    .tradeAmount(tradeAmt)
                    .curTime(curTime)
                    .build();
            // 收集封装的对象，方便批量插入
            list.add(info);
        }
        log.info("采集的当前大盘数据：{}", list);

        // 4.调用mybatis批量入库
        int count = stockMarketIndexInfoMapper.insertBatch(list);
        if (count == 0) {
            log.error("当前时间：{},插入数据：{},失败", DateTime.now().toString("yyyy-MM-dd HH:mm:ss"), list);
        } else {
            // 采集完毕 通知backend工程刷新缓存
            // 发送日期对象 接收方通过接收的日期和当前日期对比 判断出数据延迟的时长 用于运维通知处理
            rabbitTemplate.convertAndSend("stockExchange", "inner.market", new Date());
            log.info("当前时间：{},插入数据：{},成功", DateTime.now().toString("yyyy-MM-dd HH:mm:ss"), list);
        }
    }

    /**
     * 定义获取分钟级股票数据
     * http://hq.sinajs.cn/list=sz000002,sh600015
     */
    @Override
    public void getStockRtIndex() {
        // 1.获取个股编码的集合
        // List<String> allStockCodes = stockBusinessMapper.getAllStockCodes();
        // allStockCodes = allStockCodes.stream()
        //         .map(item -> item.startsWith("6") ? "sh" + item : "sz" + item)
        //         .collect(Collectors.toList());

        List<String> allStockCodes = stockCacheFace.getAllStockCodeWithPrefix();

        // 将所有个股编码组成大的集合分成若干小集合 分批次拉取数据
        long startTime = System.currentTimeMillis();
        ListUtil.split(allStockCodes, 15).forEach(item -> {
            // String url = stockInfoConfig.getMarketUrl() + String.join(",", item);
            // // 发起请求
            // ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
            // int statusCodeValue = responseEntity.getStatusCodeValue();
            // if (statusCodeValue != 200) {
            //     log.error("当前时间点：{}，采集数据失败，状态码为：{}", DateTime.now().toString("yyyy-MM-dd HH:mm:ss"), statusCodeValue);
            //     // 发送企业微信 钉钉 给运维人员提醒
            //     return;
            // }
            // // 获取js格式数据
            // String jsData = responseEntity.getBody();
            // // 调用工具类解析数据
            // List<StockRtInfo> list = parserStockInfoUtil.parser4StockOrMarketInfo(jsData, ParseType.ASHARE);
            // log.info("采集个股数据：{}", list);
            //
            // // 批量插入
            // int count = stockRtInfoMapper.insertBatch(list);
            // if (count == 0) {
            //     log.error("当前时间：{},插入数据：{},失败", DateTime.now().toString("yyyy-MM-dd HH:mm:ss"), list);
            // } else {
            //     log.info("当前时间：{},插入数据：{},成功", DateTime.now().toString("yyyy-MM-dd HH:mm:ss"), list);
            // }

            // 方案1：原始数据采集个股数据时将集合分片 然后分批次串行采集数据 效率不高 存在较高采集延迟 引入多线程
            // 方案1的问题：1.每次来任务 就创建换一个线程 复用性差 2.如果多线程使用不当 造成cpu竞争激烈 导致频繁上下文切换 性能降低
            // new Thread(() -> {
            //     String url = stockInfoConfig.getMarketUrl() + String.join(",", item);
            //     // 发起请求
            //     ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
            //     int statusCodeValue = responseEntity.getStatusCodeValue();
            //     if (statusCodeValue != 200) {
            //         log.error("当前时间点：{}，采集数据失败，状态码为：{}", DateTime.now().toString("yyyy-MM-dd HH:mm:ss"), statusCodeValue);
            //         // 发送企业微信 钉钉 给运维人员提醒
            //         return;
            //     }
            //     // 获取js格式数据
            //     String jsData = responseEntity.getBody();
            //     // 调用工具类解析数据
            //     List<StockRtInfo> list = parserStockInfoUtil.parser4StockOrMarketInfo(jsData, ParseType.ASHARE);
            //     log.info("采集个股数据：{}", list);
            //
            //     // 批量插入
            //     int count = stockRtInfoMapper.insertBatch(list);
            //     if (count == 0) {
            //         log.error("当前时间：{},插入数据：{},失败", DateTime.now().toString("yyyy-MM-dd HH:mm:ss"), list);
            //     } else {
            //         log.info("当前时间：{},插入数据：{},成功", DateTime.now().toString("yyyy-MM-dd HH:mm:ss"), list);
            //     }
            // }).start();

            // 方案2：引入线程池
            threadPoolTaskExecutor.execute(() -> {
                String url = stockInfoConfig.getMarketUrl() + String.join(",", item);
                // 发起请求
                ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
                int statusCodeValue = responseEntity.getStatusCodeValue();
                if (statusCodeValue != 200) {
                    log.error("当前时间点：{}，采集数据失败，状态码为：{}", DateTime.now().toString("yyyy-MM-dd HH:mm:ss"), statusCodeValue);
                    // 发送企业微信 钉钉 给运维人员提醒
                    return;
                }
                // 获取js格式数据
                String jsData = responseEntity.getBody();
                // 调用工具类解析数据
                List<StockRtInfo> list = parserStockInfoUtil.parser4StockOrMarketInfo(jsData, ParseType.ASHARE);
                log.info("采集个股数据：{}", list);

                // 批量插入
                int count = stockRtInfoMapper.insertBatch(list);
                if (count == 0) {
                    log.error("当前时间：{},插入数据：{},失败", DateTime.now().toString("yyyy-MM-dd HH:mm:ss"), list);
                } else {
                    log.info("当前时间：{},插入数据：{},成功", DateTime.now().toString("yyyy-MM-dd HH:mm:ss"), list);
                }
            });
        });
        long time = System.currentTimeMillis() - startTime;
        log.info("采集花费时间：{}ms", time);
    }

    /**
     * bean生命周期初始化
     */
    @PostConstruct
    public void initData() {
        // 维护请求头 添加防盗链和用户标识
        HttpHeaders httpHeaders = new HttpHeaders();
        // 防盗链
        httpHeaders.add("Referer", "https://finance.sina.com.cn/stock/");
        // 用户客户端标识
        httpHeaders.add("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/123.0.0.0 Safari/537.36");
        // 维护http请求实体对象
        httpEntity = new HttpEntity<>(httpHeaders);
    }
}
