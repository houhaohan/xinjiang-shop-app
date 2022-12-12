package com.pinet.rest.mapper;

import com.pinet.rest.entity.CustomerToken;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 用户登录token Mapper 接口
 * </p>
 *
 * @author wlbz
 * @since 2022-12-12
 */
public interface CustomerTokenMapper extends BaseMapper<CustomerToken> {

    CustomerToken selectByToken(String token);

}
