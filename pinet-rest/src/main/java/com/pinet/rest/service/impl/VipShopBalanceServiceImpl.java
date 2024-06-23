package com.pinet.rest.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.pinet.common.redis.util.RedisUtil;
import com.pinet.core.constants.DB;
import com.pinet.core.constants.RedisKeyConstant;
import com.pinet.core.util.BigDecimalUtil;
import com.pinet.core.util.ThreadLocalUtil;
import com.pinet.keruyun.openapi.param.CustomerPropertyParam;
import com.pinet.keruyun.openapi.service.IKryApiService;
import com.pinet.keruyun.openapi.vo.customer.CustomerPropertyVO;
import com.pinet.rest.entity.CustomerBalanceRecord;
import com.pinet.rest.entity.Shop;
import com.pinet.rest.entity.VipShopBalance;
import com.pinet.rest.entity.VipUser;
import com.pinet.rest.entity.vo.BalanceVo;
import com.pinet.rest.mapper.VipShopBalanceMapper;
import com.pinet.rest.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.management.Query;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

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
@RequiredArgsConstructor
public class VipShopBalanceServiceImpl extends ServiceImpl<VipShopBalanceMapper, VipShopBalance> implements IVipShopBalanceService {
    private final ICustomerBalanceRecordService customerBalanceRecordService;
    private final RedisUtil redisUtil;
    private final IShopService shopService;
    private final IVipUserService vipUserService;
    private final IKryApiService kryApiService;
    @Value("${kry.brandId}")
    private Long brandId;
    @Value("${kry.brandToken}")
    private String brandToken;

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

    @Override
    public BalanceVo queryBalance(Long shopId) {
        Long customerId = ThreadLocalUtil.getUserLogin().getUserId();
        BalanceVo vo = new BalanceVo();
        BalanceVo.Amount amount = new BalanceVo.Amount();
        if(shopId == null){
            List<VipShopBalance> vipShopBalanceList = this.getByCustomerId(customerId);
            if(!CollectionUtils.isEmpty(vipShopBalanceList)){
                amount.setAmount(vipShopBalanceList.get(0).getAmount());
            }else {
                amount.setAmount(BigDecimal.ZERO);
            }
        }else {
            BigDecimal balance = BigDecimal.ZERO;
            Shop shop = shopService.getById(shopId);
            VipShopBalance vipShopBalance = this.getByCustomerIdAndShopId(customerId,shopId);
            if(vipShopBalance != null){
                balance = vipShopBalance.getAmount();
            }
            //查询客如云的余额，控制查询频率，30分钟查询一次
            boolean lock = redisUtil.setIfAbsent(RedisKeyConstant.SYNC_KRY_BALANCE + customerId+"_"+shopId, "1", 60 * 60L, TimeUnit.SECONDS);
            if(lock){
                VipUser user = vipUserService.getByCustomerId(customerId);
                if(user != null){
                    CustomerPropertyParam param = new CustomerPropertyParam();
                    param.setCustomerId(user.getKryCustomerId());
                    param.setShopId(shop.getKryShopId().toString());
                    CustomerPropertyVO customerPropertyVO = kryApiService.queryCustomerProperty(brandId, brandToken, param);
                    if(customerPropertyVO != null && !CollectionUtils.isEmpty(customerPropertyVO.getPosCardDTOList())){
                        Integer totalValue = customerPropertyVO.getPosCardDTOList().stream()
                                .map(CustomerPropertyVO.PosCardDTO::getPosRechargeAccountList)
                                .filter(item -> !CollectionUtils.isEmpty(item))
                                .map(item -> item.get(0).getRemainAvailableValue())
                                .map(CustomerPropertyVO.RemainAvailable::getTotalValue)
                                .findFirst()
                                .orElse(0);
                        balance = BigDecimalUtil.fenToYuan(totalValue);
                        if(vipShopBalance == null){
                            vipShopBalance = new VipShopBalance();
                            vipShopBalance.setShopId(shopId);
                            vipShopBalance.setCustomerId(customerId);
                            vipShopBalance.setAmount(balance);
                            this.save(vipShopBalance);
                        }else {
                            vipShopBalance.setAmount(balance);
                            this.updateById(vipShopBalance);
                        }
                    }
                }
            }
            amount.setShopId(shopId);
            amount.setShopName(shop.getShopName());
            amount.setAmount(balance);
        }
        List<CustomerBalanceRecord> customerBalanceRecords = customerBalanceRecordService.getListLimit5(customerId);
        vo.setCustomerBalanceRecords(customerBalanceRecords);
        vo.setBalance(amount);
        return vo;
    }
}
