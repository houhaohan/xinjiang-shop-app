package com.pinet.rest.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pinet.core.constants.CommonConstant;
import com.pinet.core.constants.DB;
import com.pinet.core.exception.PinetException;
import com.pinet.core.util.BigDecimalUtil;
import com.pinet.core.util.DateUtils;
import com.pinet.core.util.ThreadLocalUtil;
import com.pinet.keruyun.openapi.dto.CustomerCreateDTO;
import com.pinet.keruyun.openapi.dto.DirectChargeDTO;
import com.pinet.keruyun.openapi.param.CustomerPropertyParam;
import com.pinet.keruyun.openapi.service.IKryApiService;
import com.pinet.keruyun.openapi.type.AuthType;
import com.pinet.keruyun.openapi.vo.customer.CustomerCreateVO;
import com.pinet.keruyun.openapi.vo.customer.CustomerPropertyVO;
import com.pinet.keruyun.openapi.vo.customer.DirectChargeVO;
import com.pinet.rest.entity.*;
import com.pinet.rest.entity.dto.VipRechargeDTO;
import com.pinet.rest.entity.enums.BalanceRecordTypeEnum;
import com.pinet.rest.entity.enums.VipLevelEnum;
import com.pinet.rest.entity.vo.VipUserVO;
import com.pinet.rest.mapper.ShopMapper;
import com.pinet.rest.mapper.VipUserMapper;
import com.pinet.rest.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * VIP用户 服务实现类
 * </p>
 *
 * @author wlbz
 * @since 2024-06-04
 */
