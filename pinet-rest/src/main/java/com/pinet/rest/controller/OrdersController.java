package com.pinet.rest.controller;


import cn.hutool.core.lang.UUID;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.pinet.core.controller.BaseController;
import com.pinet.core.exception.PinetException;
import com.pinet.core.result.Result;
import com.pinet.core.util.StringUtil;
import com.pinet.core.version.ApiVersion;
import com.pinet.inter.annotation.NotTokenSign;
import com.pinet.keruyun.openapi.dto.OrderBodyDTO;
import com.pinet.keruyun.openapi.dto.OrderChangeEventDTO;
import com.pinet.keruyun.openapi.dto.OrderSyncDTO;
import com.pinet.keruyun.openapi.dto.PerformanceCallDTO;
import com.pinet.keruyun.openapi.vo.KryResponse;
import com.pinet.rest.entity.Orders;
import com.pinet.rest.entity.dto.*;
import com.pinet.rest.entity.enums.OrderTypeEnum;
import com.pinet.rest.entity.enums.SettlementTypeEnum;
import com.pinet.rest.entity.vo.*;
import com.pinet.rest.service.IOrdersService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
@Slf4j
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


    @RequestMapping(value = "/orderDetail",method = RequestMethod.GET)
    @ApiOperation("订单详情")
    @ApiVersion(1)
    @NotTokenSign
    public Result<OrderDetailVo> orderDetail(Long orderId) {
        OrderDetailVo orderDetailVo = ordersService.orderDetail(orderId);
        return Result.ok(orderDetailVo);

    }

    @PostMapping("/orderSettlement")
    @ApiOperation("订单结算")
    @ApiVersion(1)
    public Result<OrderSettlementVo> orderSettlement(@Validated @RequestBody OrderSettlementDto dto){
        checkParam(dto);
        OrderSettlementVo orderSettlementVo = ordersService.orderSettlement(dto);
        return Result.ok(orderSettlementVo);
    }


    @PostMapping("/createOrder")
    @ApiOperation("创建订单")
    @ApiVersion(1)
    public Result<CreateOrderVo> createOrder(@Validated @RequestBody CreateOrderDto dto) {
        checkParam(dto);
        CreateOrderVo vo = ordersService.createOrder(dto);
        return Result.ok(vo);
    }

    private void checkParam(OrderSettlementDto dto){
        if (Objects.equals(dto.getOrderType(),OrderTypeEnum.TAKEAWAY.getCode()) && dto.getCustomerAddressId() == null){
            throw new PinetException("外卖订单收货地址id必传");
        }
        //如果是直接购买  店铺商品id 和商品数量为必传
        //判断是单品还是套餐
        if(Objects.equals(dto.getSettlementType(), SettlementTypeEnum.DIRECT_BUY.getCode())){
            //单品校验
            if(dto.getShopProdId() == null){
                throw new PinetException("商品ID不能为空");
            }
            if(dto.getProdNum() == null){
                throw new PinetException("商品数量不能为空");
            }
            if(StringUtil.isBlank(dto.getShopProdSpecIds())
                    && CollectionUtils.isEmpty(dto.getOrderComboDishList())){
                throw new PinetException("商品样式明细不能为空");
            }
        }
    }


    @PostMapping("/orderPay")
    @ApiOperation("订单支付")
    @ApiVersion(1)
    public Result<?> orderPay(@RequestBody @Validated OrderPayDto dto){
        Object pay = ordersService.orderPay(dto);
        return Result.ok(pay);
    }



    @GetMapping("/cancelOrder")
    @ApiOperation("取消订单")
    @ApiVersion(1)
    public Result<?> cancelOrder(Long orderId){
        Boolean res = ordersService.cancelOrder(orderId);
        if (!res){
            return Result.error("操作失败");
        }
        return Result.ok();
    }


    @RequestMapping(value = "/recurOrder",method = RequestMethod.GET)
    @ApiOperation("再来一单")
    @ApiVersion(1)
    public Result<?> recurOrder(Long orderId){
        ordersService.recurOrder(orderId,currentUserId());
        return Result.ok();
    }


    @RequestMapping(value = "/pickUpList")
    @ApiOperation("自提兑换码")
    @ApiVersion(1)
    public Result<List<PickUpListVo>> pickUpList(){
        List<PickUpListVo> pickUpListVos = ordersService.pickUpList();
        return Result.ok(pickUpListVos);
    }

    /**
     * 参考文档 https://gldopen.keruyun.com/docs/zh/tmIav4gBzPVmqdQu6S2K.html
     * @param validate
     * @param dto
     * @return
     */
    @PostMapping("/syncOrderStatus")
    @ApiOperation("同步订单状态")
    @ApiVersion(1)
    @NotTokenSign
    public KryResponse syncOrderStatus(@RequestParam(required = false) String validate,@RequestBody OrderSyncDTO dto){
        log.info("订单状态同步参数===========>{}",JSONObject.toJSONString(dto));
        OrderBodyDTO orderBody = dto.getOrderBody();
        if(orderBody != null){
            if(StringUtil.isNotBlank(orderBody.getMealCode())){
                UpdateWrapper<Orders> wrapper = new UpdateWrapper<>();
                wrapper.eq("order_no", orderBody.getOutBizId());
                Orders orders = new Orders();
                orders.setMealCode(orderBody.getMealCode());
                ordersService.update(orders,wrapper);
            }
            if("ORDER".equalsIgnoreCase(dto.getDomain())
                    && "ORDER_SUCCESS".equalsIgnoreCase(dto.getEventCode())
                    && "ORDER_REFUND".equalsIgnoreCase(orderBody.getRefundAction())){
                ordersService.syncOrderStatus(dto);
            }
        }


        KryResponse response = new KryResponse();
        if(StringUtil.isBlank(validate)){
            response.setCode(0);
        }else if("success".equals(validate)){
            response.setCode(0);
        }else {
            response.setCode(-1);
        }
        response.setMessage("成功[OK]");
        response.setMessageUuid(UUID.randomUUID().toString());
        return response;

    }

    /**
     * 参考文档 https://open.keruyun.com/docs/zh/wrhtPoIBQmWK-9w5KC3P.html
     * @param validate
     * @param dto
     * @return
     */
    @PostMapping("/orderChangeEvent")
    @ApiOperation("订单变更事件同步")
    @ApiVersion(1)
    @NotTokenSign
    public KryResponse orderChangeEvent(@RequestParam(required = false) String validate, @RequestBody OrderChangeEventDTO dto){
        log.info("订单变更事件同步===============》{}",JSONObject.toJSONString(dto));

        KryResponse response = new KryResponse();
        response.setMessage("成功[OK]");
        response.setMessageUuid(UUID.randomUUID().toString());

        if("CANCELLED".equalsIgnoreCase(dto.getOrderStatus())
                || "INVALID".equalsIgnoreCase(dto.getOrderStatus())
                || "CLOSED".equalsIgnoreCase(dto.getOrderStatus())){
            //ordersService.cancelOrder(Long.valueOf(dto.getOrderId()));
        }else if("SUCCESS".equalsIgnoreCase(dto.getOrderStatus())){
            //已完成
            //ordersService.completeOrder(Long.valueOf(dto.getOrderId()));
        }

        if(StringUtil.isBlank(validate)){
            response.setCode(0);
        }else if("success".equals(validate)){
            response.setCode(0);
        }else {
            response.setCode(-1);
        }
        return response;
    }

    /**
     * 参考文档 https://gldopen.keruyun.com/docs/zh/OnOu2IgBzPVmqdQu73n2.html
     * @param validate
     * @param dto
     * @return
     */
    @PostMapping("/performanceCall")
    @ApiOperation("履约叫号触达")
    @ApiVersion(1)
    @NotTokenSign
    public KryResponse performanceCall(@RequestParam(required = false) String validate, @RequestBody PerformanceCallDTO dto){
        log.info("履约叫号触达参数===============>{}",JSONObject.toJSONString(dto));
        if(!"OPEN_PLATFORM".equalsIgnoreCase(dto.getOrderSource())){
            return null;
        }

        KryResponse response = new KryResponse();
        response.setMessage("成功[OK]");
        response.setMessageUuid(UUID.randomUUID().toString());
        if(StringUtil.isBlank(validate) || "success".equals(validate)){
            ordersService.performanceCall(dto);
            response.setCode(0);
            return response;
        }
        response.setCode(-1);
        return response;
    }

}
