package com.steel.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * (MaterialCategory)实体类
 *
 * @author makejava
 * @since 2020-03-13 17:01:27
 */
@Getter
@Setter
public class MaterialCategory implements Serializable {
    private static final long serialVersionUID = 213344741564046216L;
    
    private Integer id;
    /**
    * 建筑材料名称
    */
    private String materialCategoryName;
    /**
    * 建筑材料类别简介
    */
    private String materialCategoryIntroduce;
    
    private Integer isDelete;

}