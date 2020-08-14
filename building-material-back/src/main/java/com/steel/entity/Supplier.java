package com.steel.entity;

import java.io.Serializable;

/**
 * (Supplier)实体类
 *
 * @author makejava
 * @since 2020-03-31 11:21:49
 */
public class Supplier implements Serializable {
    private static final long serialVersionUID = 797406455528646092L;
    
    private Integer id;
    /**
    * 供应商号
    */
    private String supplierNumber;
    /**
    * 供应商名称
    */
    private String supplierName;
    /**
    * 联系人
    */
    private String supplierContacts;
    /**
    * 联系电话
    */
    private String supplierPhone;
    /**
    * 联系地址
    */
    private String supplierAddress;
    /**
    * 供应商评价
    */
    private String supplierEvaluate;
    /**
    * 备注
    */
    private String supplierNote;
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

    public String getSupplierNumber() {
        return supplierNumber;
    }

    public void setSupplierNumber(String supplierNumber) {
        this.supplierNumber = supplierNumber;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getSupplierContacts() {
        return supplierContacts;
    }

    public void setSupplierContacts(String supplierContacts) {
        this.supplierContacts = supplierContacts;
    }

    public String getSupplierPhone() {
        return supplierPhone;
    }

    public void setSupplierPhone(String supplierPhone) {
        this.supplierPhone = supplierPhone;
    }

    public String getSupplierAddress() {
        return supplierAddress;
    }

    public void setSupplierAddress(String supplierAddress) {
        this.supplierAddress = supplierAddress;
    }

    public String getSupplierEvaluate() {
        return supplierEvaluate;
    }

    public void setSupplierEvaluate(String supplierEvaluate) {
        this.supplierEvaluate = supplierEvaluate;
    }

    public String getSupplierNote() {
        return supplierNote;
    }

    public void setSupplierNote(String supplierNote) {
        this.supplierNote = supplierNote;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

}