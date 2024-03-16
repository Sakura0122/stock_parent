package com.sakura.stock.controller;

import com.sakura.stock.pojo.domain.InnerMarketDomain;
import com.sakura.stock.pojo.domain.StockBlockDomain;
import com.sakura.stock.service.StockService;
import com.sakura.stock.vo.resp.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author: sakura
 * @date: 2024/3/14 21:38
 * @description: 定义股票相关接口
 */
@Api(tags = {"股票接口"})
@RestController
@RequestMapping("/api/quot")
public class StockController {

    @Autowired
    private StockService stockService;

    /**
     * 获取国内大盘最新数据
     * @return
     */
    @ApiOperation(value = "获取国内大盘最新数据", httpMethod = "GET")
    @GetMapping("/index/all")
    public R<List<InnerMarketDomain>> getInnerMarketInfo(){
        return R.ok(stockService.getInnerMarketInfo());
    }

    /**
     * 获取沪深两市板块最新数据，以交易总金额降序查询，取前10条数据
     * @return
     */
    @ApiOperation(value = "获取沪深两市板块最新数据", notes = "获取沪深两市板块最新数据，以交易总金额降序查询，取前10条数据", httpMethod = "GET")
    @GetMapping("/sector/all")
    public R<List<StockBlockDomain>> getSectorInfo(){
        return R.ok(stockService.getSectorInfo());
    }
}
