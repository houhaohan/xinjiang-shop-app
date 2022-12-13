package com.pinet.rest.controller;


import com.pinet.core.result.Result;
import com.pinet.core.util.ThreadLocalUtil;
import com.pinet.inter.annotation.NotTokenSign;
import com.pinet.rest.entity.dto.OrderListDto;
import com.pinet.rest.entity.dto.OrderSettlementDto;
import com.pinet.rest.entity.vo.OrderDetailVo;
import com.pinet.rest.entity.vo.OrderListVo;
import com.pinet.rest.entity.vo.OrderSettlementVo;
import com.pinet.rest.service.IOrderService;
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
@RequestMapping("/order")
@Api(tags = "订单模块")
public class OrderController extends BaseController {
    @Resource
    private IOrderService orderService;

    @PostMapping("/orderList")
    @ApiOperation("订单列表")
    public Result<List<OrderListVo>> orderList(OrderListDto dto) {
        List<OrderListVo> orderListVos = orderService.orderList(dto);
        return Result.ok(orderListVos);
    }


    @PostMapping("/orderDetail")
    @ApiOperation("订单详情")
    public Result<OrderDetailVo> orderDetail(Long orderId) {
        OrderDetailVo orderDetailVo = orderService.orderDetail(orderId);
        return Result.ok(orderDetailVo);

    }

    @PostMapping("/orderSettlement")
    @ApiOperation("订单结算")
    public Result<OrderSettlementVo> checkOrder(@Validated @RequestBody OrderSettlementDto dto) {
        OrderSettlementVo orderSettlementVo = orderService.orderSettlement(dto);
        return Result.ok(orderSettlementVo);
    }

}
