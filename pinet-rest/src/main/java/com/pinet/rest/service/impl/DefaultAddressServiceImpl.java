package com.pinet.rest.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pinet.core.constants.DB;
import com.pinet.rest.entity.DefaultAddress;
import com.pinet.rest.mapper.DefaultAddressMapper;
import com.pinet.rest.service.IDefaultAddressService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 默认地址 服务实现类
 * </p>
 *
 * @author wlbz
 * @since 2022-12-29
 */
@Service
@DS(DB.SLAVE)
public class DefaultAddressServiceImpl extends ServiceImpl<DefaultAddressMapper, DefaultAddress> implements IDefaultAddressService {

    @Override
    public DefaultAddress selectLast() {
        QueryWrapper<DefaultAddress> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_time").last("limit 1");
        return getOne(queryWrapper);
    }
}
