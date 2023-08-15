package com.pinet.keruyun.openapi.service;

import com.pinet.keruyun.openapi.dto.*;
import com.pinet.keruyun.openapi.param.CategoryParam;
import com.pinet.keruyun.openapi.param.DetailDishParam;
import com.pinet.keruyun.openapi.param.DishListParam;
import com.pinet.keruyun.openapi.type.AuthType;
import com.pinet.keruyun.openapi.vo.*;

import java.util.List;

public interface IKryApiService {

    /**
     * 获取token
     * @return
     */
    public String getToken(AuthType authType, Long orgId);


    /**
     * 根据品牌查询门店列表
     * @return
     */
    public List<BrandStoreVO.Shop> queryBrandStores(Long orgId,String token);

    /**
     * 店铺详情
     * @return
     */
    public ShopDetailVO queryShopDetail(Long orgId,String token);


    /**
     * 菜品列表
     * @param orgId
     * @param token
     * @param param
     * @return
     */
    public KryResult<DishListVO> pageQueryBaseDish(Long orgId,String token,DishListParam param);

    /**
     * 菜品详情
     * @return
     */
    public KryResult<DetailDishVO> listQueryDetailDish(Long orgId, String token, DetailDishParam param);

    /**
     * 菜品分类
     * @param param
     * @return
     */
    public KryResult<List<CategoryVO>> listQueryCategory(Long orgId, String token, CategoryParam param);

    /**
     * 快餐下单
     * @param dto
     * @return
     */
    public OrderCreateVO snackOrderCreate(Long orgId, String token, KrySnackOrderCreateDTO dto);


    /**
     * 外卖下单
     * @param dto
     * @return
     */
    public OrderCreateVO takeoutOrderCreate(Long orgId, String token, KryTakeoutOrderCreateDTO dto);

    /**
     * 合作方申请退款 快餐单
     * @param dto
     * @return
     */
    public KryResponse snackOrderApplyRefund(Long orgId, String token, OrderApplyRefundDTO dto);

    /**
     * 合作方申请退款 外卖单
     * @param dto
     * @return
     */
    public KryResponse takeoutOrderApplyRefund(Long orgId, String token, OrderApplyRefundDTO dto);

    /**
     * 合作方取消 外卖订单
     * @param dto
     * @return
     */
    public KryResponse takeoutOrderCancel(Long orgId, String token, TakeoutOrderCancelDTO dto);

    /**
     * 通知客如云取消订单
     * @param dto
     * @return
     */
    public KryResponse snackOrderRefund(Long orgId, String token,SnackOrderRefundDTO dto);

    /**
     * 合作方推送配送状态通知
     * @param dto
     * @return
     */
    public KryResponse takeoutOrderStatusPush(Long orgId, String token,TakeoutOrderStatusPushDTO dto);

    /**
     * 订单状态查询
     * @param dto
     * @return
     */
    public KryResponse takeoutOrderStatusGet(Long orgId, String token,TakeoutOrderStatusGetDTO dto);

}
