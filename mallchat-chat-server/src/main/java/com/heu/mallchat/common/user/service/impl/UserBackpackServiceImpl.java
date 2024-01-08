package com.heu.mallchat.common.user.service.impl;

import com.heu.mallchat.common.common.annotation.RedissonLock;
import com.heu.mallchat.common.common.enums.YesOrNoEnum;
import com.heu.mallchat.common.common.service.LockService;
import com.heu.mallchat.common.common.utils.AssertUtil;
import com.heu.mallchat.common.user.dao.UserBackpackDao;
import com.heu.mallchat.common.user.domain.entity.UserBackpack;
import com.heu.mallchat.common.user.domain.enums.IdempotentEnum;
import com.heu.mallchat.common.user.service.IUserBackpackService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserBackpackServiceImpl implements IUserBackpackService {

    @Autowired
    private LockService lockService;

    @Autowired
    private UserBackpackDao userBackpackDao;

    @Autowired
    @Lazy
    private UserBackpackServiceImpl userBackpackService;


    @Override
    public void acquireItem(Long uid, Long itemId, IdempotentEnum idempotentEnum, String businessId) {
        String idempotent = getIdempotent(itemId, idempotentEnum, businessId);
        userBackpackService.doAcquire(uid, itemId, idempotent);
    }

    @RedissonLock(key = "#idempotent", waitTime = 5000)
    public void doAcquire(Long uid, Long itemId, String idempotent) {
        UserBackpack userBackPack = userBackpackDao.getByIdempotent(idempotent);
        if(Objects.nonNull(userBackPack)) {
            return;
        }
        //业务检查
        //发放物品
        UserBackpack insert = UserBackpack.builder()
                .uid(uid)
                .itemId(itemId)
                .status(YesOrNoEnum.NO.getStatus())
                .idempotent(idempotent)
                .build();
        userBackpackDao.save(insert);
    }

    //幂等号=itemId+source+businessId
    private String getIdempotent(Long itemId, IdempotentEnum idempotentEnum, String businessId) {
        return String.format("%d_%d_%s", itemId, idempotentEnum.getType(), businessId);
    }
}
