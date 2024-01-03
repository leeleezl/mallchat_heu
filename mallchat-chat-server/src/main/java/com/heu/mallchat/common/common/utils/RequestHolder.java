package com.heu.mallchat.common.common.utils;

import com.heu.mallchat.common.common.domain.dto.RequestInfo;

/**
 * q    请求的上下文
 */
public class RequestHolder {

    private static  final ThreadLocal<RequestInfo> threadLocal = new ThreadLocal<>();

    public static void set(RequestInfo requestInfo) {
        threadLocal.set(requestInfo);
    }
    public static RequestInfo get() {
        return threadLocal.get();
    }
    public static void remove() {
        threadLocal.remove();
    }

}
