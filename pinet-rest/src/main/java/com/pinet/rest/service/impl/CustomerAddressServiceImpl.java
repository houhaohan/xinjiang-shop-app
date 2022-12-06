package com.pinet.rest.service.impl;

import com.pinet.rest.entity.CustomerAddress;
import com.pinet.rest.mapper.CustomerAddressMapper;
import com.pinet.rest.service.ICustomerAddressService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 地址管理表 服务实现类
 * </p>
 *
 * @author wlbz
 * @since 2022-12-06
 */
@Service
public class CustomerAddressServiceImpl extends ServiceImpl<CustomerAddressMapper, CustomerAddress> implements ICustomerAddressService {

}
