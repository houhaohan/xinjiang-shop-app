package com.pinet.rest.service;

import com.pinet.rest.entity.CustomerAddress;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pinet.rest.entity.dto.CustomerAddressDto;

/**
* @author Administrator
* @description 针对表【customer_address(地址管理表)】的数据库操作Service
* @createDate 2022-12-06 16:58:34
*/
public interface ICustomerAddressService extends IService<CustomerAddress> {

    /**
     * 新增收货地址
     * @param customerAddressDto
     */
    boolean add(CustomerAddressDto customerAddressDto);

    /**
     * 修改收货地址
     * @param customerAddressDto
     */
    boolean edit(CustomerAddressDto customerAddressDto);
}
