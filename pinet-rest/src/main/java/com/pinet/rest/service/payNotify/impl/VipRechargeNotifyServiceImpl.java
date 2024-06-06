package com.pinet.rest.service.payNotify.impl;

import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.pinet.core.constants.CommonConstant;
import com.pinet.core.constants.OrderConstant;
import com.pinet.core.util.BigDecimalUtil;
import com.pinet.core.util.DateUtils;
import com.pinet.keruyun.openapi.dto.DirectChargeDTO;
import com.pinet.keruyun.openapi.service.IKryApiService;
import com.pinet.keruyun.openapi.type.AuthType;
import com.pinet.keruyun.openapi.vo.customer.DirectChargeVO;
import com.pinet.rest.entity.*;
import com.pinet.rest.entity.enums.BalanceRecordTypeEnum;
import com.pinet.rest.entity.param.OrderPayNotifyParam;
import com.pinet.rest.mapper.ShopMapper;
import com.pinet.rest.service.*;
import com.pinet.rest.service.payNotify.IPayNotifyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * VIP充值回调
 */
@Service("vip_recharge_notify_service")
@Slf4j
public class VipRechargeNotifyServiceImpl implements IPayNotifyService {
    @Autowired
    private IOrderPayService orderPayService;
    @Autowired
    private IVipRechargeRecordService vipRechargeRecordService;
    @Autowired
    private IVipUserService vipUserService;
    @Autowired
    private IVipShopBalanceService vipShopBalanceService;
    @Autowired
    private ICustomerBalanceRecordService customerBalanceRecordService;
    @Autowired
    private IKryApiService kryApiService;
    @Autowired
    private ShopMapper shopMapper;

    @Override
    @DSTransactional
    public boolean payNotify(OrderPayNotifyParam param) {
        OrderPay orderPay = orderPayService.getByOrderNo(param.getOrderNo());
        if (orderPay == null || Objects.equals(orderPay.getPayStatus(),OrderConstant.PAID)) {
            return false;
        }
        orderPay.setPayStatus(OrderConstant.PAID);
        orderPay.setPayTime(param.getPayTime());
        orderPay.setOutTradeNo(param.getOutTradeNo());
        orderPayService.updateById(orderPay);

        VipRechargeRecord vipRechargeRecord = vipRechargeRecordService.getByOutTradeNo(param.getOutTradeNo());
        if(Objects.equals(CommonConstant.SUCCESS,vipRechargeRecord.getStatus())){
            return false;
        }
        VipUser user = vipUserService.getByCustomerId(orderPay.getCustomerId());

        Shop shop = shopMapper.selectById(vipRechargeRecord.getShopId());
        String token = kryApiService.getToken(AuthType.SHOP, shop.getKryShopId());
        DirectChargeDTO directChargeDTO = new DirectChargeDTO();
        directChargeDTO.setShopId(shop.getKryShopId().toString());
        directChargeDTO.setBizDate(DateUtils.getTime());
        directChargeDTO.setOperatorName("管理员");
        directChargeDTO.setRealValue(BigDecimalUtil.yuan2Fen(vipRechargeRecord.getRealAmount()));
        directChargeDTO.setCustomerId(user.getKryCustomerId());
        DirectChargeVO directChargeVO = kryApiService.directCharge(shop.getKryShopId(), token, directChargeDTO);
        if(directChargeVO == null){
            log.error("VIP充值异常");
            return false;
        }

        //VIP店铺充值余额
        VipShopBalance shopBalance = new VipShopBalance();
        shopBalance.setCustomerId(orderPay.getCustomerId());
        shopBalance.setShopId(shop.getId());
        shopBalance.setAmount(BigDecimalUtil.fenToYuan(directChargeVO.getRemainAvailableValue().getTotalValue()));
        vipShopBalanceService.save(shopBalance);

        BigDecimal giftAmount = BigDecimalUtil.fenToYuan(directChargeVO.getRemainAvailableValue().getGiftValue());
        vipRechargeRecord.setGiftAmount(giftAmount);
        vipRechargeRecord.setStatus(CommonConstant.SUCCESS);
        vipRechargeRecordService.updateById(vipRechargeRecord);

        //资金流水
        CustomerBalanceRecord customerBalanceRecord = new CustomerBalanceRecord();
        customerBalanceRecord.setCustomerId(user.getCustomerId());
        customerBalanceRecord.setType(BalanceRecordTypeEnum._5.getCode());
        customerBalanceRecord.setTypeStr(BalanceRecordTypeEnum._5.getMsg());
        customerBalanceRecord.setMoney(BigDecimalUtil.sum(vipRechargeRecord.getRealAmount(),vipRechargeRecord.getGiftAmount()));
        customerBalanceRecord.setFkId(user.getCustomerId());
        return customerBalanceRecordService.save(customerBalanceRecord);
    }

}
