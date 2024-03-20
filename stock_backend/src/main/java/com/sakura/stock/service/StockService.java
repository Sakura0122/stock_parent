package com.sakura.stock.service;

import com.sakura.stock.pojo.domain.*;
import com.sakura.stock.vo.resp.PageResult;
import com.sakura.stock.vo.resp.R;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @author: sakura
 * @date: 2024/3/14 21:41
 * @description: 股票服务接口
 */
public interface StockService {
    /**
     * 获取国内大盘最新数据
     * @return
     */
    List<InnerMarketDomain> getInnerMarketInfo();

    /**
     * 获取沪深两市板块最新数据，以交易总金额降序查询，取前10条数据
     * @return
     */
    List<StockBlockDomain> getSectorInfo();

    /**
     * 股票最新数据
     *
     * @return
     */
    PageResult<StockUpdownDomain> getStockPageInfo(Integer page, Integer pageSize);

    /**
     * 涨幅榜
     * @return
     */
    List<StockUpdownDomain> getStockIncrease();

    /**
     * 统计最新股票交易日内每分钟涨跌停的股票数量
     * @return
     */
    Map<String, List> getStockUpDownCount();

    /**
     * 导出指定页码最新股票信息
     * @param page
     * @param pageSize
     * @param response
     */
    void exportStockUpDownInfo(Integer page, Integer pageSize, HttpServletResponse response);

    /**
     * 统计大盘T日和T-1日每分钟交易量
     * @return
     */
    Map<String, List> getComparedStockTradeAmt();

    /**
     * 获取最新交易时间下股票（A股）在各个涨幅区间下的数量
     * @return
     */
    Map getIncreaseRange();

    /**
     * 查询单个个股的分时行情数据
     * @param code 股票编码
     * @return
     */
    List<Stock4MinuteDomain> getStockScreenTimeSharing(String code);

    /**
     * 指定股票日K线数据
     * @param code 股票编码
     * @return
     */
    List<Stock4EvrDayDomain> getStockScreenDKLine(String code);
}
