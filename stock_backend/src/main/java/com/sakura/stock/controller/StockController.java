package com.sakura.stock.controller;

import com.sakura.stock.pojo.domain.*;
import com.sakura.stock.service.StockService;
import com.sakura.stock.vo.resp.PageResult;
import com.sakura.stock.vo.resp.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author: sakura
 * @date: 2024/3/14 21:38
 * @description: 定义股票相关接口
 */
@Api(tags = {"股票接口"})
@RestController
@RequestMapping("/api/quot")
public class StockController {

    @Resource
    private StockService stockService;

    /**
     * 获取国内大盘最新数据
     *
     * @return
     */
    @ApiOperation(value = "获取国内大盘最新数据", notes = "获取国内大盘最新数据", httpMethod = "GET")
    @GetMapping("/index/all")
    public R<List<InnerMarketDomain>> getInnerMarketInfo() {
        return R.ok(stockService.getInnerMarketInfo());
    }

    /**
     * 获取沪深两市板块最新数据，以交易总金额降序查询，取前10条数据
     *
     * @return
     */
    @ApiOperation(value = "获取沪深两市板块最新数据", notes = "获取沪深两市板块最新数据，以交易总金额降序查询，取前10条数据", httpMethod = "GET")
    @GetMapping("/sector/all")
    public R<List<StockBlockDomain>> getSectorInfo() {
        return R.ok(stockService.getSectorInfo());
    }

    /**
     * 股票最新数据
     *
     * @param page     当前页
     * @param pageSize 每页大小
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "page", value = "当前页"),
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "pageSize", value = "每页大小")
    })
    @ApiOperation(value = "股票最新数据", notes = "分页查询最新股票交易时间点下沪深两市个股行情数据，并根据涨幅降序排序展示", httpMethod = "GET")
    @GetMapping("/stock/all")
    public R<PageResult<StockUpdownDomain>> getStockPageInfo(
            @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(name = "pageSize", required = false, defaultValue = "20") Integer pageSize) {
        return R.ok(stockService.getStockPageInfo(page, pageSize));
    }

    /**
     * 涨幅榜
     */
    @ApiOperation(value = "涨幅榜", notes = "统计沪深两市个股最新交易数据，并按涨幅降序排序查询前4条数据", httpMethod = "GET")
    @GetMapping("/stock/increase")
    public R<List<StockUpdownDomain>> getStockIncrease() {
        return R.ok(stockService.getStockIncrease());
    }

    /**
     * 统计最新股票交易日内每分钟涨跌停的股票数量
     *
     * @return
     */
    @ApiOperation(value = "统计最新股票交易日内每分钟涨跌停的股票数量", notes = "统计最新股票交易日内每分钟涨跌停的股票数量", httpMethod = "GET")
    @GetMapping("/stock/updown/count")
    public R<Map<String, List>> getStockUpDownCount() {
        return R.ok(stockService.getStockUpDownCount());
    }

    /**
     * 导出指定页码的最新股票信息
     *
     * @param page     当前页
     * @param pageSize 当前页大小
     * @param response 响应对象
     * @throws IOException
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "page", value = "当前页"),
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "pageSize", value = "当前页大小")
    })
    @ApiOperation(value = "导出指定页码的最新股票信息", notes = "导出指定页码的最新股票信息", httpMethod = "GET")
    @GetMapping("/stock/export")
    public void exportStockUpDownInfo(
            @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(name = "pageSize", required = false, defaultValue = "20") Integer pageSize,
            HttpServletResponse response) throws IOException {
        stockService.exportStockUpDownInfo(page, pageSize, response);
    }

    /**
     * 统计大盘T日和T-1日每分钟交易量
     *
     * @return
     */
    @ApiOperation(value = "统计大盘T日和T-1日每分钟交易量", notes = "统计大盘T日和T-1日每分钟交易量", httpMethod = "GET")
    @GetMapping("/stock/tradeAmt")
    public R<Map<String, List>> getComparedStockTradeAmt() {
        return R.ok(stockService.getComparedStockTradeAmt());
    }

    /**
     * 获取最新交易时间下股票（A股）在各个涨幅区间下的数量
     *
     * @return
     */
    @ApiOperation(value = "获取最新交易时间下股票（A股）在各个涨幅区间下的数量", notes = "获取最新交易时间下股票（A股）在各个涨幅区间下的数量", httpMethod = "GET")
    @GetMapping("/stock/updown")
    public R<Map> getIncreaseRange() {
        return R.ok(stockService.getIncreaseRange());
    }

    /**
     * 查询单个个股的分时行情数据
     *
     * @param code 股票编码
     * @return
     */
    @ApiOperation(value = "查询单个个股的分时行情数据", notes = "如果当前日期不在有效时间内，则以最近的一个股票交易时间作为查询时间点", httpMethod = "GET")
    @GetMapping("/stock/screen/time-sharing")
    public R<List<Stock4MinuteDomain>> getStockScreenTimeSharing(String code) {
        if (StringUtils.isBlank(code)) {
            return R.error("股票编码不能为空");
        }
        return R.ok(stockService.getStockScreenTimeSharing(code));
    }

    /**
     * 指定股票日K线数据
     * @param code 股票编码
     * @return
     */
    @ApiOperation(value = "指定股票日K线数据", notes = "指定股票日K线数据", httpMethod = "GET")
    @GetMapping("/stock/screen/dkline")
    public R<List<Stock4EvrDayDomain>> getStockScreenDKLine(String code) {
        if (StringUtils.isBlank(code)) {
            return R.error("股票编码不能为空");
        }
        return R.ok(stockService.getStockScreenDKLine(code));
    }
}
