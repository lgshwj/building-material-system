package com.steel.dao;

import com.steel.entity.QuotePrice;
import com.steel.entity.Supplier;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * (QuotePrice)表数据库访问层
 *
 * @author makejava
 * @since 2020-04-19 18:07:56
 */
@Mapper
public interface QuotePriceDao {


    /**
     * 修改数据
     *
     * @param quotePrice 实例对象
     * @return 影响行数
     */
    int update(QuotePrice quotePrice);

    /**
     * 通过主键删除数据
     *
     * @param ids 主键
     * @return 影响行数
     */
    int deleteById(Integer[] ids);

    List<QuotePrice> queryQuotePrice(
                String norm, String materialQuality,
                String factoryName, Integer supplierId, String materialName,
                String releaseStartTime, String releaseEndTime,
                Integer state,
                Integer offset, Integer limit
    );

    Integer queryQuotePriceCount(String norm, String materialQuality, String factoryName, Integer supplierId, String materialName,
                                 String releaseStartTime, String releaseEndTime,
                                 Integer state);

    Integer insertBatch(@Param("list")List<QuotePrice> list);

    List<Supplier> searchSupplierByMaterialId(Integer materialId);

    // 覆盖经销商原有材料报价
    Integer updateQuotePrice(Integer supplierId, Integer materialId);

    // 查询起购量
    Integer searchMinPurchase(Integer supplierId, Integer materialId);

    List<QuotePrice> analysisQuotePrice(Integer materialId, String startDate, String endDate);
}