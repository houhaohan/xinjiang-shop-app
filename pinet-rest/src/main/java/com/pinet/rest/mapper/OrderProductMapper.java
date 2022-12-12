package com.pinet.rest.mapper;

import com.pinet.rest.entity.OrderProduct;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pinet.rest.entity.bo.OrderProductBo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 订单商品表 Mapper 接口
 * </p>
 *
 * @author wlbz
 * @since 2022-12-08
 */
public interface OrderProductMapper extends BaseMapper<OrderProduct> {

    List<OrderProductBo> selectOrderProduct(@Param("orderId") Long orderId);
}
