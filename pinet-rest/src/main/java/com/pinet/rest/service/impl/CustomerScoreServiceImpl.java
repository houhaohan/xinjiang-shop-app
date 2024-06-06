package com.pinet.rest.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.pinet.rest.entity.CustomerScore;
import com.pinet.rest.mapper.CustomerScoreMapper;
import com.pinet.rest.service.ICustomerScoreService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户积分表 服务实现类
 * </p>
 *
 * @author wlbz
 * @since 2024-06-06
 */
@Service
public class CustomerScoreServiceImpl extends ServiceImpl<CustomerScoreMapper, CustomerScore> implements ICustomerScoreService {

    @Override
    public void updateScore(Long customerId, Double score) {
        if(score == 0){
            return;
        }
        UpdateWrapper<CustomerScore> wrapper = new UpdateWrapper<>();
        wrapper.eq("customer_id",customerId);
        if(score > 0){
            wrapper.setSql("score = score + " + score);
        }else {
            wrapper.setSql("score = score - " + score);
        }
        update(wrapper);
    }
}
