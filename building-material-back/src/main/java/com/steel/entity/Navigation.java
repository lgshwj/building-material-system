package com.steel.entity;

import io.swagger.models.auth.In;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @ProjectName: building-material
 * @Package: com.steel.entity
 * @ClassName: Navigation
 * @Author: Hero
 * @Description: 导航实体
 * @Date: 2020/3/9 16:28
 * @Version: 1.0
 */
@Getter
@Setter
public class Navigation implements Serializable {

    private static final long serialVersionUID = 538759017877598263L;

    private Integer id;

    private String navigationName;

    private String navigationRoute;

    private Integer isDelete;
}
