package com.sakura.stock.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.sakura.stock.pojo.vo.StockInfoConfig;
import com.sakura.stock.utils.IdWorker;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author: sakura
 * @date: 2024/3/11 23:04
 * @description: 公共配置bean
 */
@Configuration
@EnableConfigurationProperties({StockInfoConfig.class})
public class CommonConfig {
    /**
     * 密码加密器
     * BCryptPasswordEncoder方法采用SHA-256对密码进行加密
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /**
     * 基于雪花算法的ID生成器
     * @return
     */
    @Bean
    public IdWorker idWorker(){
        // 由运维人员指定
        return new IdWorker(1L, 2L);
    }
}
