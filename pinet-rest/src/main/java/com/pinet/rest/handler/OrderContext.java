package com.pinet.rest.handler;

import com.pinet.common.mq.util.JmsUtil;
import com.pinet.core.util.SpringContextUtils;
import com.pinet.rest.entity.OrderDiscount;
import com.pinet.rest.entity.OrderProduct;
import com.pinet.rest.entity.Orders;
import com.pinet.rest.entity.Shop;
import com.pinet.rest.entity.dto.CreateOrderDto;
import com.pinet.rest.entity.enums.OrderTypeEnum;
import com.pinet.rest.entity.vo.CreateOrderVo;
import com.pinet.rest.entity.vo.OrderProductVo;
import com.pinet.rest.service.*;

import java.util.List;

/**
 * @description: 订单上下文
 * @author: chengshuanghui
 * @date: 2024-03-09 10:31
 */
public class OrderContext {
    protected IShopService shopService;
    protected ICartService cartService;
    protected IOrderProductService orderProductService;
    protected IShippingFeeRuleService shippingFeeRuleService;
    protected ICustomerAddressService customerAddressService;
    protected OrderPreferentialManager orderPreferentialManager;
    protected ICustomerMemberService customerMemberService;
    protected IOrdersService ordersService;
    protected IOrderProductSpecService orderProductSpecService;
    protected IOrderAddressService orderAddressService;
    protected ICustomerService customerService;
    protected IOrderDiscountService orderDiscountService;
    protected  JmsUtil jmsUtil;
    protected IDaDaService daDaService;
    protected OrderHandler orderHandler;
    protected Long userId;
    protected Shop shop;
    protected Orders order;
    protected List<OrderProductVo> orderProducts;
    protected List<OrderDiscount> orderDiscounts;
    protected CreateOrderDto request;
    protected CreateOrderVo result;
    protected OrderTypeEnum orderTypeEnum;

    public OrderContext(Long userId,OrderTypeEnum orderTypeEnum){
        init();
        this.userId = userId;
        this.orderTypeEnum = orderTypeEnum;
        if(OrderTypeEnum.SELF_PICKUP == orderTypeEnum){
            this.orderHandler = new SelfPickupOrderHandler(this);
        }else if(OrderTypeEnum.TAKEAWAY == orderTypeEnum) {
            this.orderHandler = new TakeawayOrderHandler(this);
        }
    }

    public void setRequest(CreateOrderDto request){
        this.request = request;
    }

    public void setShop(Shop shop){
        this.shop = shop;
    }

    private void init(){
        shopService = SpringContextUtils.getBean(IShopService.class);
        cartService = SpringContextUtils.getBean(ICartService.class);
        orderProductService = SpringContextUtils.getBean(IOrderProductService.class);
        shippingFeeRuleService = SpringContextUtils.getBean(IShippingFeeRuleService.class);
        customerAddressService = SpringContextUtils.getBean(ICustomerAddressService.class);
        orderPreferentialManager = SpringContextUtils.getBean(OrderPreferentialManager.class);
        customerMemberService = SpringContextUtils.getBean(ICustomerMemberService.class);
        daDaService = SpringContextUtils.getBean(IDaDaService.class);
        orderProductSpecService = SpringContextUtils.getBean(IOrderProductSpecService.class);
        ordersService = SpringContextUtils.getBean(IOrdersService.class);
        orderAddressService = SpringContextUtils.getBean(IOrderAddressService.class);
        customerService = SpringContextUtils.getBean(ICustomerService.class);
        orderDiscountService = SpringContextUtils.getBean(IOrderDiscountService.class);
        jmsUtil = SpringContextUtils.getBean(JmsUtil.class);
    }

    /**
     * 创建的订单
     * @return
     */
    public void createOrder() {
        orderHandler.createOrder();
    }

    /**
     * 成功后业务处理
     */
    public void successHandler() {
        orderHandler.successHandler();
    }

    /**
     * 失败后业务处理
     */
    public void failHandler() {
        //orderHandler.successHandler();
    }

    /**
     * 成功后业务处理
     */
    public void payCallBackSuccessHandler() {
        orderHandler.successHandler();
    }
}
