package com.steel.entity;

import java.io.Serializable;

/**
 * (Factory)实体类
 *
 * @author makejava
 * @since 2020-03-31 20:43:23
 */
public class Factory implements Serializable {
    private static final long serialVersionUID = 292474707697796285L;
    /**
    * id主键
    */
    private Integer id;
    /**
    * 厂家编号
    */
    private String factoryNumber;
    /**
    * 厂家名称
    */
    private String factoryName;
    /**
    * 厂家联系人
    */
    private String factoryContacts;
    /**
    * 联系电话
    */
    private String factoryPhone;
    
    private String factoryAddress;
    /**
    * 评价
    */
    private String factoryEvaluate;
    /**
    * 备注
    */
    private String factoryNote;
    /**
    * 0代表未删，1代表已删
    */
    private Integer isDelete;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFactoryNumber() {
        return factoryNumber;
    }

    public void setFactoryNumber(String factoryNumber) {
        this.factoryNumber = factoryNumber;
    }

    public String getFactoryName() {
        return factoryName;
    }

    public void setFactoryName(String factoryName) {
        this.factoryName = factoryName;
    }

    public String getFactoryContacts() {
        return factoryContacts;
    }

    public void setFactoryContacts(String factoryContacts) {
        this.factoryContacts = factoryContacts;
    }

    public String getFactoryPhone() {
        return factoryPhone;
    }

    public void setFactoryPhone(String factoryPhone) {
        this.factoryPhone = factoryPhone;
    }

    public String getFactoryAddress() {
        return factoryAddress;
    }

    public void setFactoryAddress(String factoryAddress) {
        this.factoryAddress = factoryAddress;
    }

    public String getFactoryEvaluate() {
        return factoryEvaluate;
    }

    public void setFactoryEvaluate(String factoryEvaluate) {
        this.factoryEvaluate = factoryEvaluate;
    }

    public String getFactoryNote() {
        return factoryNote;
    }

    public void setFactoryNote(String factoryNote) {
        this.factoryNote = factoryNote;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

}