package com.pinet.rest.service;

import com.pinet.rest.entity.SysConfig;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wlbz
 * @since 2024-06-19
 */
public interface ISysConfigService extends IService<SysConfig> {

    SysConfig getByCode(String code);
}
