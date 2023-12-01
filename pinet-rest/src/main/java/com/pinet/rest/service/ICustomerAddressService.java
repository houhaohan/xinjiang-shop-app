package com.pinet.rest.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.pinet.core.constants.DB;
import com.pinet.rest.entity.CustomerAddress;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pinet.rest.entity.dto.CustomerAddressDto;

import java.util.List;

/**
* @author Administrator
* @description 针对表【customer_address(地址管理表)】的数据库操作Service
* @createDate 2022-12-06 16:58:34
*/
@DS(DB.SLAVE)
public interface ICustomerAddressService extends IService<CustomerAddress> {

    /**
     * 新增收货地址
     * @param customerAddressDto
     * @param userId
     */
    boolean add(CustomerAddressDto customerAddressDto,Long userId);

    /**
     * 修改收货地址
     * @param customerAddressDto
     * @param userId
     */
    boolean edit(CustomerAddressDto customerAddressDto,Long userId);


    /**
     * 设置默认地址
     * @param id
     * @return
     */
    boolean updateDefaultAddress(Long id,Long userId);

    /**
     * 获取默认地址
     * @return
     */
    CustomerAddress getDefaultAddress(Long customerId);

    /**
     * 根据用户查询地址
     * @param customerId 用户id
     * @return
     */
    List<CustomerAddress> getByCustomerId(Long customerId);
}
