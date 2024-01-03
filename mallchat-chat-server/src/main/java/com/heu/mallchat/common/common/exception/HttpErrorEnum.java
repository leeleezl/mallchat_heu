package com.heu.mallchat.common.common.exception;

import cn.hutool.http.ContentType;
import com.google.common.base.Charsets;
import com.heu.mallchat.common.common.domain.vo.resp.ApiResult;
import com.heu.mallchat.common.common.utils.JsonUtils;
import lombok.AllArgsConstructor;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@AllArgsConstructor
public enum HttpErrorEnum {
    ACCESS_DENIED(401,"登录失效，请重新登录"),

    ;

    private Integer httpCode;
    private String desc;

    public void sendHttpError(HttpServletResponse response) throws IOException {
        response.setStatus(httpCode);
        response.setContentType(ContentType.JSON.toString(Charsets.UTF_8));
        response.getWriter().write(JsonUtils.toStr(ApiResult.fail(httpCode, desc)));
    }
}
