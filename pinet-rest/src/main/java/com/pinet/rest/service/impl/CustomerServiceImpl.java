package com.pinet.rest.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pinet.common.redis.util.RedisUtil;
import com.pinet.core.constants.CommonConstant;
import com.pinet.core.constants.DB;
import com.pinet.core.enums.ErrorCodeEnum;
import com.pinet.core.exception.LoginException;
import com.pinet.core.exception.PinetException;
import com.pinet.core.util.StringUtil;
import com.pinet.core.util.ThreadLocalUtil;
import com.pinet.rest.entity.Customer;
import com.pinet.rest.entity.dto.ForgetPayPasswordDto;
import com.pinet.rest.entity.dto.SetPayPasswordDto;
import com.pinet.rest.service.ICustomerService;
import com.pinet.rest.mapper.CustomerMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Administrator
 * @description 针对表【customer(用户表)】的数据库操作Service实现
 * @createDate 2022-12-06 16:54:47
 */
@Service
@DS(DB.SLAVE)
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements ICustomerService {
    @Resource
    private RedisUtil redisUtil;

    @Override
    public Customer getByQsOpenId(String openId) {
        QueryWrapper<Customer> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("qs_open_id", openId);
        return getOne(queryWrapper);
    }

    @Override
    public Customer getByPhone(String phone) {
        QueryWrapper<Customer> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone", phone);
        return getOne(queryWrapper);
    }

    @Override
    public void setPayPassword(SetPayPasswordDto dto) {
        Long customerId = ThreadLocalUtil.getUserLogin().getUserId();
        Customer customer = getById(customerId);

        customer.setPayPassword(dto.getPayPassword());
        customer.setIsPayPassword(1);
        updateById(customer);
    }

    @Override
    public void updatePayPassword(SetPayPasswordDto dto) {
        Long customerId = ThreadLocalUtil.getUserLogin().getUserId();
        Customer customer = getById(customerId);

        if (!customer.getPayPassword().equals(dto.getOldPayPassword())) {
            throw new PinetException("支付密码错误");
        }
        customer.setPayPassword(dto.getPayPassword());
        customer.setIsPayPassword(1);
        updateById(customer);
    }

    @Override
    public void forgetPayPassword(ForgetPayPasswordDto dto) {
        Long customerId = ThreadLocalUtil.getUserLogin().getUserId();
        Customer customer = getById(customerId);
        if (!dto.getPhone().equals(customer.getPhone())){
            throw new PinetException("手机号错误");
        }
        String code = redisUtil.get(CommonConstant.SMS_CODE_FORGET_PAY_PASSWORD + dto.getPhone());
        if(StringUtil.isEmpty(code)){
            throw new LoginException(ErrorCodeEnum.SMS_EXPIRED);
        }
        if(!code.equals(dto.getCode())){
            throw new LoginException(ErrorCodeEnum.SMS_ERROR);
        }
        customer.setPayPassword(dto.getPayPassword());
        customer.setIsPayPassword(1);
        updateById(customer);
    }

}




