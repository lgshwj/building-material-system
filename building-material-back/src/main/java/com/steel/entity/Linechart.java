package com.steel.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

/**
 * @ProjectName: building-material
 * @Package: com.steel.entity
 * @ClassName: Linechart
 * @Author: Hero
 * @Description: 折线图实体
 * @Date: 2020/4/28 17:16
 * @Version: 1.0
 */
@Getter
@Setter
public class Linechart {
    private String name;

    private String type;

    //private String stack;

    private ArrayList<Double> data;

    public Linechart(String name, ArrayList<Double> data) {
        this.name = name;
        this.type = "line";
        //this.stack = "总量";
        this.data = data;
    }
}
