package com.pinet.rest.service;

import com.pinet.rest.entity.SystemError;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wlbz
 * @since 2023-12-12
 */
public interface ISystemErrorService extends IService<SystemError> {
    /**
     * 添加系统异常
     * @param requestAddress 异常接口
     * @param errorMsg 异常信息
     */
    void add(String requestAddress,String errorMsg);
}
