package com.linyuanbaobao.payload.annotation;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.annotation.AliasFor;
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
@ResponseStatus
public @interface BizErrorResponseStatus {

    @AliasFor("code")
    int value() default 400;

    @AliasFor("value")
    int code() default 400;

    @AliasFor(annotation = ResponseStatus.class, attribute = "code")
    HttpStatus status() default HttpStatus.BAD_REQUEST;
}

