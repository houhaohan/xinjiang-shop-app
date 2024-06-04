package com.pinet.rest.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pinet.core.constants.DB;
import com.pinet.rest.entity.VipShopBalance;
import com.pinet.rest.mapper.VipShopBalanceMapper;
import com.pinet.rest.service.IVipShopBalanceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.management.Query;
import java.util.List;

/**
 * <p>
 * VIP店铺余额 服务实现类
 * </p>
 *
 * @author wlbz
 * @since 2024-06-04
 */
@Service
@DS(DB.MASTER)
public class VipShopBalanceServiceImpl extends ServiceImpl<VipShopBalanceMapper, VipShopBalance> implements IVipShopBalanceService {

    @Override
    public List<VipShopBalance> getByCustomerId(Long customerId) {
        QueryWrapper<VipShopBalance> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("customer_id",customerId);
        return list(queryWrapper);
    }
}
