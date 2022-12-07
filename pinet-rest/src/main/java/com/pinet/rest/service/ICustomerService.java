package com.pinet.rest.service;

import com.pinet.rest.entity.Customer;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author Administrator
* @description 针对表【customer(用户表)】的数据库操作Service
* @createDate 2022-12-06 16:54:47
*/
public interface ICustomerService extends IService<Customer> {

    /**
     * 根据openId获取用户
     * @param openId
     * @return
     */
    Customer getByQsOpenId(String openId);

    /**
     *
     * @param phone
     * @return
     */
    Customer getByPhone(String phone);

}
