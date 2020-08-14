package com.steel.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.io.Serializable;

/**
 * (User)实体类
 *
 * @author makejava
 * @since 2020-02-21 22:00:33
 */
@Getter
@Setter
public class User implements Serializable {
    private static final long serialVersionUID = 538759017877598263L;
    
    private Integer id;
    /**
    * 账号
    */
    private String username;

    private String nickname;
    
    private String password;
    /**
    * 创建时间
    */
    private String createTime;
    /**
    * 修改时间
    */
    private String updateTime;

    private Integer isDelete;

}