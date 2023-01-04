package com.pinet.rest.controller;


import com.pinet.core.exception.PinetException;
import com.pinet.core.result.Result;
import com.pinet.core.util.StringUtil;
import com.pinet.core.version.ApiVersion;
import com.pinet.rest.entity.dto.*;
import com.pinet.rest.entity.vo.*;
import com.pinet.rest.service.IOrdersService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
@RequestMapping("/{version}/orders")
@Api(tags = "订单模块")
public class OrdersController extends BaseController {
    @Resource
    private IOrdersService ordersService;

    @PostMapping("/orderList")
    @ApiOperation("订单列表")
    @ApiVersion(1)
    public Result<List<OrderListVo>> orderList(@RequestBody OrderListDto dto) {
        List<OrderListVo> orderListVos = ordersService.orderList(dto);
        return Result.ok(orderListVos);
    }


    @RequestMapping("/orderDetail")
    @ApiOperation("订单详情")
    @ApiVersion(1)
    public Result<OrderDetailVo> orderDetail(Long orderId) {
        OrderDetailVo orderDetailVo = ordersService.orderDetail(orderId);
        return Result.ok(orderDetailVo);

    }

    @PostMapping("/orderSettlement")
    @ApiOperation("订单结算")
    @ApiVersion(1)
    public Result<OrderSettlementVo> checkOrder(@Validated @RequestBody OrderSettlementDto dto) {
        checkParam(dto);
        OrderSettlementVo orderSettlementVo = ordersService.orderSettlement(dto);
        return Result.ok(orderSettlementVo);
    }


    @PostMapping("/createOrder")
    @ApiOperation("创建订单")
    @ApiVersion(1)
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
        if (dto.getSettlementType() == 2 && (dto.getShopProdId() == null || dto.getProdNum() == null || StringUtil.isBlank(dto.getShopProdSpecIds()))) {
            throw new PinetException("直接购买必传店铺商品id || 商品数量 || 商品样式id");
        }

    }



    @PostMapping("/orderPay")
    @ApiOperation("订单支付")
    @ApiVersion(1)
    public Result<?> orderPay(@RequestBody @Validated OrderPayDto dto){
        Object pay = ordersService.orderPay(dto);
        return Result.ok(pay);
    }



    @RequestMapping("/cancelOrder")
    @ApiOperation("取消订单")
    @ApiVersion(1)
    public Result<?> cancelOrder(Long orderId){
        Boolean res = ordersService.cancelOrder(orderId);
        if (!res){
            return Result.error("操作失败");
        }
        return Result.ok();
    }

}
