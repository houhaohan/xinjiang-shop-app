package com.pinet.rest.service.impl;

import com.pinet.rest.entity.SystemError;
import com.pinet.rest.mapper.SystemErrorMapper;
import com.pinet.rest.service.ISystemErrorService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wlbz
 * @since 2023-12-12
 */
@Service
public class SystemErrorServiceImpl extends ServiceImpl<SystemErrorMapper, SystemError> implements ISystemErrorService {

    @Override
    public void add(String requestAddress, String errorMsg) {
        SystemError systemError = new SystemError();
        systemError.setRequestAddress(requestAddress);
        systemError.setErrorMsg(errorMsg);
        systemError.setCreateTime(new Date());

        save(systemError);
    }
}
