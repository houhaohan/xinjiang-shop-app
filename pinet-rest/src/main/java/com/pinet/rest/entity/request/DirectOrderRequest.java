package com.pinet.rest.entity.request;

import com.pinet.rest.entity.dto.OrderComboDishDto;
import com.pinet.rest.entity.dto.SideDishGroupDTO;
import lombok.Data;

import java.util.List;

@Data
public class DirectOrderRequest extends OrderProductRequest {

    /**
     * 商品样式 ID
     */
    private List<Long> shopProdSpecIds;

    /**
     * 订单套餐明细
     */
    private List<OrderComboDishDto> comboDishDtoList;

    /**
     * 小料明细
     */
    private List<SideDishGroupDTO> sideDishGroupList;

}
