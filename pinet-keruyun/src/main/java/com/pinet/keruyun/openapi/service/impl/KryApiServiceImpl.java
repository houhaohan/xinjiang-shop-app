package com.pinet.keruyun.openapi.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.pinet.keruyun.openapi.config.KryApiParamConfig;
import com.pinet.keruyun.openapi.dto.*;
import com.pinet.keruyun.openapi.param.CategoryParam;
import com.pinet.keruyun.openapi.param.DetailDishParam;
import com.pinet.keruyun.openapi.param.DishListParam;
import com.pinet.keruyun.openapi.service.IKryApiService;
import com.pinet.keruyun.openapi.service.KryCallService;
import com.pinet.keruyun.openapi.type.AuthType;
import com.pinet.keruyun.openapi.type.KryAPI;
import com.pinet.keruyun.openapi.util.JsonUtil;
import com.pinet.keruyun.openapi.vo.*;
import okhttp3.OkHttpClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KryApiServiceImpl extends KryCallService implements IKryApiService {

    public KryApiServiceImpl(OkHttpClient okHttpClient, KryApiParamConfig kryApiParamConfig) {
        super(okHttpClient, kryApiParamConfig);
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
        System.err.println(responseStr);
        KryResponse<KryResult<List<DetailDishVO>>> response = JsonUtil.fromJson(responseStr, new TypeReference<KryResponse<KryResult<List<DetailDishVO>>>>() {
        });
        return response.getResult();
    }

    @Override
    public KryResult<List<CategoryVO>> listQueryCategory(Long orgId, String token, CategoryParam param) {
        String responseStr = super.postCall(KryAPI.BRAND_SHOP_CATEGORY, AuthType.SHOP, orgId, token, param);
        System.err.println(responseStr);

        KryResponse<KryResult<List<CategoryVO>>> response = JsonUtil.fromJson(responseStr, new TypeReference<KryResponse<KryResult<List<CategoryVO>>>>() {
        });
        if (0 == response.getCode()) {
            return response.getResult();
        }
        return null;
    }


    @Override
    public String scanCodePrePlaceOrder(Long orgId, String token, KryScanCodeOrderCreateDTO dto) {
        String responseStr = super.postCall(KryAPI.SCAN_CODE_ORDER_CREATE, AuthType.SHOP, orgId, token, dto);
        System.err.println(responseStr);
        KryResponse<OrderCreateVO> response = JsonUtil.fromJson(responseStr, new TypeReference<KryResponse<OrderCreateVO>>() {
        });
        if (0 == response.getCode()) {
            return JsonUtil.toJson(response.getResult());
        }
        return JsonUtil.toJson(response);
    }

    @Override
    public OrderCreateVO takeoutOrderCreate(Long orgId, String token, KryTakeoutOrderCreateDTO dto) {
        String responseStr = super.postCall(KryAPI.TAKEOUT_ORDER_CREATE, AuthType.SHOP, orgId, token, dto);
        KryResponse<OrderCreateVO> response = JsonUtil.fromJson(responseStr, new TypeReference<KryResponse<OrderCreateVO>>() {
        });
        if (0 == response.getCode()) {
            return response.getResult();
        }
        return null;
    }

    @Override
    public String openTakeoutOrderCreate(Long orgId, String token, KryOpenTakeoutOrderCreateDTO dto) {
        String responseStr = super.postCall(KryAPI.TAKEOUT_ORDER_CREATE, AuthType.SHOP, orgId, token, dto);
        System.err.println(responseStr);
        KryResponse<OrderCreateVO> response = JsonUtil.fromJson(responseStr, new TypeReference<KryResponse<OrderCreateVO>>() {
        });
        if (0 == response.getCode()) {
            return JsonUtil.toJson(response.getResult());
        }
        return JsonUtil.toJson(response);
    }

    @Override
    public KryResponse snackOrderApplyRefund(Long orgId, String token, OrderApplyRefundDTO dto) {
        String responseStr = super.postCall(KryAPI.SNACK_ORDER_APPLY_REFUND, AuthType.SHOP, orgId, token, dto);
        KryResponse response = JsonUtil.fromJson(responseStr, KryResponse.class);
        return response;
    }

    @Override
    public KryResponse takeoutOrderApplyRefund(Long orgId, String token, OrderApplyRefundDTO dto) {
        String responseStr = super.postCall(KryAPI.TAKEOUT_ORDER_APPLY_REFUND, AuthType.SHOP, orgId, token, dto);
        KryResponse response = JsonUtil.fromJson(responseStr, KryResponse.class);
        return response;
    }

    @Override
    public KryResponse takeoutOrderCancel(Long orgId, String token, TakeoutOrderCancelDTO dto) {
        String responseStr = super.postCall(KryAPI.TAKEOUT_ORDER_CANCEL, AuthType.SHOP, orgId, token, dto);
        KryResponse response = JsonUtil.fromJson(responseStr, KryResponse.class);
        return response;
    }

    @Override
    public KryResponse snackOrderRefund(Long orgId, String token, SnackOrderRefundDTO dto) {
        String responseStr = super.postCall(KryAPI.SNACK_ORDER_REFUND, AuthType.SHOP, orgId, token, dto);
        KryResponse response = JsonUtil.fromJson(responseStr, KryResponse.class);
        return response;
    }

    @Override
    public KryResponse takeoutOrderStatusPush(Long orgId, String token, TakeoutOrderStatusPushDTO dto) {
        String responseStr = super.postCall(KryAPI.TAKEOUT_ORDER_STATUS_PUSH, AuthType.SHOP, orgId, token, dto);
        KryResponse response = JsonUtil.fromJson(responseStr, KryResponse.class);
        return response;
    }

    @Override
    public KryResponse takeoutOrderStatusGet(Long orgId, String token, TakeoutOrderStatusGetDTO dto) {
        String responseStr = super.postCall(KryAPI.TAKEOUT_ORDER_STATUS_GET, AuthType.SHOP, orgId, token, dto);
        KryResponse response = JsonUtil.fromJson(responseStr, KryResponse.class);
        return response;
    }


}
