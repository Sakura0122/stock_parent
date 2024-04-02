package com.sakura.stock.face;

import com.sakura.stock.pojo.entity.StockBusiness;

import java.util.List;

/**
 * @author: sakura
 * @date: 2024/4/2 14:55
 * @description: 定义股票缓存层
 */
public interface StockCacheFace {

    /**
     * 获取所有股票编码，并添加上证或者深证的股票前缀编号：sh sz
     *
     * @return
     */
    List<String> getAllStockCodeWithPrefix();

    /**
     * 根据id更新股票的信息
     */
    void updateStockInfoById(StockBusiness info);
}
