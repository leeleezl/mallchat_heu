package com.heu.mallchat.common.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CommonErrorEnum implements ErrorEnum{

    PARAM_INVALID(-2, "参数校验失败"),
    SYSTEM_ERROR(-1, "系统出小差了，请稍后再试哦~~~"),
    BUSINESS_ERROR(0, "{0}"),
    ;

    private final Integer code;
    private final String msg;

    @Override
    public Integer getErrorCode() {
        return code;
    }

    @Override
    public String getErrorMsg() {
        return msg;
    }
}
