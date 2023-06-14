package com.pinet.rest.entity.bo;

import com.pinet.rest.entity.OrderProduct;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @program: xinjiang-shop-app
 * @description: 分享时间bo
 * @author: hhh
 * @create: 2023-06-14 15:25
 **/
@Data
public class RecommendTimeBo {

    @ApiModelProperty(value = "订单id",name = "orderId")
    private Long orderId;

    @ApiModelProperty(value = "时分秒",name = "date")
    private String time;

    @ApiModelProperty(value = "商品数据",name = "orderProductBoList")
    private List<OrderProduct> orderProducts;

    @ApiModelProperty(value = "用户昵称")
    private String nickName;

    @ApiModelProperty(value = "用户头像")
    private String avatar;

    @ApiModelProperty(value = "本月下单数")
    private Integer countOrder;

    @ApiModelProperty(value = "佣金")
    private BigDecimal commission;
}
