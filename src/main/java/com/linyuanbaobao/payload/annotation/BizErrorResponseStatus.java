package com.linyuanbaobao.payload.annotation;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author linyuan - szlinyuan@ininin.com
 * @since 2021/6/7
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public @interface BizErrorResponseStatus {
    int value() default 400;
}

