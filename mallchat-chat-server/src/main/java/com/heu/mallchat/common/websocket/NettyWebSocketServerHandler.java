package com.heu.mallchat.common.websocket;

import cn.hutool.json.JSONUtil;
import com.heu.mallchat.common.websocket.domain.enums.WSReqTypeEnum;
import com.heu.mallchat.common.websocket.domain.vo.req.WSBaseReq;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

public class NettyWebSocketServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private void userOffLine(ChannelHandlerContext ctx) {
        ctx.channel().close();
        System.out.println("用户离线");
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            //读空闲
            if (idleStateEvent.state() == IdleState.READER_IDLE) {
                //关闭用户的连接
                userOffLine(ctx);
            }
        } else if(evt instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
            System.out.println("握手完成");
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        WSBaseReq wsBaseReq = JSONUtil.toBean(msg.text(), WSBaseReq.class);
        WSReqTypeEnum wsReqTypeEnum = WSReqTypeEnum.of(wsBaseReq.getType());
        switch (wsReqTypeEnum) {
            case  LOGIN:
                System.out.println("请求二维码 = " + msg.text());
                ctx.channel().writeAndFlush(new TextWebSocketFrame("123"));
                break;
            case HEARTBEAT:
                break;
            case AUTHORIZE:
                break;
        }
    }

}
