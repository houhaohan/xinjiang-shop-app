package com.pinet.rest.mapper;

import com.pinet.rest.entity.OrderComboDish;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pinet.rest.entity.vo.OrderComboDishVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 订单套餐菜品表 Mapper 接口
 * </p>
 *
 * @author wlbz
 * @since 2024-03-20
 */
public interface OrderComboDishMapper extends BaseMapper<OrderComboDish> {


    /**
     * 根据订单 ID 和套餐 ID查询
     * @param orderId
     * @param shopProdId
     * @return
     */
    List<OrderComboDishVo> getByOrderIdAndShopProdId(@Param("orderId") Long orderId, @Param("shopProdId") Long shopProdId);
}
