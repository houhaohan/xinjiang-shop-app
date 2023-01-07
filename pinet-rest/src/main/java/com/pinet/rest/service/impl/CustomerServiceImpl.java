package com.pinet.rest.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pinet.core.constants.DB;
import com.pinet.rest.entity.Customer;
import com.pinet.rest.service.ICustomerService;
import com.pinet.rest.mapper.CustomerMapper;
import org.springframework.stereotype.Service;

/**
* @author Administrator
* @description 针对表【customer(用户表)】的数据库操作Service实现
* @createDate 2022-12-06 16:54:47
*/
@Service
@DS(DB.SLAVE)
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements ICustomerService {

    @Override
    public Customer getByQsOpenId(String openId) {
        QueryWrapper<Customer> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("qs_open_id",openId);
        return getOne(queryWrapper);
    }

    @Override
    public Customer getByPhone(String phone) {
        QueryWrapper<Customer> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone",phone);
        return getOne(queryWrapper);
    }

}




