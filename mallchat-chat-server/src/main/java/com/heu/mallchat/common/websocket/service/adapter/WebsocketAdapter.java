package com.heu.mallchat.common.websocket.service.adapter;

import com.heu.mallchat.common.common.enums.YesOrNoEnum;
import com.heu.mallchat.common.user.domain.entity.User;
import com.heu.mallchat.common.websocket.domain.enums.WSRespTypeEnum;
import com.heu.mallchat.common.websocket.domain.vo.resp.WSBaseResp;
import com.heu.mallchat.common.websocket.domain.vo.resp.WSBlack;
import com.heu.mallchat.common.websocket.domain.vo.resp.WSLoginSuccess;
import com.heu.mallchat.common.websocket.domain.vo.resp.WSLoginUrl;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;

public class WebsocketAdapter {
    public static WSBaseResp<?> buildResp(WxMpQrCodeTicket wxMpQrCodeTicket) {
        WSBaseResp<WSLoginUrl> resp = new WSBaseResp<>();
        resp.setType(WSRespTypeEnum.LOGIN_URL.getType());
        resp.setData(new WSLoginUrl(wxMpQrCodeTicket.getUrl()));
        return  resp;
    }

    public static WSBaseResp<?> buildResp(User user, String token) {
        WSBaseResp<WSLoginSuccess> resp = new WSBaseResp<>();
        resp.setType(WSRespTypeEnum.LOGIN_SUCCESS.getType());
        WSLoginSuccess loginSuccess = WSLoginSuccess.builder()
                .avatar(user.getAvatar())
                .name(user.getName())
                .token(token)
                .uid(user.getId())
                .build();
        resp.setData(loginSuccess);
        return resp;
    }
    public static WSBaseResp<?> buildResp(User user, String token, boolean power) {
        WSBaseResp<WSLoginSuccess> resp = new WSBaseResp<>();
        resp.setType(WSRespTypeEnum.LOGIN_SUCCESS.getType());
        WSLoginSuccess loginSuccess = WSLoginSuccess.builder()
                .avatar(user.getAvatar())
                .name(user.getName())
                .token(token)
                .uid(user.getId())
                .power(power? YesOrNoEnum.YES.getStatus() : YesOrNoEnum.NO.getStatus())
                .build();
        resp.setData(loginSuccess);
        return resp;
    }

    public static WSBaseResp<?> buildWaitAuthorizeResp() {
        WSBaseResp<WSLoginUrl> resp = new WSBaseResp<>();
        resp.setType(WSRespTypeEnum.LOGIN_SCAN_SUCCESS.getType());
        return resp;
    }

    public static WSBaseResp<?> buildInvalidTokenResp() {
        WSBaseResp<WSLoginUrl> resp = new WSBaseResp<>();
        resp.setType(WSRespTypeEnum.INVALIDATE_TOKEN.getType());
        return resp;
    }

    public static WSBaseResp<?> buildBlack(User user) {
        WSBaseResp<WSBlack> resp = new WSBaseResp<>();
        resp.setType(WSRespTypeEnum.BLACK.getType());
        WSBlack build = WSBlack.builder()
                .uid(user.getId())
                .build();
        resp.setData(build);
        return resp;
    }
}