@Service
@RequiredArgsConstructor
@Slf4j
@DS(DB.MASTER)
public class VipUserServiceImpl extends ServiceImpl<VipUserMapper, VipUser> implements IVipUserService {
    private final IKryApiService kryApiService;
    private final ShopMapper shopMapper;
    private final IVipShopBalanceService vipShopBalanceService;
    private final IVipRechargeRecordService vipRechargeRecordService;
    private final ICustomerBalanceRecordService customerBalanceRecordService;
    private final IVipLevelService vipLevelService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(Customer customer,Long shopId) {
        VipUser user = new VipUser();
        user.setCustomerId(customer.getCustomerId());
        user.setLevel(VipLevelEnum.VIP1.getLevel());
        user.setVipName(VipLevelEnum.VIP1.getName());
        user.setPhone(customer.getPhone());
        user.setSex(customer.getSex());
        user.setStatus(CommonConstant.ENABLE);
        user.setShopId(shopId);

        Shop shop = shopMapper.selectById(shopId);
        String token = kryApiService.getToken(AuthType.SHOP, shop.getKryShopId());
        CustomerCreateDTO customerCreateDTO = new CustomerCreateDTO();
        customerCreateDTO.setShopId(shop.getKryShopId().toString());
        customerCreateDTO.setMobile(customer.getPhone());
        customerCreateDTO.setName(customer.getNickname());
        if(customer.getSex() == 0){
            customerCreateDTO.setGender(2);
        }else if(customer.getSex() == 1){
            customerCreateDTO.setGender(1);
        }else {
            customerCreateDTO.setGender(0);
        }
        CustomerCreateVO customerCreateVO = kryApiService.createCustomer(shop.getKryShopId(), token, customerCreateDTO);
        if(customerCreateVO == null){
            log.error("手机号{}创建会员失败",customer.getPhone());
            throw new PinetException("创建会员失败");
        }
        user.setKryCustomerId(customerCreateVO.getCustomerId());
        this.save(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void recharge(VipRechargeDTO dto) {
        Shop shop = shopMapper.selectById(dto.getShopId());
        String token = kryApiService.getToken(AuthType.SHOP, shop.getKryShopId());
        Long userId = ThreadLocalUtil.getUserLogin().getUserId();
        VipUser user = this.getByCustomerId(userId);

        DirectChargeDTO directChargeDTO = new DirectChargeDTO();
        directChargeDTO.setShopId(shop.getKryShopId().toString());
        directChargeDTO.setBizDate(DateUtils.getTime());
        directChargeDTO.setOperatorName("管理员");
        directChargeDTO.setRealValue(BigDecimalUtil.yuan2Fen(dto.getAmount()));
        directChargeDTO.setCustomerId(user.getKryCustomerId());
        DirectChargeVO directChargeVO = kryApiService.directCharge(shop.getKryShopId(), token, directChargeDTO);
        if(directChargeVO == null){
            throw new PinetException("充值异常，请联系商家");
        }

        //店铺余额
        VipShopBalance shopBalance = new VipShopBalance();
        shopBalance.setCustomerId(userId);
        shopBalance.setShopId(shop.getId());
        shopBalance.setAmount(BigDecimalUtil.fenToYuan(directChargeVO.getRemainAvailableValue().getTotalValue()));
        vipShopBalanceService.save(shopBalance);

        //充值记录
        VipRechargeRecord rechargeRecord = new VipRechargeRecord();
        rechargeRecord.setCustomerId(userId);
        rechargeRecord.setShopId(shop.getId());
        rechargeRecord.setRealAmount(dto.getAmount());
        BigDecimal giftAmount = BigDecimalUtil.fenToYuan(directChargeVO.getRemainAvailableValue().getGiftValue());
        rechargeRecord.setGiftAmount(giftAmount);
        rechargeRecord.setGiftCouponId(dto.getCouponId());
        vipRechargeRecordService.save(rechargeRecord);

        //资金流水
        CustomerBalanceRecord customerBalanceRecord = new CustomerBalanceRecord();
        customerBalanceRecord.setCustomerId(userId);
        customerBalanceRecord.setType(BalanceRecordTypeEnum._5.getCode());
        customerBalanceRecord.setTypeStr(BalanceRecordTypeEnum._5.getMsg());
        customerBalanceRecord.setMoney(dto.getAmount());
        customerBalanceRecord.setFkId(userId);
        customerBalanceRecordService.save(customerBalanceRecord);
    }

    @Override
    public VipUserVO info(Long customerId) {
        VipUser user = getByCustomerId(customerId);
        VipUserVO vipUserVO = new VipUserVO();
        vipUserVO.setCustomerId(customerId);
        vipUserVO.setLevel(user.getLevel());
        vipUserVO.setVipName(user.getVipName());
        BigDecimal nextLevelDiffAmount = vipLevelService.nextLevelDiffAmount(customerId, user.getLevel());
        vipUserVO.setNextLevelDiffAmount(nextLevelDiffAmount);

        List<VipShopBalance> vipShopBalances = vipShopBalanceService.getByCustomerId(customerId);
        if(CollectionUtils.isEmpty(vipShopBalances)){
            vipUserVO.setAmount(BigDecimal.ZERO);
            return vipUserVO;
        }

        VipShopBalance shopBalance = vipShopBalances.get(0);
        Shop shop = shopMapper.selectById(shopBalance.getShopId());

        String token = kryApiService.getToken(AuthType.SHOP, shop.getKryShopId());
        CustomerPropertyParam param = new CustomerPropertyParam();
        param.setShopId(shop.getKryShopId().toString());
        param.setCustomerId(user.getKryCustomerId());
        CustomerPropertyVO customerPropertyVO = kryApiService.queryCustomerProperty(shop.getKryShopId(), token, param);
        CustomerPropertyVO.RemainAvailable remainAvailable = customerPropertyVO.getPosCardDTOList().get(0).getPosRechargeAccountList().get(0).getRemainAvailableValue();
        vipUserVO.setAmount(BigDecimalUtil.fenToYuan(remainAvailable.getTotalValue()));
        return vipUserVO;
    }

    @Override
    public VipUser getByCustomerId(Long customerId) {
        QueryWrapper<VipUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("customer_id",customerId);
        return getOne(queryWrapper);
    }
}
