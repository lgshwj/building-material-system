package com.steel.entity;

import io.swagger.models.auth.In;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

/**
 * @ProjectName: building-material
 * @Package: com.steel.entity
 * @ClassName: Barchart
 * @Author: Hero
 * @Description: 柱形统计图
 * @Date: 2020/4/29 10:05
 * @Version: 1.0
 */
@Getter
@Setter
public class Barchart {
    private String name;

    private String type;

    private Integer barGap;

    private ArrayList<Double> data;

    public Barchart(String name, ArrayList<Double> data) {
        this.name = name;
        this.data = data;
        this.type = "bar";
        this.barGap = 0;
    }
}
