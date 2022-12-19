package com.pinet.rest.controller;


import com.pinet.core.exception.PinetException;
import com.pinet.core.result.Result;
import com.pinet.rest.entity.dto.CreateOrderDto;
import com.pinet.rest.entity.dto.OrderListDto;
import com.pinet.rest.entity.dto.OrderPayDto;
import com.pinet.rest.entity.dto.OrderSettlementDto;
import com.pinet.rest.entity.vo.*;
import com.pinet.rest.service.IOrdersService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.pinet.core.controller.BaseController;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 订单表 前端控制器
 * </p>
 *
 * @author wlbz
 * @since 2022-12-06
 */
@RestController
@RequestMapping("/orders")
@Api(tags = "订单模块")
public class OrdersController extends BaseController {
    @Resource
    private IOrdersService ordersService;

    @PostMapping("/orderList")
    @ApiOperation("订单列表")
    public Result<List<OrderListVo>> orderList(@RequestBody OrderListDto dto) {
        List<OrderListVo> orderListVos = ordersService.orderList(dto);
        return Result.ok(orderListVos);
    }


    @RequestMapping("/orderDetail")
    @ApiOperation("订单详情")
    public Result<OrderDetailVo> orderDetail(Long orderId) {
        OrderDetailVo orderDetailVo = ordersService.orderDetail(orderId);
        return Result.ok(orderDetailVo);

    }

    @PostMapping("/orderSettlement")
    @ApiOperation("订单结算")
    public Result<OrderSettlementVo> checkOrder(@Validated @RequestBody OrderSettlementDto dto) {
        checkParam(dto);
        OrderSettlementVo orderSettlementVo = ordersService.orderSettlement(dto);
        return Result.ok(orderSettlementVo);
    }


    @PostMapping("/createOrder")
    @ApiOperation("创建订单")
    public Result<CreateOrderVo> createOrder(@Validated @RequestBody CreateOrderDto dto) {
        checkParam(dto);
        if (dto.getOrderType() == 1 && dto.getCustomerAddressId() == null){
            throw new PinetException("外卖订单收货地址id必传");
        }
        CreateOrderVo vo = ordersService.createOrder(dto);
        return Result.ok(vo);
    }

    private void checkParam(OrderSettlementDto dto){
        //如果是直接购买  店铺商品id 和商品数量为必传
        if (dto.getSettlementType() == 2 && (dto.getShopProdId() == null || dto.getProdNum() == null || dto.getShopProdSpecId() == null)) {
            throw new PinetException("直接购买必传店铺商品id || 商品数量 || 商品样式id");
        }

    }



    @PostMapping("/orderPay")
    @ApiOperation("订单支付")
    public Result<OrderPayVo> orderPay(@Validated OrderPayDto dto){
        OrderPayVo orderPayVo = ordersService.orderPay(dto);
        return Result.ok(orderPayVo);
    }



}
