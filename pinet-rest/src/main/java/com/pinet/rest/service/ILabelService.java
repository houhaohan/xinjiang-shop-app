package com.pinet.rest.service;

import com.pinet.rest.entity.Label;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wlbz
 * @since 2023-11-27
 */
public interface ILabelService extends IService<Label> {
    String getByShopProdId(Long shopProdId);

}
