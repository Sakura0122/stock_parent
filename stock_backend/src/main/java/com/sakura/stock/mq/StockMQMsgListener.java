package com.sakura.stock.mq;

import com.sakura.stock.service.StockService;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import com.github.benmanes.caffeine.cache.Cache;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author: sakura
 * @date: 2024/3/24 22:00
 * @description: 定义股票相关mq消息监听
 */
@Component
@Slf4j
public class StockMQMsgListener {

    @Resource
    private Cache<String, Object> caffeineCache;

    @Resource
    private StockService stockService;

    // @RabbitListener(queues = "innerMarketQueue")
    public void refreshInnerMarketInfo(Date startTime) {
        // 统计当前时间点与发送消息时时间点差值 如果超过一分钟 则告警
        // 获取时间毫秒差值
        long diffTime = DateTime.now().getMillis() - new DateTime(startTime).getMillis();
        // 超过一分钟告警
        if (diffTime > 60000) {
            log.error("采集国内大盘时间点：{},同步超时：{}ms", new DateTime(startTime).toString("yyyy-MM-dd HH:mm:ss"), diffTime);
        }

        // 将缓存置为失效删除
        caffeineCache.invalidate("innerMarketKey");
        // 调用服务更新缓存
        stockService.getInnerMarketInfo();
    }
}
