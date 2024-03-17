package com.sakura.stock.mapper;

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
     * @param curDate 日期时间
     * @return
     */
    List<StockUpdownDomain> getStockInfoByTime(@Param("curDate") Date curDate);

    /**
     * 涨幅榜
     * @return
     */
    List<StockUpdownDomain> getStockIncrease();


    /**
     * 统计最新股票交易日内每分钟涨跌停的股票数量
     * @param startDate 开盘时间
     * @param endDate 截止时间
     * @param flag 涨跌停标识
     * @return
     */
    List<Map> getStockUpDownCount(@Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("flag") String flag);
}
