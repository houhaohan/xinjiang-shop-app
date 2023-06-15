package com.pinet.rest.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pinet.core.entity.BaseEntity;
import com.pinet.core.exception.PinetException;
import com.pinet.core.util.IPUtils;
import com.pinet.core.util.SpringContextUtils;
import com.pinet.core.util.ThreadLocalUtil;
import com.pinet.rest.entity.Customer;
import com.pinet.rest.entity.CustomerMember;
import com.pinet.rest.entity.OrderPay;
import com.pinet.rest.entity.dto.PayDto;
import com.pinet.rest.entity.dto.RecommendListDto;
import com.pinet.rest.entity.enums.BalanceRecordTypeEnum;
import com.pinet.rest.entity.enums.MemberLevelEnum;
import com.pinet.rest.entity.param.PayParam;
import com.pinet.rest.entity.vo.MemberVo;
import com.pinet.rest.entity.vo.RecommendListVo;
import com.pinet.rest.mapper.CustomerMemberMapper;
import com.pinet.rest.service.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wlbz
 * @since 2023-06-13
 */
@Service
public class CustomerMemberServiceImpl extends ServiceImpl<CustomerMemberMapper, CustomerMember> implements ICustomerMemberService {
    @Resource
    private IOrderPayService orderPayService;

    @Resource
    private IOrdersService ordersService;

    @Resource
    private ICustomerBalanceRecordService customerBalanceRecordService;

    @Resource
    private ICustomerService customerService;

    @Override
    public Object recharge(PayDto dto) {
        if (dto.getOrderPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new PinetException("充值金额不能为0");
        }
        Long userId = ThreadLocalUtil.getUserLogin().getUserId();

        PayParam param = new PayParam();
        param.setOpenId(dto.getOpenId());
        param.setPayPrice(dto.getOrderPrice());
        param.setPayDesc("店帮主充值");
        param.setOrderNo(IdUtil.getSnowflake().nextIdStr());

        IPayService payService = SpringContextUtils.getBean(dto.getChannelId() + "_" + "service", IPayService.class);
        Object res = payService.pay(param);

        //构造orderPay
        OrderPay orderPay = new OrderPay();
        orderPay.setPayType(2);
        orderPay.setOrderNo(Long.valueOf(param.getOrderNo()));
        orderPay.setCustomerId(userId);
        orderPay.setPayStatus(1);
        orderPay.setOrderPrice(dto.getOrderPrice());
        orderPay.setPayPrice(dto.getOrderPrice());
        orderPay.setOpenId(dto.getOpenId());
        orderPay.setChannelId(dto.getChannelId());
        orderPay.setPayName(payService.getPayName());
        orderPay.setIp(IPUtils.getIpAddr());
        orderPayService.save(orderPay);

        return res;
    }

    @Override
    public CustomerMember getByCustomerId(Long customerId) {
        LambdaQueryWrapper<CustomerMember> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(CustomerMember::getCustomerId, customerId).eq(BaseEntity::getDelFlag, 0);
        return getOne(lambdaQueryWrapper);
    }

    @Override
    public MemberVo member() {
        Long customerId = ThreadLocalUtil.getUserLogin().getUserId();
        MemberVo memberVo = ordersService.countMember(customerId);

        //统计累计充值金额
        BigDecimal sumRechargePrice = customerBalanceRecordService.sumMoneyByCustomerIdAndType(customerId, BalanceRecordTypeEnum._5);
        memberVo.setSumRechargePrice(sumRechargePrice);


        int memberLevel = getMemberLevel(customerId);


        memberVo.setMemberLevel(memberLevel);
        memberVo.setMemberLevelStr(MemberLevelEnum.getMsgByCode(memberLevel));


        Customer customer = customerService.getById(customerId);
        memberVo.setAvatar(customer.getAvatar());
        memberVo.setNickName(customer.getNickname());


        return memberVo;
    }

    @Override
    public List<RecommendListVo> recommendList(RecommendListDto dto) {
        Long customerId = ThreadLocalUtil.getUserLogin().getUserId();
        dto.setCustomerId(customerId);
        List<RecommendListVo> recommendListVos = ordersService.recommendList(dto);

        return recommendListVos;
    }

    @Override
    public Integer getMemberLevel(Long customerId) {
        int memberLevel = 0;
        CustomerMember customerMember = getByCustomerId(customerId);
        if (ObjectUtil.isNotNull(customerMember)) {
            memberLevel = customerMember.getMemberLevel();
        }
        return memberLevel;
    }
}
