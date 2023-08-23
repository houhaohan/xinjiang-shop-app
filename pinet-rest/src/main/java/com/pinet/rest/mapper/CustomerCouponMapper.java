package com.pinet.rest.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.pinet.rest.entity.CustomerCoupon;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

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

    IPage<CustomerCoupon> selectCustomerCouponList(@Param("page") IPage<CustomerCoupon> page, @Param("userId") Long userId);

    IPage<CustomerCoupon> selectCustomerCouponDetailList(@Param("page")IPage<CustomerCoupon> page,@Param("userId") Long userId);

    IPage<CustomerCoupon> selectcustomerCouponInvalidList(@Param("page")IPage<CustomerCoupon> page,@Param("userId") Long userId);

    List<CustomerCoupon> selectIndexCouponList(@Param("lastId") Long lastId,@Param("userId") Long userId);

    Integer countByCustomerId(@Param("customerId") Long customerId);
}
