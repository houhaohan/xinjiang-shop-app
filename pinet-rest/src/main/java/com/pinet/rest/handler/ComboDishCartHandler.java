package com.pinet.rest.handler;

import cn.hutool.core.convert.Convert;
import com.pinet.core.exception.PinetException;
import com.pinet.core.util.BigDecimalUtil;
import com.pinet.rest.entity.*;
import com.pinet.rest.entity.bo.QueryOrderProductBo;
import com.pinet.rest.entity.dto.AddCartDto;
import com.pinet.rest.entity.vo.AddCartVo;
import com.pinet.rest.entity.vo.CartComboDishSpecVo;
import com.pinet.rest.mapper.CartMapper;
import com.pinet.rest.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description: xinjiang-shop-mini
 * @author: chengshuanghui
 * @date: 2024-03-18 14:21
 */
public class ComboDishCartHandler extends DishCartHandler{

    public ComboDishCartHandler(CartContext context){
        this.context = context;
    }

    @Autowired
    private ICartProductSpecService cartProductSpecService;

    @Autowired
    private IOrderProductService orderProductService;

    @Autowired
    private IKryComboGroupDetailService kryComboGroupDetailService;

    @Autowired
    private ICartComboDishSpecService cartComboDishSpecService;

    @Autowired
    private ICartComboDishService cartComboDishService;

    @Autowired
    private IShopProductSpecService shopProductSpecService;

    @Autowired
    private CartMapper cartMapper;


    @Override
    public OrderProduct getOrderProductByCartId(Long cartId, Long shopProdId, Integer prodNum, Integer orderType) {
        //套餐
        List<CartProductSpec> cartProductSpecs = cartProductSpecService.getComboByCartId(cartId);
        List<Long> shopProdSpecIds = cartProductSpecs.stream().map(CartProductSpec::getShopProdSpecId).collect(Collectors.toList());
        QueryOrderProductBo queryOrderProductBo = new QueryOrderProductBo(shopProdId, prodNum, shopProdSpecIds,orderType);
        return orderProductService.getByQueryOrderProductBo(queryOrderProductBo);
     }


    /**
     * 新增购物车
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void handler() {
        List<Long> shopProdSpecIds = new ArrayList<>();
        for (AddCartDto singleDish : context.request.getComboDetails()){
            shopProdSpecIds.addAll(Convert.toList(Long.class, singleDish.getShopProdSpecIds()));
        }
        //查询套餐价格
        Long unitPrice = kryComboGroupDetailService.getPriceByShopProdId(context.request.getShopProdId());

        List<CartComboDishSpecVo> cartComboDishSpecVos = cartComboDishSpecService.getByUserIdAndShopProdSpecId(context.customerId, shopProdSpecIds);
        List<Long> shopProdSpecIdDBs = cartComboDishSpecVos.stream().map(CartComboDishSpecVo::getShopProdSpecId).collect(Collectors.toList());
        boolean allMatch = shopProdSpecIds.stream().allMatch(shopProdSpecIdDBs::contains);
        if(allMatch){
            //增加数量
            context.prodNum ++;
            Long cartId = cartComboDishSpecVos.get(0).getCartId();
            Cart cart = cartMapper.selectById(cartId);
            cart.setProdNum(cart.getProdNum() + context.request.getProdNum());
            cartMapper.updateById(cart);
            //套餐价格
            BigDecimal price = BigDecimalUtil.multiply(BigDecimalUtil.fenToYuan(unitPrice), new BigDecimal(cart.getProdNum()));
            context.totalPrice = BigDecimalUtil.sum(context.totalPrice,price);
            return;
        }


        //新增购物车
        context.prodNum += context.request.getProdNum();
        Cart cart = buildCartInfo();
        cartMapper.insert(cart);
        List<AddCartDto> comboDetails = context.request.getComboDetails();
        for(AddCartDto singleDish : comboDetails){
            CartComboDish cartComboDish = new CartComboDish();
            cartComboDish.setCartId(cart.getId());
            cartComboDish.setShopProdId(singleDish.getShopProdId());
            cartComboDishService.save(cartComboDish);
            List<Long> singleProdSpecIds = Convert.toList(Long.class, singleDish.getShopProdSpecIds());
            for(Long shopProdSpecId : singleProdSpecIds){

                CartComboDishSpec cartComboDishSpec = new CartComboDishSpec();
                cartComboDishSpec.setCartId(cart.getId());
                cartComboDishSpec.setShopProdId(singleDish.getShopProdId());
                cartComboDishSpec.setShopProdSpecId(shopProdSpecId);
                ShopProductSpec shopProductSpec = shopProductSpecService.getById(shopProdSpecId);
                if (shopProductSpec == null) {
                    throw new PinetException("样式不存在");
                }
                cartComboDishSpec.setShopProdSpecName(shopProductSpec.getSpecName());
                cartComboDishSpecService.save(cartComboDishSpec);
            }
        }

        BigDecimal price = BigDecimalUtil.multiply(BigDecimalUtil.fenToYuan(unitPrice), new BigDecimal(cart.getProdNum()));
        context.totalPrice = BigDecimalUtil.sum(context.totalPrice,price);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void refreshCart(Cart cart,Integer prodNum) {
        if (prodNum > 0) {
            cart.setProdNum(prodNum);
            cartMapper.updateById(cart);
        }
        cartComboDishService.deleteByCartId(cart.getId().intValue());
        cartComboDishSpecService.deleteByCartId(cart.getId().intValue());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void clear(List<Long> ids) {
        if(CollectionUtils.isEmpty(ids)){
            return;
        }
        cartComboDishService.deleteByCartIds(ids);
        cartComboDishSpecService.deleteByCartIds(ids);
    }

}
