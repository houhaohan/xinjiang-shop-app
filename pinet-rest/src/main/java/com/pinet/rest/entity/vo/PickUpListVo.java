package com.pinet.rest.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @program: xinjiang-shop-app
 * @description: 自提兑换码vo
 * @author: hhh
 * @create: 2023-07-10 16:51
 **/
@Data
public class PickUpListVo {
    @ApiModelProperty(value = "创建时间",name = "createTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm",timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "店铺名称",name = "shopName")
    private String shopName;

    @ApiModelProperty(value = "订单ID",name = "id")
    private String id;

    @ApiModelProperty(value = "订单状态",name = "orderStatus")
    private Integer orderStatus;

    @ApiModelProperty(value = "订单状态str",name = "orderStatusStr")
    private String orderStatusStr;

    @ApiModelProperty(value = "取餐码",name = "mealCode")
    private String mealCode;
}
