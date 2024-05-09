package com.pinet.keruyun.openapi.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.pinet.keruyun.openapi.config.KryApiParamConfig;
import com.pinet.keruyun.openapi.dto.*;
import com.pinet.keruyun.openapi.param.*;
import com.pinet.keruyun.openapi.service.IKryApiService;
import com.pinet.keruyun.openapi.service.KryCallService;
import com.pinet.keruyun.openapi.type.AuthType;
import com.pinet.keruyun.openapi.type.KryAPI;
import com.pinet.keruyun.openapi.util.JsonUtil;
import com.pinet.keruyun.openapi.vo.*;
import com.pinet.keruyun.openapi.vo.customer.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class KryApiServiceImpl extends KryCallService implements IKryApiService {

    public KryApiServiceImpl(KryApiParamConfig kryApiParamConfig) {
        super(kryApiParamConfig);
    }

    @Override
    public String getToken(AuthType authType, Long orgId) {
        return super.getToken(authType,orgId);
    }

    @Override
    public List<BrandStoreVO.Shop> queryBrandStores(Long orgId,String token) {
        String responseStr = super.postCall(KryAPI.BRAND_SHOP_LIST, AuthType.BRAND, orgId, token, null);
        KryResponse<BrandStoreVO> response = JsonUtil.fromJson(responseStr, new TypeReference<KryResponse<BrandStoreVO>>() {
        });
        if (0 == response.getCode()) {
            return response.getResult().getShops();
        } else {
            throw new RuntimeException(response.getMessage());
        }
    }

    @Override
    public ShopDetailVO queryShopDetail(Long orgId, String token) {
        String responseStr = super.postCall(KryAPI.BRAND_SHOP_DETAIL, AuthType.SHOP, orgId, token, null);
        KryResponse<ShopDetailVO> response = JsonUtil.fromJson(responseStr, new TypeReference<KryResponse<ShopDetailVO>>() {
        });
        return response.getResult();
    }

    @Override
    public KryResult<DishListVO> pageQueryBaseDish(Long orgId, String token, DishListParam param) {
        String responseStr = super.postCall(KryAPI.BRAND_SHOP_DISH, AuthType.SHOP, orgId, token, param);
        KryResponse<KryResult<DishListVO>> response = JsonUtil.fromJson(responseStr, new TypeReference<KryResponse<KryResult<DishListVO>>>() {
        });
        return response.getResult();
    }

    @Override
    public KryResult<List<DetailDishVO>> listQueryDetailDish(Long orgId, String token, DetailDishParam param) {
        String responseStr = super.postCall(KryAPI.BRAND_SHOP_DETAIL_DISH, AuthType.SHOP, orgId, token, param);
        KryResponse<KryResult<List<DetailDishVO>>> response = JsonUtil.fromJson(responseStr, new TypeReference<KryResponse<KryResult<List<DetailDishVO>>>>() {
        });
        return response.getResult();
    }

    @Override
    public KryResult<List<CategoryVO>> listQueryCategory(Long orgId, String token, CategoryParam param) {
        String responseStr = super.postCall(KryAPI.BRAND_SHOP_CATEGORY, AuthType.SHOP, orgId, token, param);
        KryResponse<KryResult<List<CategoryVO>>> response = JsonUtil.fromJson(responseStr, new TypeReference<KryResponse<KryResult<List<CategoryVO>>>>() {
        });
        if (0 == response.getCode()) {
            return response.getResult();
        }
        return null;
    }


    @Override
    public ScanCodePrePlaceOrderVo scanCodePrePlaceOrder(Long orgId, String token, KryScanCodeOrderCreateDTO dto) {
        String responseStr = super.postCall(KryAPI.SCAN_CODE_ORDER_CREATE, AuthType.SHOP, orgId, token, dto);
        log.info("客如云堂食订单同步结果=============>{}",responseStr);
        KryResponse<ScanCodePrePlaceOrderVo> response = JsonUtil.fromJson(responseStr, new TypeReference<KryResponse<ScanCodePrePlaceOrderVo>>() {
        });
        return response.getResult();
    }

    @Override
    public OrderCreateVO takeoutOrderCreate(Long orgId, String token, KryTakeoutOrderCreateDTO dto) {
        String responseStr = super.postCall(KryAPI.TAKEOUT_ORDER_CREATE, AuthType.SHOP, orgId, token, dto);
        log.info("客如云外卖订单同步结果=============>{}",responseStr);
        KryResponse<OrderCreateVO> response = JsonUtil.fromJson(responseStr, new TypeReference<KryResponse<OrderCreateVO>>() {
        });
        if (0 == response.getCode()) {

            return response.getResult();
        }
        return null;
    }

    @Override
    public TakeoutOrderCreateVo openTakeoutOrderCreate(Long orgId, String token, KryOpenTakeoutOrderCreateDTO dto) {
        String responseStr = super.postCall(KryAPI.TAKEOUT_ORDER_CREATE, AuthType.SHOP, orgId, token, dto);
        log.info("客如云外卖订单同步结果=============>{}",responseStr);
        KryResponse<TakeoutOrderCreateVo> response = JsonUtil.fromJson(responseStr, new TypeReference<KryResponse<TakeoutOrderCreateVo>>() {
        });
        if (0 == response.getCode()) {
            return response.getResult();
        }
        return null;
    }

    @Override
    public OrderDetailVO getOrderDetail(Long orgId, String token,KryOrderDetailDTO dto) {
        String responseStr = super.postCall(KryAPI.ORDER_DETAIL, AuthType.SHOP, orgId, token, dto);
        log.info("客如云订单详情结果=============>{}",responseStr);
        KryResponse<KryOrderDetailResult> response = JsonUtil.fromJson(responseStr, new TypeReference<KryResponse<KryOrderDetailResult>>() {
        });
        if (0 == response.getCode()) {
            return response.getResult().getData();
        }
        return null;
    }

    @Override
    public CustomerCreateVO createCustomer(Long orgId, String token, CustomerCreateDTO dto) {
        String responseStr = super.postCall(KryAPI.CUSTOMER_CREATE, AuthType.SHOP, orgId, token, dto);
        log.info("客如云创建会员结果=============>{}",responseStr);
        KryResponse<Result<CustomerCreateVO>> response = JsonUtil.fromJson(responseStr, new TypeReference<KryResponse<Result<CustomerCreateVO>>>() {
        });
        if (0 == response.getCode()) {
            return response.getResult().getData();
        }
        return null;
    }

    @Override
    public CustomerQueryVO queryByMobile(Long orgId, String token, CustomerParam param) {
        String responseStr = super.postCall(KryAPI.CUSTOMER_QUERY, AuthType.SHOP, orgId, token, param);
        log.info("客如云查询会员结果=============>{}",responseStr);
        KryResponse<Result<CustomerQueryVO>> response = JsonUtil.fromJson(responseStr, new TypeReference<KryResponse<Result<CustomerQueryVO>>>() {
        });
        if (0 == response.getCode()) {
            return response.getResult().getData();
        }
        return null;
    }

    @Override
    public CustomerPropertyVO queryCustomerProperty(Long orgId, String token, CustomerPropertyParam param) {
        String responseStr = super.postCall(KryAPI.CUSTOMER_PROPERTY, AuthType.SHOP, orgId, token, param);
        log.info("客如云查询会员资产结果=============>{}",responseStr);
        KryResponse<Result<CustomerPropertyVO>> response = JsonUtil.fromJson(responseStr, new TypeReference<KryResponse<Result<CustomerPropertyVO>>>() {
        });
        if (0 == response.getCode()) {
            return response.getResult().getData();
        }
        return null;
    }

    @Override
    public DirectChargeVO directCharge(Long orgId, String token, DirectChargeDTO dto) {
        String responseStr = super.postCall(KryAPI.CUSTOMER_PROPERTY, AuthType.SHOP, orgId, token, dto);
        log.info("客如云查询会员资产结果=============>{}",responseStr);
        KryResponse<Result<DirectChargeVO>> response = JsonUtil.fromJson(responseStr, new TypeReference<KryResponse<Result<DirectChargeVO>>>() {
        });
        if (0 == response.getCode()) {
            return response.getResult().getData();
        }
        return null;
    }


}
