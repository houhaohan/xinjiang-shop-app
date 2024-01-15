package com.pinet.rest.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.pinet.rest.entity.CustomerCoupon;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pinet.rest.entity.vo.CustomerCouponVo;
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

    List<CustomerCouponVo> selectCustomerCouponList(@Param(Constants.WRAPPER) Wrapper<CustomerCoupon> wrapper);

}
