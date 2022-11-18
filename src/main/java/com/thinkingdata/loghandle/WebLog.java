package com.thinkingdata.loghandle;

import java.lang.annotation.*;

/**
 * @author Fernando Zhu
 * @version 1.0
 * @date 2021/3/10 15:02
 */
// 注解作用于方法级别
@Target({ElementType.METHOD})
// 运行时起作用
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WebLog {
    /**
     * 日志信息描述,记录该方法的作用等信息
     */
    String description() default "";
}