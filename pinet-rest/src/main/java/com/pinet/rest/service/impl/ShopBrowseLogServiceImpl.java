package com.pinet.rest.service.impl;

import com.pinet.core.util.IPUtils;
import com.pinet.rest.entity.ShopBrowseLog;
import com.pinet.rest.mapper.ShopBrowseLogMapper;
import com.pinet.rest.service.IShopBrowseLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wlbz
 * @since 2023-08-16
 */
@Service
public class ShopBrowseLogServiceImpl extends ServiceImpl<ShopBrowseLogMapper, ShopBrowseLog> implements IShopBrowseLogService {

    @Override
    public void addBrowseLog(Long shopId, Long customerId) {
        ShopBrowseLog shopBrowseLog = new ShopBrowseLog();
        shopBrowseLog.setShopId(shopId);
        shopBrowseLog.setCustomerId(customerId);
        shopBrowseLog.setIp(IPUtils.getIpAddr());
        save(shopBrowseLog);
    }
}
