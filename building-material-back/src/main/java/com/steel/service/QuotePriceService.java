package com.steel.service;

import com.steel.common.Result;
import com.steel.entity.QuotePrice;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * (QuotePrice)表服务接口
 *
 * @author makejava
 * @since 2020-04-19 18:07:56
 */
public interface QuotePriceService {

    /**
     * 通过主键删除数据
     *
     * @param ids 主键
     * @return 是否成功
     */
    Result deleteById(Integer[] ids);

    Result queryQuotePrice(String norm, String materialQuality, String factoryName, Integer supplierId, String materialName,
                           String releaseStartTime, String releaseEndTime,
                           Integer state,
                           Integer pageIndex, Integer pageSize);

    Result uploadQuotePrice(MultipartFile uploadFile);

    Result searchSupplierByMaterialId(Integer materialId);

    Result analysisQuotePrice(Integer materialId, String startDate, String endDate) throws Exception;
}