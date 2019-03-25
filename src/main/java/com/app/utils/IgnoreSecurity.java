package com.app.utils;

import java.lang.annotation.*;

/**
 * （自定义注解）
 * function： 安全性忽略注解
 */
@Target(ElementType.PARAMETER)//用于参数
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IgnoreSecurity {
}
