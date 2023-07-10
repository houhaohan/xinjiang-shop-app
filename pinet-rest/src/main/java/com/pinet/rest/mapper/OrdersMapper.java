package com.pinet.rest.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pinet.rest.entity.CustomerMember;
import com.pinet.rest.entity.Orders;
import com.pinet.rest.entity.bo.RecommendTimeBo;
import com.pinet.rest.entity.dto.RecommendListDto;
import com.pinet.rest.entity.vo.*;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 订单表 Mapper 接口
 * </p>
 *
 * @author wlbz
 * @since 2022-12-06
 */
public interface OrdersMapper extends BaseMapper<Orders> {

    Page<OrderListVo> selectOrderList(Page<OrderListVo> page , @Param("customerId") Long customerId);

    OrderDetailVo selectOrderDetail(@Param("orderId") Long orderId);

    Integer countShopOrderMakeNum(@Param("shopId") Long shopId,@Param("queryDate") Date queryDate);

    MemberVo countMember(@Param("customerId") Long customerId);

    Page<RecommendListVo> selectRecommendList(Page<RecommendListVo> page,@Param("dto") RecommendListDto dto);

    List<RecommendTimeBo> selectRecommendIndexList(@Param("customerId") Long customerId);

    List<PickUpListVo> selectPickUpList(@Param("customerId") Long customerId);
}
