package com.pinet.rest.mapper;

import com.pinet.rest.entity.OrderProduct;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pinet.rest.entity.bo.OrderProductBo;
import com.pinet.rest.entity.dto.OrderProductDto;
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


    List<OrderProduct> selectByOrderId(@Param("orderId") Long orderId);

    /**
     * 根据订单ID查询商品信息，对接客如云订单 使用
     * @param orderId
     * @return
     */
    List<OrderProductDto> getByOrderId(@Param("orderId") Long orderId);

    /**
     * 根据订单查询套餐信息
     * @param orderId
     * @return
     */
    List<OrderProduct> getComboByOrderId(@Param("orderId") Long orderId);

}


