package com.steel.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @ProjectName: building-material
 * @Package: com.steel.entity
 * @ClassName: SelectOptions
 * @Author: Hero
 * @Description: 实体
 * @Date: 2020/4/28 12:25
 * @Version: 1.0
 */
@Getter
@Setter
public class SelectOptions {

    private Integer value;

    private String label;

    private List<SelectOptions> children;

    public SelectOptions() {
    }

    public SelectOptions(Integer value, String label) {
        this.value = value;
        this.label = label;
    }

    public SelectOptions(Integer value, String label, List<SelectOptions> children) {
        this.value = value;
        this.label = label;
        this.children = children;
    }
}
