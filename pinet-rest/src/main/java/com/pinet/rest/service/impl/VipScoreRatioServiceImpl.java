package com.pinet.rest.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.pinet.core.constants.DB;
import com.pinet.rest.entity.VipScoreRatio;
import com.pinet.rest.mapper.VipScoreRatioMapper;
import com.pinet.rest.service.IVipScoreRatioService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * VIP积分比例 服务实现类
 * </p>
 *
 * @author wlbz
 * @since 2024-06-04
 */
@Service
@DS(DB.MASTER)
public class VipScoreRatioServiceImpl extends ServiceImpl<VipScoreRatioMapper, VipScoreRatio> implements IVipScoreRatioService {

}
