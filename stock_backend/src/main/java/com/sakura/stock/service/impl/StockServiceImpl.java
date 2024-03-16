package com.sakura.stock.service.impl;

import com.sakura.stock.mapper.StockMarketIndexInfoMapper;
import com.sakura.stock.pojo.domain.InnerMarketDomain;
import com.sakura.stock.pojo.domain.StockBlockDomain;
import com.sakura.stock.pojo.vo.StockInfoConfig;
import com.sakura.stock.service.StockService;
import com.sakura.stock.utils.DateTimeUtil;
import com.sakura.stock.vo.resp.R;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

/**
 * @author: sakura
 * @date: 2024/3/14 21:42
 * @description: 定义股票服务实现
 */
@Service
public class StockServiceImpl implements StockService {

    @Autowired
    private StockInfoConfig stockInfoConfig;

    @Autowired
    private StockMarketIndexInfoMapper stockMarketIndexInfoMapper;

    /**
     * 获取国内大盘最新数据
     *
     * @return
     */
    @Override
    public List<InnerMarketDomain> getInnerMarketInfo() {
        // 1.获取股票最新交易时间点（准确到分钟 秒和毫秒置为0）
        DateTime curDateTime = DateTimeUtil.getLastDate4Stock(DateTime.now());
        Date date = curDateTime.toDate();
        date = DateTime.parse("2022-07-07 14:52:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();

        // 2.获取大盘编码集合
        List<String> mCodes = stockInfoConfig.getInner();

        // 3.调用mapper查询数据
        List<InnerMarketDomain> data = stockMarketIndexInfoMapper.getMarketInfo(date, mCodes);

        // 4.封装并响应
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
        DateTime curDateTime = DateTimeUtil.getLastDate4Stock(DateTime.now());
        Date date = curDateTime.toDate();
        date = DateTime.parse("2022-01-14 16:57:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();

        // 2.调用mapper查询数据
        List<StockBlockDomain> data = stockMarketIndexInfoMapper.getSectorInfo(date);


        // 3.封装并响应
        return data;
    }
}
