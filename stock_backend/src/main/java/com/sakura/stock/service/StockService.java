package com.sakura.stock.service;

import com.sakura.stock.pojo.domain.InnerMarketDomain;
import com.sakura.stock.pojo.domain.StockBlockDomain;
import com.sakura.stock.vo.resp.R;

import java.util.List;

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
}
