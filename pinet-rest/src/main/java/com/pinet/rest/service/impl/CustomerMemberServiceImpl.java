package com.pinet.rest.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pinet.core.constants.DB;
import com.pinet.core.exception.PinetException;
import com.pinet.core.util.IPUtils;
import com.pinet.core.util.SpringContextUtils;
import com.pinet.core.util.ThreadLocalUtil;
import com.pinet.rest.entity.Customer;
import com.pinet.rest.entity.CustomerMember;
import com.pinet.rest.entity.OrderPay;
import com.pinet.rest.entity.OrderProduct;
import com.pinet.rest.entity.bo.RecommendTimeBo;
import com.pinet.rest.entity.dto.MemberRecommendProdDto;
import com.pinet.rest.entity.dto.PayDto;
import com.pinet.rest.entity.dto.RecommendListDto;
import com.pinet.rest.entity.enums.BalanceRecordTypeEnum;
import com.pinet.rest.entity.enums.MemberLevelEnum;
import com.pinet.rest.entity.enums.PayTypeEnum;
import com.pinet.rest.entity.param.PayParam;
import com.pinet.rest.entity.vo.*;
import com.pinet.rest.mapper.CustomerMemberMapper;
import com.pinet.rest.mapper.OrdersMapper;
import com.pinet.rest.service.*;
import lombok.extern.slf4j.Slf4j;
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
@DS(DB.MASTER)
@Slf4j
public class CustomerMemberServiceImpl extends ServiceImpl<CustomerMemberMapper, CustomerMember> implements ICustomerMemberService {
    @Resource
    private IOrderPayService orderPayService;

    @Resource
    private OrdersMapper ordersMapper;

    @Resource
    private ICustomerBalanceRecordService customerBalanceRecordService;

    @Resource
    private ICustomerService customerService;

    @Resource
    private IOrderProductService orderProductService;

    @Resource
    private IProductService productService;

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
        param.setPayType(PayTypeEnum._2.getCode());
        IPayService payService = SpringContextUtils.getBean(dto.getChannelId() + "_" + "service", IPayService.class);
        Object res = payService.pay(param);

        //构造orderPay
        OrderPay orderPay = new OrderPay();
        orderPay.setOrderId(0L);
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
        return baseMapper.selectByCustomerId(customerId);
    }

    @Override
    public MemberVo member() {
        Long customerId = ThreadLocalUtil.getUserLogin().getUserId();
        MemberVo memberVo = ordersMapper.countMember(customerId);
        CustomerMember customerMember = getByCustomerId(customerId);

        //统计累计充值金额
        BigDecimal sumRechargePrice;
        log.info("会员信息"+JSONObject.toJSONString(customerMember));
        if (ObjectUtil.isNotNull(customerMember) && ObjectUtil.isNotNull(customerMember.getExpireTime()) &&  customerMember.getExpireTime().getTime() > System.currentTimeMillis()){
            memberVo.setExpireTime(customerMember.getExpireTime());
            sumRechargePrice = customerBalanceRecordService.sumMoneyByCustomerIdAndType(customerId, BalanceRecordTypeEnum._5,customerMember.getExpireTime());
        }else {
            sumRechargePrice = customerBalanceRecordService.sumMoneyByCustomerIdAndType(customerId, BalanceRecordTypeEnum._5);
        }
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
        Page<RecommendListVo> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        Long customerId = ThreadLocalUtil.getUserLogin().getUserId();
        dto.setCustomerId(customerId);
        Page<RecommendListVo> recommendListVos = ordersMapper.selectRecommendList(page, dto);
        recommendListVos.getRecords().forEach(k -> {
            k.getRecommendTimeBos().forEach(k1 -> {
                List<OrderProduct> orderProducts = orderProductService.getByOrderId(k1.getOrderId());
                k1.setOrderProducts(orderProducts);
            });
        });
        return recommendListVos.getRecords();
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

    @Override
    public List<MemberRecommendProdVo> memberRecommendProd(MemberRecommendProdDto dto) {
        Page<OrderListVo> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        IPage<MemberRecommendProdVo> memberRecommendProd = baseMapper.selectMemberRecommendProd(page,dto);
        return memberRecommendProd.getRecords();
    }

    @Override
    public List<RecommendTimeBo> recommendIndexList() {
        Long customerId = ThreadLocalUtil.getUserLogin().getUserId();
        List<RecommendTimeBo> recommendListVos = ordersMapper.selectRecommendIndexList(customerId);
        recommendListVos.forEach(k -> {
            List<OrderProduct> orderProducts = orderProductService.getByOrderId(k.getOrderId());
            k.setOrderProducts(orderProducts);
        });
        return recommendListVos;
    }

    @Override
    public List<ProductListVo> productList(Long shopId) {
        List<ProductListVo> productListVos = productService.productList(shopId);
        return productListVos;
    }
}
