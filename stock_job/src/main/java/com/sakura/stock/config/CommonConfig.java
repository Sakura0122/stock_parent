package com.sakura.stock.config;

import com.sakura.stock.pojo.vo.StockInfoConfig;
import com.sakura.stock.utils.IdWorker;
import com.sakura.stock.utils.ParserStockInfoUtil;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: sakura
 * @date: 2024/3/11 23:04
 * @description: 公共配置bean
 */
@Configuration
@EnableConfigurationProperties({StockInfoConfig.class})
public class CommonConfig {

    /**
     * 基于雪花算法的ID生成器
     * @return
     */
    @Bean
    public IdWorker idWorker(){
        // 由运维人员指定
        return new IdWorker(1L, 2L);
    }

    /**
     * 定义解析股票 大盘 外盘 个股 板块相关信息的工具类bean
     * @return
     */
    @Bean
    public ParserStockInfoUtil parserStockInfoUtil(){
        return new ParserStockInfoUtil(idWorker());
    }
}
