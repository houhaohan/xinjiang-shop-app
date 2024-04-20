package com.pinet.rest.handler.cart;

import com.pinet.core.util.SpringContextUtils;
import com.pinet.keruyun.openapi.constants.DishType;
import com.pinet.rest.entity.ShopProduct;
import com.pinet.rest.entity.dto.AddCartDTO;
import com.pinet.rest.entity.vo.AddCartVo;
import com.pinet.rest.mapper.CartMapper;
import com.pinet.rest.service.*;

import java.math.BigDecimal;
import java.util.List;

public class CartContext {
    protected ICartProductSpecService cartProductSpecService;

    protected IOrderProductService orderProductService;

    protected IKryComboGroupDetailService kryComboGroupDetailService;

    protected ICartComboDishSpecService cartComboDishSpecService;

    protected ICartComboDishService cartComboDishService;

    protected IShopProductSpecService shopProductSpecService;

    protected ICartSideService cartSideService;

    protected CartMapper cartMapper;

    /**
     * 商品数量
     */
    protected int prodNum;

    /**
     * 商品总价，单位元
     */
    protected BigDecimal totalPrice = BigDecimal.ZERO;

    /**
     * 菜品类型，SINGLE：单菜 ，COMBO：套餐， SIDE：配料
     */
    protected String dishType;

    /**
     * 商品信息
     */
    protected ShopProduct shopProduct;

    /**
     * 购物车请求参数
     */
    protected AddCartDTO request;

    /**
     * 购物车处理器
     */
    protected CartHandler cartHandler;


    public void setRequest(AddCartDTO request){
        this.request = request;
    }

    public void setShopProduct(ShopProduct shopProduct) {
        this.shopProduct = shopProduct;
    }



    public CartContext(String dishType){
        init();
        this.dishType  = dishType;
        if(DishType.SINGLE.equalsIgnoreCase(dishType)){
            this.cartHandler = new SingleDishCartHandler(this);
        }else if(DishType.COMBO.equalsIgnoreCase(dishType)){
            this.cartHandler = new ComboDishCartHandler(this);
        }
    }

    private void init(){
        cartProductSpecService = SpringContextUtils.getBean(ICartProductSpecService.class);
        orderProductService = SpringContextUtils.getBean(IOrderProductService.class);
        kryComboGroupDetailService = SpringContextUtils.getBean(IKryComboGroupDetailService.class);
        cartComboDishSpecService = SpringContextUtils.getBean(ICartComboDishSpecService.class);
        cartComboDishService = SpringContextUtils.getBean(ICartComboDishService.class);
        shopProductSpecService = SpringContextUtils.getBean(IShopProductSpecService.class);
        cartMapper = SpringContextUtils.getBean(CartMapper.class);
        cartProductSpecService = SpringContextUtils.getBean(ICartProductSpecService.class);
        cartSideService = SpringContextUtils.getBean(ICartSideService.class);
    }


    /**
     * 新增购物车处理器
     */
    public void handler(){
        cartHandler.handler();
    }

    /**
     * 刷新购物车商品数量
     */
    public void refreshCart(Long cartId,Integer prodNum){
        cartHandler.refreshCart(cartId,prodNum);
    }


    /**
     * 清除购物车
     */
    public void clear(List<Long> ids){
        cartHandler.clear(ids);
    }

    /**
     * 新增购物车响应结果
     * @return
     */
    public AddCartVo response(){
        AddCartVo response = new AddCartVo();
        response.setProdNum(prodNum);
        response.setPrice(totalPrice);
        return response;
    }


}
