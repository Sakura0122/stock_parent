package com.sakura.stock.service.impl;

import com.alibaba.excel.EasyExcel;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sakura.stock.mapper.StockMarketIndexInfoMapper;
import com.sakura.stock.mapper.StockRtInfoMapper;
import com.sakura.stock.pojo.domain.InnerMarketDomain;
import com.sakura.stock.pojo.domain.StockBlockDomain;
import com.sakura.stock.pojo.domain.StockUpdownDomain;
import com.sakura.stock.pojo.vo.StockInfoConfig;
import com.sakura.stock.service.StockService;
import com.sakura.stock.utils.DateTimeUtil;
import com.sakura.stock.vo.resp.PageResult;
import com.sakura.stock.vo.resp.R;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: sakura
 * @date: 2024/3/14 21:42
 * @description: 定义股票服务实现
 */
@Service
@Slf4j
public class StockServiceImpl implements StockService {

    @Autowired
    private StockInfoConfig stockInfoConfig;

    @Autowired
    private StockMarketIndexInfoMapper stockMarketIndexInfoMapper;

    @Autowired
    private StockRtInfoMapper stockRtInfoMapper;

    /**
     * 获取国内大盘最新数据
     *
     * @return
     */
    @Override
    public List<InnerMarketDomain> getInnerMarketInfo() {
        // 1.获取股票最新交易时间点（准确到分钟 秒和毫秒置为0）
        Date curDate = DateTimeUtil.getLastDate4Stock(DateTime.now()).toDate();
        curDate = DateTime.parse("2022-07-07 14:52:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();

        // 2.获取大盘编码集合
        List<String> mCodes = stockInfoConfig.getInner();

        // 3.调用mapper查询数据
        List<InnerMarketDomain> data = stockMarketIndexInfoMapper.getMarketInfo(curDate, mCodes);

        // 4.响应数据
        return data;
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
}
