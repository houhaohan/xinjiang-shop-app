package com.pinet.rest.service;

import com.pinet.rest.entity.VipEquity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * VIP权益表 服务类
 * </p>
 *
 * @author wlbz
 * @since 2024-06-11
 */
public interface IVipEquityService extends IService<VipEquity> {

    /**
     * 权益列表
     * @return
     */
    List<VipEquity> equityList();
}
