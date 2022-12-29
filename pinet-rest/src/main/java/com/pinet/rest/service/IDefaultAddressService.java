package com.pinet.rest.service;

import com.pinet.rest.entity.DefaultAddress;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 默认地址 服务类
 * </p>
 *
 * @author wlbz
 * @since 2022-12-29
 */
public interface IDefaultAddressService extends IService<DefaultAddress> {
    /**
     * 获取默认地址
     * @return
     */
    DefaultAddress selectLast();
}
