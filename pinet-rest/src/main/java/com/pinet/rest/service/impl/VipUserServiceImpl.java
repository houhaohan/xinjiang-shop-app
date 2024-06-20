package com.pinet.rest.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.pinet.common.mq.util.JmsUtil;
import com.pinet.common.redis.util.RedisUtil;
import com.pinet.core.constants.CommonConstant;
import com.pinet.core.constants.DB;
import com.pinet.core.constants.OrderConstant;
import com.pinet.core.constants.RedisKeyConstant;
import com.pinet.core.exception.PinetException;
import com.pinet.core.util.*;
import com.pinet.keruyun.openapi.param.CustomerParam;
import com.pinet.keruyun.openapi.service.IKryApiService;
import com.pinet.keruyun.openapi.vo.customer.CustomerQueryVO;
import com.pinet.rest.entity.*;
import com.pinet.rest.entity.dto.VipRechargeDTO;
import com.pinet.rest.entity.enums.PayTypeEnum;
import com.pinet.rest.entity.enums.VipLevelEnum;
import com.pinet.rest.entity.param.PayParam;
import com.pinet.rest.entity.vo.VipUserVO;
import com.pinet.rest.mapper.VipUserMapper;
import com.pinet.rest.mq.constants.QueueConstants;
import com.pinet.rest.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
@DS(DB.MASTER)
public class VipUserServiceImpl extends ServiceImpl<VipUserMapper, VipUser> implements IVipUserService {
    private final IVipRechargeRecordService vipRechargeRecordService;
    private final IVipLevelService vipLevelService;
    private final IOrderPayService orderPayService;
    private final ICustomerService customerService;
    private final JmsUtil jmsUtil;
    private final IPayService payService;
    private final RedisUtil redisUtil;
    private final IKryApiService kryApiService;
    @Value("${kry.brandId}")
    private Long brandId;
    @Value("${kry.brandToken}")
    private String brandToken;

    public VipUserServiceImpl(IVipRechargeRecordService vipRechargeRecordService,
                              IVipLevelService vipLevelService,
                              IOrderPayService orderPayService,
                              ICustomerService customerService,
                              JmsUtil jmsUtil,
                              @Qualifier("weixin_mini_service") IPayService payService,
                              RedisUtil redisUtil,
                              IKryApiService kryApiService){
        this.vipRechargeRecordService = vipRechargeRecordService;
        this.vipLevelService = vipLevelService;
        this.orderPayService = orderPayService;
        this.customerService = customerService;
        this.jmsUtil = jmsUtil;
        this.payService = payService;
        this.redisUtil = redisUtil;
        this.kryApiService = kryApiService;
    }

    @Override
    public void create(Customer customer,Long shopId) {
        VipUser user = getByCustomerId(customer.getCustomerId());
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
        rechargeRecord.setGiftAmount(dto.getGiftAmount());
        rechargeRecord.setOrderNo(param.getOrderNo());
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
        orderPayService.getPaidPriceByCustomerId(customerId);
        if(user == null){
            Customer customer = customerService.getById(customerId);
            CustomerQueryVO customerQueryVO = null;
            //查询是否是客如云的会员,需要控制查询频率,每个用户每2分钟查询一次
            boolean lock = redisUtil.setIfAbsent(RedisKeyConstant.QUERY_KRY_VIP_USER + customerId, "1", 120, TimeUnit.SECONDS);
            if(lock){
                CustomerParam customerParam = new CustomerParam();
                customerParam.setMobile(customer.getPhone());
                customerQueryVO = kryApiService.queryByMobile(brandId, brandToken, customerParam);
            }

            if(customerQueryVO == null){
                vipUserVO.setLevel(VipLevelEnum.VIP1.getLevel());
                vipUserVO.setVipName(VipLevelEnum.VIP1.getName());
                vipUserVO.setNextLevelDiffAmount(VipLevelEnum.VIP2.getMinAmount());
            }else {
                //是客如云的会员
                user = new VipUser();
                user.setCustomerId(customer.getCustomerId());
                user.setLevel(Integer.valueOf(customerQueryVO.getLevelDTO().getLevelNo()));
                user.setVipName(customerQueryVO.getLevelDTO().getLevelName());
                user.setPhone(customerQueryVO.getMobile());
                user.setSex(customer.getSex());
                user.setStatus(CommonConstant.ENABLE);
                user.setNickname(customerQueryVO.getName());
                user.setShopId(24L);//默认店铺ID
                user.setKryCustomerId(customerQueryVO.getCustomerId());
                this.save(user);

                vipUserVO.setLevel(user.getLevel());
                vipUserVO.setVipName(user.getVipName());
                BigDecimal nextLevelDiffAmount = vipLevelService.nextLevelDiffAmount(customerId);
                vipUserVO.setNextLevelDiffAmount(nextLevelDiffAmount);
            }
        }else {
            vipUserVO.setLevel(user.getLevel());
            vipUserVO.setVipName(user.getVipName());
            BigDecimal nextLevelDiffAmount = vipLevelService.nextLevelDiffAmount(customerId);
            vipUserVO.setNextLevelDiffAmount(nextLevelDiffAmount);
        }
        return vipUserVO;
    }

    @Override
    public VipUser getByCustomerId(Long customerId) {
        QueryWrapper<VipUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("customer_id",customerId);
        return getOne(queryWrapper);
    }

    @Override
    public Integer getLevelByCustomerId(Long customerId) {
        VipUser user = this.getByCustomerId(customerId);
        return user == null ? VipLevelEnum.VIP1.getLevel() : user.getLevel();
    }

    @Override
    public boolean updateLevel(Long customerId,BigDecimal paidAmount) {
        //如果目前等级大于level, 那就不用更新了，有些会员等级是在客如云那边直接设置的
        QueryWrapper<VipUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("customer_id",customerId);
        VipUser user = getOne(queryWrapper);

        VipLevelEnum e = VipLevelEnum.getEnumByAmount(paidAmount);
        if(user.getLevel() >= e.getLevel()){
            return false;
        }
        user.setLevel(e.getLevel());
        user.setVipName(e.getName());
        return updateById(user);
    }

}
