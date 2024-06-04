package com.pinet.rest.service;

import com.pinet.rest.entity.VipRechargeRecord;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * VIP充值记录 服务类
 * </p>
 *
 * @author wlbz
 * @since 2024-06-04
 */
public interface IVipRechargeRecordService extends IService<VipRechargeRecord> {

    /**
     * 根据第三方支付单号查询记录
     * @param outTradeNo
     * @return
     */
    VipRechargeRecord getByOutTradeNo(String outTradeNo);
}
