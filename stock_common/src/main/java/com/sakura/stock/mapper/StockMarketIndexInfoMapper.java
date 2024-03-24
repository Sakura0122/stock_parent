package com.sakura.stock.mapper;

import com.sakura.stock.pojo.domain.InnerMarketDomain;
import com.sakura.stock.pojo.domain.StockBlockDomain;
import com.sakura.stock.pojo.entity.StockMarketIndexInfo;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
* @author sakura
* @description 针对表【stock_market_index_info(国内大盘数据详情表)】的数据库操作Mapper
* @createDate 2024-03-10 20:35:10
* @Entity com.sakura.stock.pojo.entity.StockMarketIndexInfo
*/
public interface StockMarketIndexInfoMapper {

    int deleteByPrimaryKey(Long id);

    int insert(StockMarketIndexInfo record);

    int insertSelective(StockMarketIndexInfo record);

    StockMarketIndexInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(StockMarketIndexInfo record);

    int updateByPrimaryKey(StockMarketIndexInfo record);

    /**
     * 获取大盘信息
     * @param date 指定时间点
     * @param marketCodes 大盘编码集合
     * @return
     */
    List<InnerMarketDomain> getMarketInfo(@Param("date") Date date, @Param("marketCodes") List<String> marketCodes);

    /**
     * 获取沪深两市板块最新数据，以交易总金额降序查询，取前10条数据
     * @param date
     * @return
     */
    List<StockBlockDomain> getSectorInfo(Date date);

    /**
     * 批量插入大盘数据
     * @param list 大盘集合
     * @return
     */
    int insertBatch(@Param("list") ArrayList<StockMarketIndexInfo> list);
}
