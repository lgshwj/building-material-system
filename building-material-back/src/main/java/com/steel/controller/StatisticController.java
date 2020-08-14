package com.steel.controller;

import com.steel.common.Result;
import com.steel.common.VerifyToken;
import com.steel.service.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @ProjectName: building-material
 * @Package: com.steel.controller
 * @ClassName: StatisticController
 * @Author: Hero
 * @Description: 统计controller
 * @Date: 2020/4/30 16:36
 * @Version: 1.0
 */
@RestController
@RequestMapping(value = "/statistic")
public class StatisticController {

    @Autowired
    private StatisticService statisticService;

    @VerifyToken
    @GetMapping("/statisticTotalPurchase")
    public Result statisticTotalPurchase() {
        return statisticService.statisticTotalPurchase();
    }

    @VerifyToken
    @GetMapping("/statisticBySelect")
    public Result statisticByUserSelect(Integer type, String startDate, String endDate) throws Exception {
        return statisticService.statisticBySelect(type, startDate, endDate);
    }
}
