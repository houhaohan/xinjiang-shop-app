package com.pinet.rest.service;

import com.pinet.rest.entity.VipShopBalance;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pinet.rest.entity.vo.BalanceVo;

import java.math.BigDecimal;
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

    /**
     * 查询用户店铺余额
     * @param customerId
     * @param shopId
     * @return
     */
    VipShopBalance getByCustomerIdAndShopId(Long customerId,Long shopId);

    /**
     * 更新余额
     * @param customerId
     * @param shopId
     * @param amount
     */
    void updateAmount(Long customerId, Long shopId, BigDecimal amount);

    /**
     * 查询用户余额
     * @param shopId
     * @return
     */
    BalanceVo queryBalance(Long shopId);
}
