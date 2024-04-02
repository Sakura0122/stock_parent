package com.sakura.stock.face.impl;

import com.sakura.stock.face.StockCacheFace;
import com.sakura.stock.mapper.StockBusinessMapper;
import com.sakura.stock.pojo.entity.StockBusiness;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: sakura
 * @date: 2024/4/2 14:56
 * @description: 定义股票缓存层的实现
 */
@Component
public class StockCacheFaceImpl implements StockCacheFace {

    @Resource
    private StockBusinessMapper stockBusinessMapper;

    @Cacheable(cacheNames = "stock", key = "'stockCodes'")
    @Override
    public List<String> getAllStockCodeWithPrefix() {
        // 1.获取个股编码的集合
        List<String> allStockCodes = stockBusinessMapper.getAllStockCodes();
        allStockCodes = allStockCodes.stream()
                .map(item -> item.startsWith("6") ? "sh" + item : "sz" + item)
                .collect(Collectors.toList());
        return allStockCodes;
    }

    @Override
    public void updateStockInfoById(StockBusiness info) {

    }
}
