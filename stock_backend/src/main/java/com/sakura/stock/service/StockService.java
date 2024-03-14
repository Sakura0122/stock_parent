package com.sakura.stock.service;

import com.sakura.stock.pojo.domain.InnerMarketDomain;

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
}
