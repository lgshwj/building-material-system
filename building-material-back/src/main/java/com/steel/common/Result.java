package com.steel.common;

import lombok.Getter;

/**
 * @ProjectName: building-material
 * @Package: com.steel.common
 * @ClassName: Result
 * @Author: Hero
 * @Description: 返回类
 * @Date: 2020/2/21 21:22
 * @Version: 1.0
 */
@Getter
public class Result<T> {
    private String code;

    private String msg;

    private T data;

    private Integer total;

    public Result(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Result(String code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public Result(String code, String msg, T data, Integer total) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.total = total;
    }
}
