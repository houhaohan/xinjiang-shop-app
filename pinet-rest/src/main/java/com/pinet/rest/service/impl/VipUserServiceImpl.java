package com.pinet.rest.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.pinet.common.mq.util.JmsUtil;
import com.pinet.core.constants.CommonConstant;
import com.pinet.core.constants.OrderConstant;
import com.pinet.core.exception.PinetException;
import com.pinet.core.util.*;
import com.pinet.keruyun.openapi.param.CustomerPropertyParam;
import com.pinet.keruyun.openapi.service.IKryApiService;
import com.pinet.keruyun.openapi.vo.customer.CustomerPropertyVO;
import com.pinet.rest.entity.*;
import com.pinet.rest.entity.dto.VipRechargeDTO;
import com.pinet.rest.entity.enums.PayTypeEnum;
import com.pinet.rest.entity.enums.VipLevelEnum;
import com.pinet.rest.entity.param.PayParam;
import com.pinet.rest.entity.vo.VipUserVO;
import com.pinet.rest.mapper.OrdersMapper;
import com.pinet.rest.mapper.ShopMapper;
import com.pinet.rest.mapper.VipUserMapper;
import com.pinet.rest.mq.constants.QueueConstants;
import com.pinet.rest.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
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
@Slf4j
public class VipUserServiceImpl extends ServiceImpl<VipUserMapper, VipUser> implements IVipUserService {
    private final IKryApiService kryApiService;
    private final ShopMapper shopMapper;
    private final IVipShopBalanceService vipShopBalanceService;
    private final IVipRechargeRecordService vipRechargeRecordService;
    private final IVipLevelService vipLevelService;
    private final IOrderPayService orderPayService;
    private final ICustomerService customerService;
    private final OrdersMapper ordersMapper;
    private final JmsUtil jmsUtil;
    private final IPayService payService;
    @Value("${kry.brandId}")
    private Long brandId;
    @Value("${kry.brandToken}")
    private String brandToken;

    public VipUserServiceImpl(IKryApiService kryApiService,
                              ShopMapper shopMapper,
                              IVipShopBalanceService vipShopBalanceService,
                              IVipRechargeRecordService vipRechargeRecordService,
                              IVipLevelService vipLevelService,
                              IOrderPayService orderPayService,
                              ICustomerService customerService,
                              OrdersMapper ordersMapper,
                              JmsUtil jmsUtil,
                              @Qualifier("weixin_mini_service") IPayService payService){
        this.kryApiService = kryApiService;
        this.shopMapper = shopMapper;
        this.vipShopBalanceService = vipShopBalanceService;
        this.vipRechargeRecordService = vipRechargeRecordService;
        this.vipLevelService = vipLevelService;
        this.orderPayService = orderPayService;
        this.customerService = customerService;
        this.ordersMapper = ordersMapper;
        this.jmsUtil = jmsUtil;
        this.payService = payService;
    }

    @Override
    public void create(Customer customer,Long shopId) {
        VipUser user = this.getByCustomerId(customer.getCustomerId());
        if(user == null){
            user = new VipUser();
            user.setCustomerId(customer.getCustomerId());
            user.setLevel(VipLevelEnum.VIP1.getLevel());
            user.setVipName(VipLevelEnum.VIP1.getName());
            user.setPhone(customer.getPhone());
            user.setSex(customer.getSex());
            user.setStatus(CommonConstant.ENABLE);
            user.setNickname(customer.getNickname());
            user.setShopId(shopId);
            this.save(user);
        }else {
            if(StringUtil.isNotBlank(user.getKryCustomerId())){
                return;
            }
        }
        //异步创建客如云会员
        jmsUtil.sendMsgQueue(QueueConstants.KRY_VIP_CREATE, String.valueOf(user.getId()));
    }

    @Override
    @DSTransactional
    public WxPayMpOrderResult recharge(VipRechargeDTO dto) {
        Long userId = ThreadLocalUtil.getUserLogin().getUserId();
        Customer customer = customerService.getById(userId);
        //创建会员
        this.create(customer,dto.getShopId());

        PayParam param = new PayParam();
        param.setOpenId(customer.getQsOpenId());
        param.setPayPrice(dto.getAmount());
        param.setPayDesc("会员充值");
        param.setOrderNo(IdUtil.getSnowflake().nextIdStr());
        param.setPayType(PayTypeEnum.VIP_RECHARGE.getCode());
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
        rechargeRecord.setStatus(CommonConstant.UNPAY);
        vipRechargeRecordService.save(rechargeRecord);
        return (WxPayMpOrderResult)res;
    }

    @Override
    public VipUserVO info(Long customerId) {
        VipUser user = getByCustomerId(customerId);

        VipUserVO vipUserVO = new VipUserVO();
        vipUserVO.setCustomerId(customerId);
        if(user == null){
            vipUserVO.setLevel(VipLevelEnum.VIP1.getLevel());
            vipUserVO.setVipName(VipLevelEnum.VIP1.getName());
            vipUserVO.setNextLevelDiffAmount(new BigDecimal("500"));
        }else {
            vipUserVO.setLevel(user.getLevel());
            vipUserVO.setVipName(user.getVipName());
            BigDecimal nextLevelDiffAmount = vipLevelService.nextLevelDiffAmount(customerId, vipUserVO.getLevel());
            vipUserVO.setNextLevelDiffAmount(nextLevelDiffAmount);
        }
        List<VipShopBalance> vipShopBalanceList = vipShopBalanceService.getByCustomerId(customerId);
        if(CollectionUtils.isEmpty(vipShopBalanceList)){
            vipUserVO.setAmount(BigDecimal.ZERO);
            return vipUserVO;
        }

        VipShopBalance shopBalance = vipShopBalanceList.get(0);
        Shop shop = shopMapper.selectById(shopBalance.getShopId());

        CustomerPropertyParam param = new CustomerPropertyParam();
        param.setShopId(shop.getKryShopId().toString());
        param.setCustomerId(user.getKryCustomerId());
        CustomerPropertyVO customerPropertyVO = kryApiService.queryCustomerProperty(brandId, brandToken, param);
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

    @Override
    public void updateLevel(Long customerId,BigDecimal paidAmount) {
        Integer level = currVipLevel(paidAmount);

        UpdateWrapper<VipUser> wrapper = new UpdateWrapper<>();
        wrapper.eq("customer_id",customerId);
        VipUser entity = new VipUser();
        entity.setLevel(level);
        entity.setVipName(VipLevelEnum.getNameByCode(level));
        this.update(entity,wrapper);
    }

    /**
     * 根据支付金额获取当前VIP等级
     * @param paidAmount
     * @return
     */
    private Integer currVipLevel(BigDecimal paidAmount) {
        //倒序排列
        QueryWrapper<VipLevel> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        List<VipLevel> list = vipLevelService.list();
        for(VipLevel vipLevel : list){
            if(BigDecimalUtil.gt(paidAmount,vipLevel.getMinAmount())){
                return vipLevel.getLevel();
            }
        }
        return VipLevelEnum.VIP1.getLevel();
    }


}
