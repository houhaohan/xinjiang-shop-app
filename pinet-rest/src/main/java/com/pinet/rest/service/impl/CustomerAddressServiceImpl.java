package com.pinet.rest.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pinet.rest.entity.CustomerAddress;
import com.pinet.rest.service.ICustomerAddressService;
import com.pinet.rest.mapper.CustomerAddressMapper;
import org.springframework.stereotype.Service;

/**
* @author Administrator
* @description 针对表【customer_address(地址管理表)】的数据库操作Service实现
* @createDate 2022-12-06 16:58:34
*/
@Service
public class CustomerAddressServiceImpl extends ServiceImpl<CustomerAddressMapper, CustomerAddress>
    implements ICustomerAddressService {

}




