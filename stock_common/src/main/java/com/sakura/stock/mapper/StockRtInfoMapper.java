package com.sakura.stock.mapper;

import com.sakura.stock.pojo.domain.Stock4EvrDayDomain;
import com.sakura.stock.pojo.domain.Stock4MinuteDomain;
import com.sakura.stock.pojo.domain.StockUpdownDomain;
import com.sakura.stock.pojo.entity.StockRtInfo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author sakura
 * @description 针对表【stock_rt_info(个股详情信息表)】的数据库操作Mapper
 * @createDate 2024-03-10 20:35:10
 * @Entity com.sakura.stock.pojo.entity.StockRtInfo
 */
public interface StockRtInfoMapper {

    int deleteByPrimaryKey(Long id);

    int insert(StockRtInfo record);

    int insertSelective(StockRtInfo record);

    StockRtInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(StockRtInfo record);

    int updateByPrimaryKey(StockRtInfo record);

    /**
     * 查询指定时间点下股票数据集合
     *
     * @param curDate 日期时间
     * @return
     */
    List<StockUpdownDomain> getStockInfoByTime(@Param("curDate") Date curDate);

    /**
     * 涨幅榜
     *
     * @return
     */
    List<StockUpdownDomain> getStockIncrease();


    /**
     * 统计最新股票交易日内每分钟涨跌停的股票数量
     *
     * @param startDate 开盘时间
     * @param endDate   截止时间
     * @param flag      涨跌停标识
     * @return
     */
    List<Map> getStockUpDownCount(@Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("flag") String flag);

    /**
     * 统计大盘T日和T-1日每分钟交易量
     *
     * @param startDate   开始时间
     * @param endDate     截止时间
     * @param marketCodes A股大盘ID集合
     * @return
     */
    List<Map> getSumAmtInfo(@Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("marketCodes") List<String> marketCodes);

    /**
     * 获取最新交易时间下股票（A股）在各个涨幅区间下的数量
     *
     * @param date 交易时间
     * @return
     */
    List<Map> getIncreaseRangeInfoByDate(@Param("date") Date date);

    /**
     * 查询单个个股的分时行情数据
     *
     * @param startDate 开始时间
     * @param endDate   截止时间
     * @param code      股票代码
     * @return
     */
    List<Stock4MinuteDomain> getStock4MinuteInfo(@Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("code") String code);

    /**
     * 指定股票日K线数据
     * @param startDate 开始时间
     * @param endDate 截止时间
     * @param code 股票代码
     * @return
     */
    List<Stock4EvrDayDomain> getStock4DkLine(@Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("code")String code);

    /**
     * 批量插入个股数据
     * @param list
     * @return
     */
    int insertBatch(@Param("list") List<StockRtInfo> list);
}
