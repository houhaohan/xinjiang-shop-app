package com.pinet.rest.handler.order;

import com.pinet.rest.entity.dto.OrderComboDishDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DirectOrderRequest {

    private Long orderId;
    private List<Long> shopProdSpecIds;
    private Long shopProdId;
    private String dishId;
    private String productName;
    private Integer prodNum;
    private String unit;
    private String productImg;
    private boolean calculate;
    /**
     * 订单状态  1外卖  2自提
     */
    private Integer orderType;
    private List<OrderComboDishDto> comboDishDtoList;
}
