package com.pinet.rest.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.pinet.core.constants.DB;
import com.pinet.rest.entity.Address;
import com.pinet.rest.entity.vo.AddressIdVo;
import com.pinet.rest.mapper.AddressMapper;
import com.pinet.rest.service.IAddressService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * <p>
 * 地址表 服务实现类
 * </p>
 *
 * @author wlbz
 * @since 2022-12-09
 */
@Service
@DS(DB.SLAVE)
public class AddressServiceImpl extends ServiceImpl<AddressMapper, Address> implements IAddressService {


    @Override
    public AddressIdVo selectIdByName(String province, String city, String district) {
        return baseMapper.selectIdByName(province,city,district);
    }
}
