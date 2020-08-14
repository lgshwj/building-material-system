package com.steel.service;

import com.steel.common.Result;


/**
 * @ProjectName: building-material
 * @Package: com.steel.service
 * @ClassName: StatisticService
 * @Author: Hero
 * @Description: 统计service
 * @Date: 2020/4/30 16:39
 * @Version: 1.0
 */
public interface StatisticService {


    Result statisticTotalPurchase();

    Result statisticBySelect(Integer type, String startDate, String endDate) throws Exception;
}
