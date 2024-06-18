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
    public void addScore(Long customerId, Double score) {
        if(score == 0){
            return;
        }
        QueryWrapper<CustomerScore> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("customer_id",customerId);
        CustomerScore customerScore = getOne(queryWrapper);
        if(customerScore != null){
            customerScore.setScore(customerScore.getScore() +score );
            updateById(customerScore);
            return;
        }
        customerScore = new CustomerScore();
        customerScore.setCustomerId(customerId);
        customerScore.setScore(score);
        save(customerScore);
    }

    @Override
    public void subScore(Long customerId, Double score) {
        if(score == 0){
            return;
        }
        UpdateWrapper<CustomerScore> wrapper = new UpdateWrapper<>();
        wrapper.eq("customer_id",customerId);
        wrapper.setSql("score = score - " + score);
        update(wrapper);
    }

    @Override
    public Double getScoreByCustomerId(Long customerId) {
        QueryWrapper<CustomerScore> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("score");
        queryWrapper.eq("customer_id",customerId);
        Double score = getObj(queryWrapper, o -> Double.valueOf(o.toString()));
        return score == null ? 0D : score;
    }

    @Override
    public CustomerScore getByCustomerId(Long customerId) {
        QueryWrapper<CustomerScore> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("customer_id",customerId);
        return getOne(queryWrapper);
    }

}
