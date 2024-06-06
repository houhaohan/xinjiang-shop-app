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
     * 更细用户积分
     * @param customerId
     * @param score
     */
    void updateScore(Long customerId,Double score);
}
