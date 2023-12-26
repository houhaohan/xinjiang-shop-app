package com.pinet.rest.service;

import com.pinet.rest.entity.BUserBalance;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;

/**
 * <p>
 * 商家资金余额表 服务类
 * </p>
 *
 * @author wlbz
 * @since 2023-07-12
 */
public interface IBUserBalanceService extends IService<BUserBalance> {

    BUserBalance getByShopId(Long shopId);

    /**
     * 根据店铺id增加/扣减余额
     * @param shopId 店铺id
     * @param amount 金额  扣减传负数
     * @return
     */
    boolean addAmount(Long shopId, BigDecimal amount);
}
