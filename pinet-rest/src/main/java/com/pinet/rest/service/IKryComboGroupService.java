package com.pinet.rest.service;

import com.pinet.rest.entity.KryComboGroup;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 客如云门套餐分组表 服务类
 * </p>
 *
 * @author wlbz
 * @since 2023-08-29
 */
public interface IKryComboGroupService extends IService<KryComboGroup> {
    List<KryComboGroup> getByShopProdId(Long shopProdId);
}
