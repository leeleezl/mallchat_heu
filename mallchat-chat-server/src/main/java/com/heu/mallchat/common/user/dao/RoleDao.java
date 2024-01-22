package com.heu.mallchat.common.user.dao;

import com.heu.mallchat.common.user.domain.entity.Role;
import com.heu.mallchat.common.user.mapper.RoleMapper;
import com.heu.mallchat.common.user.service.IRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author <a href="https://github.com/leeleezl">lizzzz</a>
 * @since 2024-01-09
 */
@Service
public class RoleDao extends ServiceImpl<RoleMapper, Role> {

}
