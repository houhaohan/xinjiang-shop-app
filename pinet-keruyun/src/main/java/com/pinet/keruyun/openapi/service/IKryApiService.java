package com.pinet.keruyun.openapi.service;

import com.pinet.keruyun.openapi.dto.*;
import com.pinet.keruyun.openapi.param.*;
import com.pinet.keruyun.openapi.type.AuthType;
import com.pinet.keruyun.openapi.vo.*;
import com.pinet.keruyun.openapi.vo.customer.CustomerCreateVO;
import com.pinet.keruyun.openapi.vo.customer.CustomerPropertyVO;
import com.pinet.keruyun.openapi.vo.customer.CustomerQueryVO;
import com.pinet.keruyun.openapi.vo.customer.DirectChargeVO;

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

    /**
     * 创建会员
     * @param dto
     * @return
     */
    public CustomerCreateVO createCustomer(Long orgId, String token, CustomerCreateDTO dto);


    /**
     * 查询会员
     * @param orgId
     * @param token
     * @param param
     * @return
     */
    public CustomerQueryVO queryByMobile(Long orgId, String token, CustomerParam param);

    /**
     * 会员资产
     * @param orgId
     * @param token
     * @param param
     * @return
     */
    public CustomerPropertyVO queryCustomerProperty(Long orgId, String token, CustomerPropertyParam param);

    /**
     * 会员充值
     * @param orgId
     * @param token
     * @param dto
     * @return
     */
    public DirectChargeVO directCharge(Long orgId, String token, DirectChargeDTO dto);

}
