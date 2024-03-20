package com.sakura.stock.pojo.vo;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import java.util.List;

/**
 * @author: sakura
 * @date: 2024/3/14 20:46
 * @description: 定义股票相关信息
 */
@ConfigurationProperties(prefix = "stock")
@Data
public class StockInfoConfig {
    // A股大盘ID集合
    private List<String> inner;
    // 外盘ID集合
    private List<String> outer;
    // 股票涨幅区间集合
    private List<String> upDownRange;
}
