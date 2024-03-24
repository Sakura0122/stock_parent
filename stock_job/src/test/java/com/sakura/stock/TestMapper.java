package com.sakura.stock;

import cn.hutool.core.collection.ListUtil;
import com.sakura.stock.mapper.StockBusinessMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: sakura
 * @date: 2024/3/24 18:02
 * @description:
 */
@SpringBootTest
public class TestMapper {
    @Resource
    private StockBusinessMapper stockBusinessMapper;

    /**
     * 测试获取所有个股编码集合
     */
    @Test
    public void test01() {
        List<String> allStockCodes = stockBusinessMapper.getAllStockCodes();
        allStockCodes = allStockCodes.stream()
                .map(item -> item.startsWith("6") ? "sh" + item : "sz" + item)
                .collect(Collectors.toList());
        // 将所有个股编码组成大的集合分成若干小集合
        List<List<String>> split = ListUtil.split(allStockCodes, 15);
        System.out.println(split);
    }
}
