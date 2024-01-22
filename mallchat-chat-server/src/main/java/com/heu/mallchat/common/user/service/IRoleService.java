package com.heu.mallchat.common.user.service;

import com.heu.mallchat.common.user.domain.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;
import com.heu.mallchat.common.user.domain.enums.RoleEnum;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @author <a href="https://github.com/leeleezl">lizzzz</a>
 * @since 2024-01-09
 */
public interface IRoleService {

    /**
     * 是否拥有某个权限 临时写法
     */
    boolean hasPower(Long uid, RoleEnum roleEnum);
}
