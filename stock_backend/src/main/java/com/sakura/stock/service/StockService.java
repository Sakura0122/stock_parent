package com.sakura.stock.service;

import com.sakura.stock.pojo.domain.InnerMarketDomain;
import com.sakura.stock.pojo.domain.StockBlockDomain;
import com.sakura.stock.pojo.domain.StockUpdownDomain;
import com.sakura.stock.vo.resp.PageResult;
import com.sakura.stock.vo.resp.R;

import java.util.List;
import java.util.Map;

/**
 * @author: sakura
 * @date: 2024/3/14 21:41
 * @description: 股票服务接口
 */
public interface StockService {
    /**
     * 获取国内大盘最新数据
     * @return
     */
    List<InnerMarketDomain> getInnerMarketInfo();

    /**
     * 获取沪深两市板块最新数据，以交易总金额降序查询，取前10条数据
     * @return
     */
    List<StockBlockDomain> getSectorInfo();

    /**
     * 股票最新数据
     *
     * @return
     */
    PageResult<StockUpdownDomain> getStockPageInfo(Integer page, Integer pageSize);

    /**
     * 涨幅榜
     * @return
     */
    List<StockUpdownDomain> getStockIncrease();

    /**
     * 统计最新股票交易日内每分钟涨跌停的股票数量
     * @return
     */
    Map<String, List> getStockUpDownCount();
}
