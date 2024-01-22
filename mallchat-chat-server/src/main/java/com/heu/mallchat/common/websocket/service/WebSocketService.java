package com.heu.mallchat.common.websocket.service;

import com.heu.mallchat.common.websocket.domain.vo.resp.WSBaseResp;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import me.chanjar.weixin.common.error.WxErrorException;

public interface WebSocketService {
    void connect(Channel channel);

    void handleLoginRequest(Channel channel) throws WxErrorException;

    void remove(Channel channel);

    void scanLoginSuccess(Integer code, Long id);

    void waitAuthorize(Integer code);

    void authorize(Channel channel, String token);

    void sendMsgToAll(WSBaseResp<?> msg);
}
