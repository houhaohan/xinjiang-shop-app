package com.pinet.keruyun.openapi.type;

import lombok.Getter;

/**
 * @author zhaobo
 */

public enum KryAPI {
    GET_SHOP_TOKEN("获取门店token", "/open/v1/token/get"),
    AUTH_SHOP_LIST("授权门店列表", "/open/v1/shop/shoplist"),
    BRAND_SHOP_DETAIL("门店详情", "/open/standard/shop/MerchantOrgReadService/queryById"),
    PAY_WAY_QUERY("获取门店支付类型", "/open/v1/pay/payWay/query"),
    ORDER_DETAIL("订单详情", "/open/standard/order/queryDetail"),
    ORDER_LIST("订单列表", "/open/standard/order/queryList"),
    SCAN_CODE_ORDER_CREATE("扫码下单", "/open/standard/snack/open/standard/snack/OpenPlatformBuyFacade/scanCodePrePlaceOrder"),
    TAKEOUT_ORDER_CREATE("外卖下单", "/open/standard/takeout/open/standard/takeout/OpenPlatformBuyFacade/takeoutPlaceOrder"),
    BRAND_SHOP_LIST("品牌门店列表", "/open/standard/shop/MerchantOrgReadService.queryBrandStores"),
    BRAND_SHOP_DISH("菜品列表信息", "/open/standard/dish/shop/pageQueryBaseDish"),
    BRAND_SHOP_DETAIL_DISH("菜品详情信息", "/open/standard/dish/shop/listQueryDetailDish"),
    BRAND_SHOP_CATEGORY("菜品分类信息", "/open/standard/dish/shop/listQueryCategory"),
    CUSTOMER_CREATE("创建会员", "/open/standard/crm/CustomerOpenFacade/createCustomer"),
    CUSTOMER_QUERY("查询会员", "/open/standard/crm/CustomerOpenFacade/queryByMobile"),
    CUSTOMER_PROPERTY("会员资产", "/open/standard/crm/CustomerOpenFacade/queryCustomerProperty"),
    DIRECT_CHARGE("会员储值充值", "/open/standard/crm/RechargeOpenFacade/directCharge");

    private String description;
    @Getter
    private String uri;

    KryAPI(String description, String uri) {
        this.description = description;
        this.uri = uri;
    }
}
