package com.linyuanbaobao.payload;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author linyuan - 765371578@qq.com
 * @since 2022/1/20
 */
@RestControllerAdvice
@ConditionalOnProperty(prefix = "spring.mvc.payload", name = "biz-error-enabled", havingValue = "true")
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorPayload handleException(Exception e) {
        return new ErrorPayload(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BizException.class)
    public ErrorPayload handleException(BizException e) {
        return new ErrorPayload(e.getMessage(), e.getCode());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public ErrorPayload handleException(BindException e) {
        ObjectError error = e.getAllErrors().get(0);
        String errMsg = (error != null) ? error.getDefaultMessage() : "参数校验错误！";
        return new ErrorPayload(errMsg);
    }

    public static class ErrorPayload {

        private Integer code = 400;

        private final Boolean success = false;

        private final String message;

        private final Long timestamp = System.currentTimeMillis();

        public ErrorPayload(String message) {
            this.message = message;
        }

        public ErrorPayload(String message, Integer code) {
            this.message = message;
            this.code = code;
        }

        public Integer getCode() {
            return code;
        }

        public Boolean getSuccess() {
            return success;
        }

        public String getMessage() {
            return message;
        }

        public Long getTimestamp() {
            return timestamp;
        }
    }

}
