package com.sakura.stock.service.impl;

import com.alibaba.excel.EasyExcel;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sakura.stock.mapper.StockMarketIndexInfoMapper;
import com.sakura.stock.mapper.StockRtInfoMapper;
import com.sakura.stock.pojo.domain.*;
import com.sakura.stock.pojo.vo.StockInfoConfig;
import com.sakura.stock.service.StockService;
import com.sakura.stock.utils.DateTimeUtil;
import com.sakura.stock.vo.resp.PageResult;
import com.sakura.stock.vo.resp.R;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: sakura
 * @date: 2024/3/14 21:42
 * @description: 定义股票服务实现
 */
@Service
@Slf4j
public class StockServiceImpl implements StockService {

    @Resource
    private StockInfoConfig stockInfoConfig;

    @Resource
    private StockMarketIndexInfoMapper stockMarketIndexInfoMapper;

    @Resource
    private StockRtInfoMapper stockRtInfoMapper;

    @Resource
    private Cache<String, Object> caffeineCache;

    /**
     * 获取国内大盘最新数据
     *
     * @return
     */
    @Override
    public List<InnerMarketDomain> getInnerMarketInfo() {
        // 默认从本地缓存加载数据 不存在则从数据库加载同步到缓存
        // 在开盘周期内 本地缓存默认有效期一分钟
        Object cache = caffeineCache.get("innerMarketKey", key -> {
            // 1.获取股票最新交易时间点（准确到分钟 秒和毫秒置为0）
            Date curDate = DateTimeUtil.getLastDate4Stock(DateTime.now()).toDate();
            curDate = DateTime.parse("2022-07-07 14:52:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();

            // 2.获取大盘编码集合
            List<String> mCodes = stockInfoConfig.getInner();

            // 3.调用mapper查询数据
            List<InnerMarketDomain> data = stockMarketIndexInfoMapper.getMarketInfo(curDate, mCodes);

            // 4.响应数据
            return data;
        });
        return (List<InnerMarketDomain>) cache;
    }

    /**
     * 获取沪深两市板块最新数据，以交易总金额降序查询，取前10条数据
     *
     * @return
     */
    @Override
    public List<StockBlockDomain> getSectorInfo() {
        // 1.获取股票最新交易时间点（准确到分钟 秒和毫秒置为0）
        Date curDate = DateTimeUtil.getLastDate4Stock(DateTime.now()).toDate();
        curDate = DateTime.parse("2022-01-14 16:57:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();

        // 2.调用mapper查询数据
        List<StockBlockDomain> data = stockMarketIndexInfoMapper.getSectorInfo(curDate);


        // 3.响应数据
        return data;
    }

    /**
     * 股票最新数据
     *
     * @return
     */
    @Override
    public PageResult<StockUpdownDomain> getStockPageInfo(Integer page, Integer pageSize) {
        // 1.获取股票最新交易时间点（准确到分钟 秒和毫秒置为0）
        Date curDate = DateTimeUtil.getLastDate4Stock(DateTime.now()).toDate();
        curDate = DateTime.parse("2021-12-30 09:32:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();

        // 2.设置分页参数
        PageHelper.startPage(page, pageSize);

        // 3.调用mapper查询数据
        List<StockUpdownDomain> pageData = stockRtInfoMapper.getStockInfoByTime(curDate);

        // 4.组装pageResult对象
        PageInfo<StockUpdownDomain> pageInfo = new PageInfo<>(pageData);
        PageResult<StockUpdownDomain> data = new PageResult<>(pageInfo);

        // 5.响应数据
        return data;
    }

    /**
     * 涨幅榜
     *
     * @return
     */
    @Override
    public List<StockUpdownDomain> getStockIncrease() {
        // 1.调用mapper查询数据
        List<StockUpdownDomain> data = stockRtInfoMapper.getStockIncrease();

        // 2.响应数据
        return data;
    }

    /**
     * 统计最新股票交易日内每分钟涨跌停的股票数量
     *
     * @return
     */
    @Override
    public Map<String, List> getStockUpDownCount() {
        // 1.获取最新股票交易时间点
        DateTime curDateTime = DateTimeUtil.getLastDate4Stock(DateTime.now());
        // 假数据
        curDateTime = DateTime.parse("2022-01-06 14:25:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
        Date endDate = curDateTime.toDate();

        // 2.获取最新交易时间点对应的开盘时间
        Date startDate = DateTimeUtil.getOpenDate(curDateTime).toDate();

        // 3.统计涨停数据
        List<Map> upList = stockRtInfoMapper.getStockUpDownCount(startDate, endDate, "up");

        // 4.统计跌停数据
        List<Map> downList = stockRtInfoMapper.getStockUpDownCount(startDate, endDate, "down");

        // 5.组装数据
        Map<String, List> data = new HashMap<>();
        data.put("upList", upList);
        data.put("downList", downList);

        // 6.响应数据
        return data;
    }

    /**
     * 导出指定页码最新股票信息
     *
     * @param page     当前页
     * @param pageSize 页大小
     * @param response 响应对象
     */
    @Override
    public void exportStockUpDownInfo(Integer page, Integer pageSize, HttpServletResponse response) {
        // 1.获取分页数据
        PageResult<StockUpdownDomain> stockPageInfo = this.getStockPageInfo(page, pageSize);
        List<StockUpdownDomain> rows = stockPageInfo.getRows();

        // 2.将数据导出到excel中
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        try {
            String fileName = URLEncoder.encode("股票信息表", "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            EasyExcel.write(response.getOutputStream(), StockUpdownDomain.class).sheet("股票导出信息").doWrite(rows);
        } catch (IOException e) {
            log.info("当前页码：{},每页大小：{},当前时间：{},异常信息：{}", page, pageSize, DateTime.now().toString("yyyy-MM-dd HH:mm:ss"), e.getMessage());
            // 通知前端异常 稍后重试
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            R<Object> error = R.error("导出失败，稍后重试");
            try {
                String jsonData = new ObjectMapper().writeValueAsString(error);
                response.getWriter().write(jsonData);
            } catch (IOException ex) {
                log.error("exportStockUpDownInfo：响应错误信息失败,时间：{}", DateTime.now().toString("yyyy-MM-dd HH:mm:ss"));
            }
        }
    }

    /**
     * 统计大盘T日和T-1日每分钟交易量
     *
     * @return
     */
    @Override
    public Map<String, List> getComparedStockTradeAmt() {
        // 1.获取T日（最新股票交易时间点）
        DateTime tEndcurDateTime = DateTimeUtil.getLastDate4Stock(DateTime.now());
        // 假数据
        tEndcurDateTime = DateTime.parse("2021-12-28 16:00:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
        Date tEndDate = tEndcurDateTime.toDate();
        Date tStartDate = DateTimeUtil.getOpenDate(tEndcurDateTime).toDate();

        // 2.获取T-1日（前一日交易时间点）
        DateTime preTcurDateTime = DateTimeUtil.getPreviousTradingDay(tEndcurDateTime);
        Date preTEndDate = preTcurDateTime.toDate();
        Date preTStartDate = DateTimeUtil.getOpenDate(preTcurDateTime).toDate();

        // 3.调用mapper查询数据
        // 3.1 统计T日
        List<Map> tData = stockRtInfoMapper.getSumAmtInfo(tStartDate, tEndDate, stockInfoConfig.getInner());
        // 3.2 统计T-1日
        List<Map> preTData = stockRtInfoMapper.getSumAmtInfo(preTStartDate, preTEndDate, stockInfoConfig.getInner());

        // 4.组装数据
        Map<String, List> data = new HashMap<>();
        data.put("amtList", tData);
        data.put("yesAmtList", preTData);

        // 5.响应数据
        return data;
    }

    /**
     * 获取最新交易时间下股票（A股）在各个涨幅区间下的数量
     *
     * @return
     */
    @Override
    public Map getIncreaseRange() {
        // 1.获取当前最新交易时间点
        DateTime curDateTime = DateTimeUtil.getLastDate4Stock(DateTime.now());
        curDateTime = DateTime.parse("2022-01-06 09:55:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
        Date date = curDateTime.toDate();

        // 2.调用mapper查询数据
        List<Map> infos = stockRtInfoMapper.getIncreaseRangeInfoByDate(date);

        // 获取有序的涨幅区间标题集合
        List<String> upDownRange = stockInfoConfig.getUpDownRange();
        // 将顺序的涨幅区间内的每个元素转换成Map对象即可
        // List<Map> allInfos  = new ArrayList<>();
        // for (String title : upDownRange) {
        //     Map tmp = null;
        //     for (Map info : infos) {
        //         if (info.containsValue(title)) {
        //             tmp = info;
        //             break;
        //         }
        //     }
        //     if (tmp == null) {
        //         tmp = new HashMap();
        //         tmp.put("count", 0);
        //         tmp.put("title", title);
        //     }
        //     allInfos.add(tmp);
        // }

        // stream遍历获取
        List<Map> allInfos = upDownRange.stream().map(title -> {
            Optional<Map> result = infos.stream().filter(info -> info.containsValue(title)).findFirst();
            if (result.isPresent()) {
                return result.get();
            } else {
                Map<String, Object> tmp = new HashMap();
                tmp.put("count", 0);
                tmp.put("title", title);
                return tmp;
            }
        }).collect(Collectors.toList());

        // 3.组装数据
        Map<String, Object> data = new HashMap<>();
        data.put("time", curDateTime.toString("yyyy-MM-dd HH:mm:ss"));
        // data.put("infos", infos);
        data.put("infos", allInfos);

        // 4.响应数据
        return data;
    }

    /**
     * 查询单个个股的分时行情数据
     *
     * @param code 股票编码
     * @return
     */
    @Override
    public List<Stock4MinuteDomain> getStockScreenTimeSharing(String code) {
        // 1.获取当前最新交易时间点 开始时间与结束时间
        DateTime endDateTime = DateTimeUtil.getLastDate4Stock(DateTime.now());
        endDateTime = DateTime.parse("2021-12-30 14:30:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
        Date endDate = endDateTime.toDate();
        Date startDate = DateTimeUtil.getOpenDate(endDateTime).toDate();

        // 2.调用mapper查询数据
        List<Stock4MinuteDomain> data = stockRtInfoMapper.getStock4MinuteInfo(startDate, endDate, code);

        // 3.响应数据
        return data;
    }

    /**
     * 指定股票日K线数据
     *
     * @param code 股票编码
     * @return
     */
    @Override
    public List<Stock4EvrDayDomain> getStockScreenDKLine(String code) {
        // 1.获取统计日k线数据的时间范围
        // 1.1 获取截止时间
        DateTime endDateTime = DateTimeUtil.getLastDate4Stock(DateTime.now());
        endDateTime = DateTime.parse("2022-01-06 14:25:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
        Date endDate = endDateTime.toDate();
        // 1.2 获取起始时间
        Date startDate = endDateTime.minusMonths(3).toDate();

        // 2.调用mapper查询数据
        List<Stock4EvrDayDomain> data = stockRtInfoMapper.getStock4DkLine(startDate, endDate, code);

        // 3.响应数据
        return data;
    }
}
