package com.pinet.rest.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.pinet.core.constants.DB;
import com.pinet.core.util.BigDecimalUtil;
import com.pinet.rest.entity.VipShopBalance;
import com.pinet.rest.mapper.VipShopBalanceMapper;
import com.pinet.rest.service.IVipShopBalanceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.management.Query;
import java.math.BigDecimal;
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

    @Override
    public VipShopBalance getByCustomerIdAndShopId(Long customerId, Long shopId) {
        QueryWrapper<VipShopBalance> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("customer_id",customerId);
        queryWrapper.eq("shop_id",shopId);
        return getOne(queryWrapper);
    }

    @Override
    public void updateAmount(Long customerId, Long shopId, BigDecimal amount) {
        VipShopBalance vipShopBalance = getByCustomerIdAndShopId(customerId, shopId);
        if(vipShopBalance == null){
            vipShopBalance = new VipShopBalance();
            vipShopBalance.setCustomerId(customerId);
            vipShopBalance.setShopId(shopId);
            vipShopBalance.setAmount(amount);
            save(vipShopBalance);
            return;
        }
        vipShopBalance.setAmount(BigDecimalUtil.sum(vipShopBalance.getAmount(),amount));
        updateById(vipShopBalance);
    }
}
