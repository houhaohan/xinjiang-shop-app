package com.pinet.rest.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.pinet.rest.entity.CustomerCoupon;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pinet.rest.entity.vo.CustomerCouponListVo;
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

    List<CustomerCouponListVo> selectCustomerCouponList(@Param("pageNo") Integer pageNo, @Param("pageSize") Integer pageSize, @Param("userId") Long userId);

    List<CustomerCouponListVo> selectCustomerCouponDetailList(@Param("pageNo")Integer pageNo,@Param("pageSize") Integer pageSize,@Param("userId") Long userId);

    List<CustomerCouponListVo> selectcustomerCouponInvalidList(@Param("pageNo")Integer pageNo,@Param("pageSize") Integer pageSize,@Param("userId") Long userId);

    List<CustomerCoupon> selectIndexCouponList(@Param("lastId") Long lastId,@Param("userId") Long userId);

}
