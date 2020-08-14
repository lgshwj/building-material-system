package com.steel.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @ProjectName: building-material
 * @Package: com.steel.common
 * @ClassName: VerifyToken
 * @Author: Hero
 * @Description: 注解验证
 * @Date: 2020/2/21 21:21
 * @Version: 1.0
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface VerifyToken {
    boolean required() default true;
}
