package com.pinet.rest.handler;

import com.pinet.core.util.SpringContextUtils;
import com.pinet.keruyun.openapi.constants.DishType;
import com.pinet.rest.entity.Cart;
import com.pinet.rest.entity.ShopProduct;
import com.pinet.rest.entity.dto.AddCartDto;
import com.pinet.rest.entity.vo.AddCartVo;
import com.pinet.rest.mapper.CartMapper;
import com.pinet.rest.service.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

public class CartContext {
    protected ICartProductSpecService cartProductSpecService;

    protected IOrderProductService orderProductService;

    protected IKryComboGroupDetailService kryComboGroupDetailService;

    protected ICartComboDishSpecService cartComboDishSpecService;

    protected ICartComboDishService cartComboDishService;

    protected IShopProductSpecService shopProductSpecService;

    protected CartMapper cartMapper;

    /**
     * 用户 ID
     */
    protected Long customerId;

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
    protected AddCartDto request;

    /**
     * 购物车处理器
     */
    protected CartHandler cartHandler;


    public void setRequest(AddCartDto request){
        this.request = request;
    }

    public void setCustomerId(Long customerId){
        this.customerId = customerId;
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
    };


    /**
     * 新增购物车处理器
     */
    public void handler(){
        cartHandler.handler();
    };

    /**
     * 刷新购物车商品数量
     */
    public void refreshCart(Cart cart,Integer prodNum){
        cartHandler.refreshCart(cart,prodNum);
    };


    /**
     * 清除购物车
     */
    public void clear(List<Long> ids){
        cartHandler.clear(ids);
    };

    /**
     * 新增购物车响应结果
     * @return
     */
    public AddCartVo response(){
        AddCartVo response = new AddCartVo();
        response.setProdNum(prodNum);
        response.setPrice(totalPrice);
        return response;
    };


}
