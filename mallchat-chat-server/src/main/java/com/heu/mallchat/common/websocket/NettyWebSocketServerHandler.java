package com.heu.mallchat.common.websocket;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import com.heu.mallchat.common.websocket.domain.enums.WSReqTypeEnum;
import com.heu.mallchat.common.websocket.domain.vo.req.WSBaseReq;
import com.heu.mallchat.common.websocket.service.WebSocketService;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.springframework.beans.factory.annotation.Autowired;
@ChannelHandler.Sharable
public class NettyWebSocketServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private WebSocketService webSocketService;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        webSocketService = SpringUtil.getBean(WebSocketService.class);
        webSocketService.connect(ctx.channel());
    }

    private void userOffLine(ChannelHandlerContext ctx) {
        webSocketService.remove(ctx.channel());
        ctx.channel().close();
//        System.out.println("用户离线");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        userOffLine(ctx);
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
            this.webSocketService.connect(ctx.channel());
            String token = NettyUtil.getAttr(ctx.channel(), NettyUtil.TOKEN);
            if (StrUtil.isNotBlank(token)) {
                this.webSocketService.authorize(ctx.channel(), token);
            }
            System.out.println("握手完成");
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
            WSBaseReq wsBaseReq = JSONUtil.toBean(msg.text(), WSBaseReq.class);
            WSReqTypeEnum wsReqTypeEnum = WSReqTypeEnum.of(wsBaseReq.getType());
            switch (wsReqTypeEnum) {
                case  LOGIN:
                    webSocketService.handleLoginRequest(ctx.channel());
                    break;
                case HEARTBEAT:
                    break;
                case AUTHORIZE:
                    webSocketService.authorize(ctx.channel(), wsBaseReq.getData());
                    break;
            }
        }

}
