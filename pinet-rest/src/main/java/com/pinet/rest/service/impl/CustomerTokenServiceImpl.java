package com.pinet.rest.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.pinet.core.entity.Token;
import com.pinet.core.util.DateUtil;
import com.pinet.rest.entity.CustomerToken;
import com.pinet.rest.mapper.CustomerTokenMapper;
import com.pinet.rest.service.ICustomerTokenService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户登录token 服务实现类
 * </p>
 *
 * @author wlbz
 * @since 2022-12-12
 */
@Service
@DS("slave")
public class CustomerTokenServiceImpl extends ServiceImpl<CustomerTokenMapper, CustomerToken> implements ICustomerTokenService {

    @Override
    public Long validateAndReturnCustomerId(String token, int terminal) {

        CustomerToken customerToken = baseMapper.selectByToken(token);

        if(customerToken == null
                //过期时间加上缓冲时间是否过期
                || customerToken.getGraceTime() < System.currentTimeMillis()
                || customerToken.getIsBlackmail() == 1){
            return null;
        }
        return customerToken.getCustomerId();
    }

    @Override
    public boolean refreshToken(Token token, String oldToken){
        if(token == null || token.getTerminal() == null){
            return false;
        }
        return this.saveToken(token);
    }

    public boolean saveToken(Token token) {
        if(token == null){
            return false;
        }

        CustomerToken customerToken = new CustomerToken();
        BeanUtils.copyProperties(token, customerToken);
        return this.save(customerToken);
    }

}
