package com.heu.mallchat.common.websocket.service.impl;


import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.heu.mallchat.common.common.config.ThreadPoolConfig;
import com.heu.mallchat.common.common.event.UserOnlineEvent;
import com.heu.mallchat.common.user.dao.UserDao;
import com.heu.mallchat.common.user.domain.entity.IpInfo;
import com.heu.mallchat.common.user.domain.entity.User;
import com.heu.mallchat.common.user.domain.enums.RoleEnum;
import com.heu.mallchat.common.user.service.IRoleService;
import com.heu.mallchat.common.user.service.IUserRoleService;
import com.heu.mallchat.common.user.service.LoginService;
import com.heu.mallchat.common.websocket.NettyUtil;
import com.heu.mallchat.common.websocket.domain.dto.WSChannelExtraDTO;
import com.heu.mallchat.common.websocket.domain.enums.WSRespTypeEnum;
import com.heu.mallchat.common.websocket.domain.vo.resp.WSBaseResp;
import com.heu.mallchat.common.websocket.domain.vo.resp.WSLoginUrl;
import com.heu.mallchat.common.websocket.service.WebSocketService;
import com.heu.mallchat.common.websocket.service.adapter.WebsocketAdapter;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 管理websocket逻辑，包括推拉
 */
@Service
public class WebsocketServiceImpl implements WebSocketService {

    @Autowired
    private UserDao userDao;

    @Autowired
    @Lazy
    private WxMpService wxMpService;

    @Autowired
    private LoginService loginService;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private IRoleService roleService;

    @Qualifier(ThreadPoolConfig.WS_EXECUTOR)
    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    /**
     * 管理所有用户的连接（登录态/游客）
     */
    private static final ConcurrentHashMap<Channel, WSChannelExtraDTO> ONLINE_WS_MAP = new ConcurrentHashMap<>();
    public static final int MAXIMUM_SIZE = 10000;
    public static final Duration DURATION = Duration.ofHours(1);
    /**
     * 临时保存登录code 和 channel 的映射关系
     */
    private static final Cache<Integer, Channel> WAIT_LOGIN_MAP = Caffeine.newBuilder()
            .maximumSize(MAXIMUM_SIZE)
            .expireAfterWrite(DURATION)
            .build();
    //保存
    @Override
    public void connect(Channel channel) {
        ONLINE_WS_MAP.put(channel, new WSChannelExtraDTO());
    }

    @Override
    public void handleLoginRequest(Channel channel) throws WxErrorException {
        // 生成随机码
        Integer code = generateLoginCode(channel);
        // 找微信申请带参二维码
        WxMpQrCodeTicket wxMpQrCodeTicket = wxMpService.getQrcodeService().qrCodeCreateTmpTicket(code, (int) DURATION.getSeconds());
        // 把二维码推送给前端
        sendMsg(channel, WebsocketAdapter.buildResp(wxMpQrCodeTicket));
    }

    @Override
    public void remove(Channel channel) {
        ONLINE_WS_MAP.remove(channel);
        // todo 用户下线
    }

    @Override
    public void scanLoginSuccess(Integer code, Long uid) {
        //确认连接在机器上
        Channel channel = WAIT_LOGIN_MAP.getIfPresent(code);
        if (Objects.isNull(channel)) {
            return ;
        }
        User user = userDao.getById(uid);
        //移除code
        WAIT_LOGIN_MAP.invalidate(code);
        //调用登录模块获取token
        String token = loginService.login(uid);
        // 用户登录成功
        loginSuccess(channel, user, token);
    }

    @Override
    public void waitAuthorize(Integer code) {
        Channel channel = WAIT_LOGIN_MAP.getIfPresent(code);
        if (Objects.isNull(channel)) {
            return ;
        }
        sendMsg(channel, WebsocketAdapter.buildWaitAuthorizeResp());

    }

    @Override
    public void authorize(Channel channel, String token) {
        Long validUid = loginService.getValidUid(token);
        if(Objects.nonNull(validUid)) {
            User user = userDao.getById(validUid);
            loginSuccess(channel, user, token);
        } else {
            sendMsg(channel, WebsocketAdapter.buildInvalidTokenResp());
        }
    }

    @Override
    public void sendMsgToAll(WSBaseResp<?> msg) {
        ONLINE_WS_MAP.forEach((channel, ext) ->{
            threadPoolTaskExecutor.execute(()->{
                sendMsg(channel, msg);
            });

        });
    }

    private void loginSuccess(Channel channel, User user, String token) {
        //保存 channel 的对应UID
        WSChannelExtraDTO wsChannelExtraDTO = ONLINE_WS_MAP.get(channel);
        wsChannelExtraDTO.setUid(user.getId());
        //推送成功消息
        sendMsg(channel, WebsocketAdapter.buildResp(user, token, roleService.hasPower(user.getId(), RoleEnum.CHAT_MANAGER)));
        //用户上线成功状态修改事件
        user.setLastOptTime(new Date());
        user.refreshIp(NettyUtil.getAttr(channel, NettyUtil.IP));
        applicationEventPublisher.publishEvent(new UserOnlineEvent(this, user));
    }

    private void sendMsg(Channel channel, WSBaseResp<?> resp) {
         channel.writeAndFlush(new TextWebSocketFrame(JSONUtil.toJsonStr(resp)));
    }

    private Integer generateLoginCode(Channel channel) {
        Integer code;
        do {
            code = RandomUtil.randomInt(Integer.MAX_VALUE);
        } while (Objects.nonNull(WAIT_LOGIN_MAP.asMap().putIfAbsent(code, channel)));
        return code;
    }

}
