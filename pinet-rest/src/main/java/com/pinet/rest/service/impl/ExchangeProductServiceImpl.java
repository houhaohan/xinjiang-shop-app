package com.pinet.rest.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pinet.common.redis.util.RedisUtil;
import com.pinet.core.constants.DB;
import com.pinet.core.exception.PinetException;
import com.pinet.core.util.ThreadLocalUtil;
import com.pinet.rest.entity.Coupon;
import com.pinet.rest.entity.CustomerCoupon;
import com.pinet.rest.entity.ExchangeProduct;
import com.pinet.rest.entity.dto.ExchangeDto;
import com.pinet.rest.entity.dto.ExchangeProductListDto;
import com.pinet.rest.entity.enums.ScoreRecordTypeEnum;
import com.pinet.rest.mapper.ExchangeProductMapper;
import com.pinet.rest.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 兑换商品表 服务实现类
 * </p>
 *
 * @author wlbz
 * @since 2024-01-16
 */
@Service
@DS(DB.MASTER)
public class ExchangeProductServiceImpl extends ServiceImpl<ExchangeProductMapper, ExchangeProduct> implements IExchangeProductService {
    @Resource
    private ICouponService couponService;

    @Resource
    private ICustomerBalanceService customerBalanceService;

    @Resource
    private ICustomerCouponService customerCouponService;

    @Resource
    private IExchangeRecordService exchangeRecordService;

    @Resource
    private IScoreRecordService scoreRecordService;

    @Resource
    private RedisUtil redisUtil;

    @Override
    public List<ExchangeProduct> exchangeProductList(ExchangeProductListDto dto) {
        Page<ExchangeProduct> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        return baseMapper.selectExchangeProductList(page, dto);
    }

    @Override
    public ExchangeProduct exchangeProductDetail(Long id) {
        ExchangeProduct exchangeProduct = getById(id);
        if (ObjectUtil.isNull(exchangeProduct)) {
            throw new PinetException("兑换商品不存在");
        }
        if (exchangeProduct.getProdType() == 2) {
            Coupon coupon = couponService.getById(exchangeProduct.getCouponId());
            //判断优惠券类型
            if (coupon.getEffectType() == 1) {
                exchangeProduct.setExpireTimeStr("自领取后" + coupon.getEffectDay() + "天后失效");
            } else {
                exchangeProduct.setExpireTimeStr(DateUtil.formatDateTime(coupon.getPastTime()) + "后失效");
            }
        }
        return exchangeProduct;
    }

    @Override
    @DSTransactional
    public void exchange(ExchangeDto dto) {
        Long customerId = ThreadLocalUtil.getUserLogin().getUserId();
        //查询兑换商品
        ExchangeProduct exchangeProduct = getById(dto.getExchangeProductId());
        if (ObjectUtil.isNull(exchangeProduct)) {
            throw new PinetException("兑换商品不存在");
        }
        //扣减积分
        customerBalanceService.subtractAvailableBalance(customerId, exchangeProduct.getScore());

        //判断是否是优惠券
        if (exchangeProduct.getProdType() == 2) {
            //发放优惠券
            CustomerCoupon customerCoupon = customerCouponService.receive(exchangeProduct.getCouponId());

            //处理下首页优惠券弹窗
            String redisKey = "qingshi:coupon:index:" + customerId;
            redisUtil.set(redisKey, customerCoupon.getId().toString());
        }

        //添加兑换记录
        Long exchangeRecordId = exchangeRecordService.addExchangeRecord(customerId, exchangeProduct.getId(), exchangeProduct.getScore(),
                exchangeProduct.getProdType(), exchangeProduct.getProdName(), 1,
                exchangeProduct.getShopId(), exchangeProduct.getShopName());

        //添加积分记录
        scoreRecordService.addScoreRecord(exchangeProduct.getShopId(), "兑换" + exchangeProduct.getProdName(),
                exchangeProduct.getScore(), exchangeRecordId, ScoreRecordTypeEnum._3);
    }
}
