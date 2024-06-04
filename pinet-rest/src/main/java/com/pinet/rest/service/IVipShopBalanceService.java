package com.pinet.rest.service;

import com.pinet.rest.entity.VipShopBalance;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * VIP店铺余额 服务类
 * </p>
 *
 * @author wlbz
 * @since 2024-06-04
 */
public interface IVipShopBalanceService extends IService<VipShopBalance> {

    /**
     * 查询用户店铺余额
     * @param customerId
     * @return
     */
    List<VipShopBalance> getByCustomerId(Long customerId);
}
