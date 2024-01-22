package com.heu.mallchat.common.user.dao;

import com.heu.mallchat.common.user.domain.entity.Black;
import com.heu.mallchat.common.user.mapper.BlackMapper;
import com.heu.mallchat.common.user.service.IBlackService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 黑名单 服务实现类
 * </p>
 *
 * @author <a href="https://github.com/leeleezl">lizzzz</a>
 * @since 2024-01-09
 */
@Service
public class BlackDao extends ServiceImpl<BlackMapper, Black> {

}
