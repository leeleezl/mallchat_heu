package com.heu.mallchat.common;

import com.heu.mallchat.common.common.utils.JwtUtils;
import com.heu.mallchat.common.common.utils.RedisUtils;
import com.heu.mallchat.common.user.dao.UserDao;
import com.heu.mallchat.common.user.domain.entity.User;
import com.heu.mallchat.common.user.service.LoginService;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.service.WxService;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import org.apache.tomcat.jni.Time;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class DaoTest {

    @Autowired
    private WxMpService wxMpService;
    @Autowired
    private JwtUtils jwtUtils;

    @Test
    public void test() throws WxErrorException {
        WxMpQrCodeTicket wxMpQrCodeTicket = wxMpService.getQrcodeService().qrCodeCreateTmpTicket(1, 10000);
        String url = wxMpQrCodeTicket.getUrl();
        System.out.println(url);
    }

    @Test
    public void redis() {
//        redisTemplate.opsForValue().set("name","卷心菜");
//        String name = (String) redisTemplate.opsForValue().get("name");
//        System.out.println(name); //卷心菜
        RedisUtils.set("214", "1014");
        System.out.println(RedisUtils.get("214"));
    }

    @Autowired
    private RedissonClient redissonClient;

    @Test
    public void redisson() {
        RLock rLock = redissonClient.getLock("123");
        rLock.lock();
        System.out.println(111);
        rLock.unlock();
    }

    @Autowired
    private LoginService loginService;
    @Test
    public void isValid() {
        System.out.println(loginService.getValidUid("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1aWQiOjIwMDAwLCJjcmVhdGVUaW1lIjoxNzAxMDg2NTAzfQ.wE2AgnOdceCj6UTZ49d3bs5o3UkP0ZAaxkI54kYmrJs"));
    }

}
