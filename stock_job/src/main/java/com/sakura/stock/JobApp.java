package com.sakura.stock;

import com.sakura.stock.pojo.vo.TaskThreadPoolInfo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@MapperScan("com.sakura.stock.mapper") // 扫描持久层mapper接口 生成代理对象 维护到ioc容器
@SpringBootApplication
@EnableConfigurationProperties({TaskThreadPoolInfo.class})
public class JobApp {
    public static void main(String[] args) {
        SpringApplication.run(JobApp.class, args);
    }
}
