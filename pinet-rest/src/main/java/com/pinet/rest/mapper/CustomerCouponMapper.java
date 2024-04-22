package com.pinet.rest.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pinet.rest.entity.CustomerCoupon;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pinet.rest.entity.vo.CustomerCouponVo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 用户优惠券 Mapper 接口
 * </p>
 *
 * @author wlbz
 * @since 2023-08-14
 */
public interface CustomerCouponMapper extends BaseMapper<CustomerCoupon> {

    List<CustomerCouponVo> selectCustomerCouponList(Page<CustomerCoupon> page, @Param(Constants.WRAPPER) Wrapper<CustomerCoupon> wrapper);

    CustomerCouponVo selectCustomerCouponVoById(@Param("id") Long id);

    Date getFirstCouponReceiveTime(@Param("customerId") Long customerId,@Param("couponId") Long couponId);

}
