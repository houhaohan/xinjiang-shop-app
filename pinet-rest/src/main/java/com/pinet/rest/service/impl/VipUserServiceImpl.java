package com.pinet.rest.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pinet.core.constants.CommonConstant;
import com.pinet.core.constants.DB;
import com.pinet.core.constants.OrderConstant;
import com.pinet.core.exception.PinetException;
import com.pinet.core.util.*;
import com.pinet.keruyun.openapi.dto.CustomerCreateDTO;
import com.pinet.keruyun.openapi.dto.DirectChargeDTO;
import com.pinet.keruyun.openapi.param.CustomerPropertyParam;
import com.pinet.keruyun.openapi.service.IKryApiService;
import com.pinet.keruyun.openapi.type.AuthType;
import com.pinet.keruyun.openapi.vo.customer.CustomerCreateVO;
import com.pinet.keruyun.openapi.vo.customer.CustomerPropertyVO;
import com.pinet.keruyun.openapi.vo.customer.DirectChargeVO;
import com.pinet.rest.entity.*;
import com.pinet.rest.entity.dto.PayDto;
import com.pinet.rest.entity.dto.VipRechargeDTO;
import com.pinet.rest.entity.enums.PayTypeEnum;
import com.pinet.rest.entity.enums.VipLevelEnum;
import com.pinet.rest.entity.param.PayParam;
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
    private final IVipLevelService vipLevelService;
    private final IOrderPayService orderPayService;
    private final ICustomerService customerService;

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
        Long userId = ThreadLocalUtil.getUserLogin().getUserId();
        Customer customer = customerService.getById(userId);
        PayParam param = new PayParam();
        param.setOpenId(customer.getQsOpenId());
        param.setPayPrice(dto.getAmount());
        param.setPayDesc("会员充值");
        param.setOrderNo(IdUtil.getSnowflake().nextIdStr());
        param.setPayType(PayTypeEnum.VIP_RECHARGE.getCode());
        IPayService payService = SpringContextUtils.getBean("weixin_mini_service", IPayService.class);
        Object res = payService.pay(param);
        if(res == null){
            //失败
            throw new PinetException("支付失败");
        }

        //构造orderPay
        OrderPay orderPay = new OrderPay();
        orderPay.setOrderId(0L);
        orderPay.setPayType(PayTypeEnum.VIP_RECHARGE.getCode());
        orderPay.setOrderNo(Long.valueOf(param.getOrderNo()));
        orderPay.setCustomerId(userId);
        orderPay.setPayStatus(OrderConstant.UNPAID);
        orderPay.setOrderPrice(dto.getAmount());
        orderPay.setPayPrice(dto.getAmount());
        orderPay.setOpenId(customer.getQsOpenId());
        orderPay.setChannelId("weixin_mini_service");
        orderPay.setPayName(payService.getPayName());
        orderPay.setIp(IPUtils.getIpAddr());
        orderPayService.save(orderPay);

        //VIP充值记录
        VipRechargeRecord rechargeRecord = new VipRechargeRecord();
        rechargeRecord.setCustomerId(userId);
        rechargeRecord.setShopId(dto.getShopId());
        rechargeRecord.setRealAmount(dto.getAmount());
        rechargeRecord.setOutTradeNo(param.getOrderNo());
        rechargeRecord.setGiftCouponId(dto.getCouponId());
        rechargeRecord.setTemplateId(dto.getTemplateId());
        vipRechargeRecordService.save(rechargeRecord);
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

        List<VipShopBalance> vipShopBalanceList = vipShopBalanceService.getByCustomerId(customerId);
        if(CollectionUtils.isEmpty(vipShopBalanceList)){
            vipUserVO.setAmount(BigDecimal.ZERO);
            return vipUserVO;
        }

        VipShopBalance shopBalance = vipShopBalanceList.get(0);
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
