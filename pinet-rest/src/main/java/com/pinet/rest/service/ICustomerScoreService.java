package com.pinet.rest.service;

import com.pinet.rest.entity.CustomerScore;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户积分表 服务类
 * </p>
 *
 * @author wlbz
 * @since 2024-06-06
 */
public interface ICustomerScoreService extends IService<CustomerScore> {


    /**
     * 添加积分
     * @param customerId
     * @param score
     */
    void addScore(Long customerId,Double score);

    /**
     * 减少积分
     * @param customerId
     * @param score
     */
    void subScore(Long customerId,Double score);

    /**
     * 查询用户积分
     * @param customerId
     * @return
     */
    Double getScoreByCustomerId(Long customerId);

    /**
     * 查询用户积分对象
     * @param customerId
     * @return
     */
    CustomerScore getByCustomerId(Long customerId);
}
