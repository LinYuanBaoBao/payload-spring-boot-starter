package com.linyuanbaobao.payload;

import org.springframework.http.HttpStatus;

/**
 * @author linyuan - szlinyuan@ininin.com
 * @since 2021/12/23
 */
public class BizException extends RuntimeException {

    private Integer code = HttpStatus.BAD_REQUEST.value();

    public BizException(String msg) {
        super(msg);
    }

    public BizException(String msg, Integer code) {
        super(msg);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

}
