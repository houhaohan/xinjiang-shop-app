package com.pinet.rest.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer>
    implements ICustomerService {

}




