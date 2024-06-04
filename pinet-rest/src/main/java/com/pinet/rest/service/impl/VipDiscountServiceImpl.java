package com.pinet.rest.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.pinet.core.constants.DB;
import com.pinet.rest.entity.VipDiscount;
import com.pinet.rest.mapper.VipDiscountMapper;
import com.pinet.rest.service.IVipDiscountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * VIP等级折扣 服务实现类
 * </p>
 *
 * @author wlbz
 * @since 2024-06-04
 */
@Service
@DS(DB.MASTER)
public class VipDiscountServiceImpl extends ServiceImpl<VipDiscountMapper, VipDiscount> implements IVipDiscountService {

}
