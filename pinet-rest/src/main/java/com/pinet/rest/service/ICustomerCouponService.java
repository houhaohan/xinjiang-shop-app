package com.pinet.rest.service;

import com.pinet.core.page.PageRequest;
import com.pinet.rest.entity.CustomerCoupon;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pinet.rest.entity.OrderProduct;
import com.pinet.rest.entity.dto.UpdateCouponStatusDto;
import com.pinet.rest.entity.request.UsableCouponRequest;
import com.pinet.rest.entity.vo.CustomerCouponVo;
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

    List<CustomerCouponVo> customerCouponList(PageRequest pageRequest);

    /**
     * 用户可用优惠券
     * @param request
     * @return
     */
    List<CustomerCouponVo> usableCouponList(UsableCouponRequest request);

    boolean updateCouponStatus(UpdateCouponStatusDto dto);

    List<CustomerCouponVo> customerCouponListDetailList(PageRequest pageRequest);

    List<CustomerCouponVo> customerCouponInvalidList(PageRequest pageRequest);

    List<CustomerCouponVo> indexCouponList();

    /**
     * 判断优惠券是否可用
     * @param customerCouponId 优惠券id
     * @param shopId 店铺id
     * @param orderProducts 订单商品
     * @return 是否可用
     */
    Boolean checkCoupon(Long customerCouponId, Long shopId, List<OrderProduct> orderProducts);

    /**
     * 判断优惠券是否可用
     * @param customerCoupon 优惠券bean
     * @param shopId 店铺id
     * @param orderProducts 订单商品
     * @return 是否可用
     */
    Boolean checkCoupon(CustomerCouponVo customerCoupon, Long shopId, List<OrderProduct> orderProducts);

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
    Long countByCustomerId(Long customerId);

    /**
     * 优惠券领取
     * @param couponId
     * @return CustomerCoupon 领取的优惠券CustomerCoupon数据
     */
    CustomerCoupon receive(Long couponId);

    CustomerCoupon receive(Long customerId, Long couponId);

}
