package com.pinet.rest.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.pinet.core.constants.DB;
import com.pinet.rest.entity.Agreement;
import com.pinet.rest.mapper.AgreementMapper;
import com.pinet.rest.service.IAgreementService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 协议表 服务实现类
 * </p>
 *
 * @author wlbz
 * @since 2023-09-04
 */
@Service
@DS(DB.SLAVE)
public class AgreementServiceImpl extends ServiceImpl<AgreementMapper, Agreement> implements IAgreementService {

}
