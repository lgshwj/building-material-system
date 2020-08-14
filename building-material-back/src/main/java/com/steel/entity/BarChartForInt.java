package com.steel.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

/**
 * @ProjectName: building-material
 * @Package: com.steel.entity
 * @ClassName: BarChartForInt
 * @Author: Hero
 * @Description:
 * @Date: 2020/4/30 19:17
 * @Version: 1.0
 */
@Getter
@Setter
public class BarChartForInt {
    private String name;

    private String type;

    private Integer barGap;

    private ArrayList<Integer> data;

    public BarChartForInt(String name, ArrayList<Integer> data) {
        this.name = name;
        this.data = data;
        this.type = "bar";
        this.barGap = 0;
    }
}
