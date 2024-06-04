package com.pinet.rest.service;

import com.pinet.rest.entity.VipLevel;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;

/**
 * <p>
 * VIP等级表 服务类
 * </p>
 *
 * @author wlbz
 * @since 2024-06-04
 */
public interface IVipLevelService extends IService<VipLevel> {
    /**
     * 距离下个会员等级相差的金额
     * @param customerId
     * @param level
     * @return
     */
    BigDecimal nextLevelDiffAmount(Long customerId,Integer level);

    /**
     * 获取VIP等级
     * @param level
     * @return
     */
    VipLevel getByLevel(Integer level);
}
