package com.pinet.rest.service;

import com.pinet.core.page.PageRequest;
import com.pinet.rest.entity.CustomerCoupon;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pinet.rest.entity.dto.UpdateCouponStatusDto;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 用户优惠券 服务类
 * </p>
 *
 * @author wlbz
 * @since 2023-08-14
 */
public interface ICustomerCouponService extends IService<CustomerCoupon> {

    List<CustomerCoupon> customerCouponList(PageRequest pageRequest);

    boolean updateCouponStatus(UpdateCouponStatusDto dto);

    List<CustomerCoupon> customerCouponListDetailList(PageRequest pageRequest);

    List<CustomerCoupon> customerCouponInvalidList(PageRequest pageRequest);

    List<CustomerCoupon> indexCouponList();

    /**
     * 判断优惠券是否可用
     * @param customerCouponId 优惠券id
     * @param shopId 店铺id
     * @param orderProdPrice 实付金额
     * @return 是否可用
     */
    Boolean checkCoupon(Long customerCouponId, Long shopId, BigDecimal orderProdPrice);

    /**
     * 判断优惠券是否可用
     * @param customerCoupon 优惠券bean
     * @param shopId 店铺id
     * @param orderProdPrice 实付金额
     * @return 是否可用
     */
    Boolean checkCoupon(CustomerCoupon customerCoupon, Long shopId, BigDecimal orderProdPrice);

    /**
     * 优惠券过期提醒
     * @param customerCouponId 优惠券id
     */
    void couponWarn(Long customerCouponId);

    /**
     * 推送优惠券过期提醒
     */
    void pushCouponExpireMsg(String data1,String data2,String data3,String data4,String data5,String openId);

    void pushCouponExpireMsg(Long customerCouponId);

    /**
     * 发放新人优惠券
     * @param customerId
     */
    void grantNewCustomerCoupon(Long customerId);

    /**
     * 根据用户id统计可用优惠券数量
     * @param customerId
     * @return
     */
    Integer countByCustomerId(Long customerId);
}
