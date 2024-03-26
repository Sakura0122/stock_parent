package com.sakura.stock;

import com.sakura.stock.service.StockTimerTaskService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * @author: sakura
 * @date: 2024/3/20 22:26
 * @description:
 */
@SpringBootTest
public class TestRestTemplate {

    @Resource
    private RestTemplate restTemplate;

    @Resource
    private StockTimerTaskService stockTimerTaskService;

    @Test
    public void test() {
        String url = "http://localhost:8091/api/quot/index/all";
        ResponseEntity<String> resp = restTemplate.getForEntity(url, String.class);
        // 1.获取响应头
        HttpHeaders headers = resp.getHeaders();
        System.out.println(headers);
        // 2.获取响应状态码
        int statusCode = resp.getStatusCodeValue();
        System.out.println(statusCode);
        // 3.获取响应体
        String body = resp.getBody();
        System.out.println(body);
    }

    /**
     * 测试国内大盘数据
     */
    @Test
    public void testInnerGetMarketInfo() {
        // stockTimerTaskService.getInnerMarketInfo();
        stockTimerTaskService.getStockRtIndex();
    }
}
