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
    public KryResult<List<DetailDishVO>> listQueryDetailDish(Long orgId, String token, DetailDishParam param);

    /**
     * 菜品分类
     * @param param
     * @return
     */
    public KryResult<List<CategoryVO>> listQueryCategory(Long orgId, String token, CategoryParam param);


    /**
     * 堂食扫码下单
     * 参考文档： https://open.keruyun.com/docs/zh/p3JOKokB77V9K553kNMI.html
     * @param dto
     * @return
     */
    public ScanCodePrePlaceOrderVo scanCodePrePlaceOrder(Long orgId, String token, KryScanCodeOrderCreateDTO dto);


    /**
     * 外卖下单
     * @param dto
     * @return
     */
    public OrderCreateVO takeoutOrderCreate(Long orgId, String token, KryTakeoutOrderCreateDTO dto);

    /**
     * 外卖下单
     * @param dto
     * @return
     */
    public TakeoutOrderCreateVo openTakeoutOrderCreate(Long orgId, String token, KryOpenTakeoutOrderCreateDTO dto);


    /**
     * 订单详情
     * @param orgId
     * @param token
     * @return
     */
    public OrderDetailVO getOrderDetail(Long orgId, String token,KryOrderDetailDTO dto);

}
