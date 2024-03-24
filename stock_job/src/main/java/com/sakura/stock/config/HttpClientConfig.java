package com.sakura.stock.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author: sakura
 * @date: 2024/3/20 21:35
 * @description: 定义访问http服务的配置类
 */
@Configuration
public class HttpClientConfig {
    /**
     * 定义http客户端bean
     * @return
     */
    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
