package com.byteblogs.common.annotation;

import com.byteblogs.system.enums.RoleEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: zsg
 * @description:
 * @date: 2019/8/28 23:02
 * @modified:
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginRequired {
    boolean required() default true;

    RoleEnum role() default RoleEnum.ADMIN;
}
