package com.sakura.stock.job;

import com.sakura.stock.service.StockTimerTaskService;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author: sakura
 * @date: 2024/3/26 14:17
 * @description: 定义xxljob任务执行器bean
 */
@Component
public class StockJob {
    @Resource
    private StockTimerTaskService stockTimerTaskService;

    /**
     * 1、简单任务示例（Bean模式）
     */
    @XxlJob("myJobHandler")
    public void demoJobHandler() throws Exception {
        System.out.println("当前时间点为：" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-M-dd HH:mm")));
    }

    /**
     * 定时采集国内大盘的实时数据信息
     * 建议：针对不同股票数据 定义不同采集任务 解耦 方便维护
     */
    @XxlJob("getInnerMarketInfo")
    public void getInnerMarketInfo() {
        stockTimerTaskService.getInnerMarketInfo();
    }

    /**
     * 定时采集A股大盘数据
     */
    @XxlJob("getStockInfos")
    public void getStockInfos() {
        stockTimerTaskService.getStockRtIndex();
    }
}
