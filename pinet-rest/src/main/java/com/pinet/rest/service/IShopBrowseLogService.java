package com.pinet.rest.service;

import com.pinet.rest.entity.ShopBrowseLog;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wlbz
 * @since 2023-08-16
 */
public interface IShopBrowseLogService extends IService<ShopBrowseLog> {

    void addBrowseLog(Long shopId,Long customerId);
}
