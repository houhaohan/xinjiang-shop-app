package com.pinet.rest.service.impl;

import cn.hutool.core.convert.Convert;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pinet.core.ApiErrorEnum;
import com.pinet.core.constants.DB;
import com.pinet.core.entity.BaseEntity;
import com.pinet.core.exception.PinetException;
import com.pinet.core.util.BigDecimalUtil;
import com.pinet.core.util.ThreadLocalUtil;
import com.pinet.keruyun.openapi.constants.DishType;
import com.pinet.rest.entity.*;
import com.pinet.rest.entity.dto.AddCartDto;
import com.pinet.rest.entity.dto.CartListDto;
import com.pinet.rest.entity.dto.ClearCartDto;
import com.pinet.rest.entity.dto.EditCartProdNumDto;
import com.pinet.rest.entity.vo.AddCartVo;
import com.pinet.rest.entity.vo.CartComboDishSpecVo;
import com.pinet.rest.entity.vo.CartListVo;
import com.pinet.rest.entity.vo.CartVo;
import com.pinet.rest.mapper.CartMapper;
import com.pinet.rest.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>
 * 购物车 服务实现类
 * </p>
 *
 * @author wlbz
 * @since 2022-12-08
 */
@Service
@DS(DB.MASTER)
public class CartServiceImpl extends ServiceImpl<CartMapper, Cart> implements ICartService {

    @Resource
    private CartMapper cartMapper;

    @Resource
    private IShopService shopService;

    @Resource
    private IShopProductService shopProductService;

    @Resource
    private ICartProductSpecService cartProductSpecService;

    @Resource
    private IShopProductSpecService shopProductSpecService;

    @Autowired
    private IKryComboGroupDetailService kryComboGroupDetailService;

    @Autowired
    private ICartComboDishService cartComboDishService;

    @Autowired
    private ICartComboDishSpecService cartComboDishSpecService;

    @Override
    public List<CartListVo> cartList(CartListDto dto) {
        //判断店铺是否存在  店铺状态
        Shop shop = shopService.getById(dto.getShopId());
        if (shop == null || shop.getDelFlag() == 1) {
            throw new PinetException("店铺不存在");
        }
        List<CartListVo> cartListVos = cartMapper.selectCartList(dto);
        cartListVos.forEach(k -> {
            List<CartProductSpec> cartProductSpecs = null;
            if(DishType.COMBO.equalsIgnoreCase(k.getDishType())){
                List<CartComboDishSpec> cartComboDishSpecs = cartComboDishSpecService.getByCartId(k.getCartId());
                String prodSpecName = cartComboDishSpecs.stream().map(CartComboDishSpec::getShopProdSpecName).collect(Collectors.joining(","));
                k.setProdSpecName(prodSpecName);
                Long unitPrice = kryComboGroupDetailService.getPriceByShopProdId(k.getShopProdId());
                k.setProdPrice(BigDecimalUtil.fenToYuan(unitPrice));
                k.setAllPrice(BigDecimalUtil.multiply(k.getProdPrice(),new BigDecimal(k.getProdNum())));
            }else {
                cartProductSpecs = cartProductSpecService.getByCartId(k.getCartId());
                String prodSpecName = cartProductSpecs.stream().map(CartProductSpec::getShopProdSpecName).collect(Collectors.joining(","));
                k.setProdSpecName(prodSpecName);
                BigDecimal price = cartProductSpecs.stream().map(CartProductSpec::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
                k.setProdPrice(price);
                k.setAllPrice(BigDecimalUtil.multiply(price,new BigDecimal(k.getProdNum())));
            }

        });

        return cartListVos;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public AddCartVo addCart(AddCartDto dto) {
        ShopProduct shopProduct = shopProductService.getById(dto.getShopProdId());
        //校验店铺商品id是否存在
        if (shopProduct == null){
            throw new PinetException("店铺商品不存在");
        }
        dto.setShopId(shopProduct.getShopId());
        Long customerId = dto.getCustomerId();
        int num = 0;
        Long cartId = 0L;
        BigDecimal totalPrice = BigDecimal.ZERO;
        if(DishType.COMBO.equalsIgnoreCase(shopProduct.getDishType())){
            List<Long> shopProdSpecIds = new ArrayList<>();
            for (AddCartDto singleDish : dto.getComboDetails()){
                shopProdSpecIds.addAll(Convert.toList(Long.class, singleDish.getShopProdSpecIds()));
            }
            //查询套餐价格
            Long unitPrice = kryComboGroupDetailService.getPriceByShopProdId(shopProduct.getId());
            List<CartComboDishSpecVo> cartComboDishSpecVos = cartComboDishSpecService.getByUserIdAndShopProdSpecId(customerId, shopProdSpecIds);
            List<Long> shopProdSpecIdList = cartComboDishSpecVos.stream().map(CartComboDishSpecVo::getShopProdSpecId).collect(Collectors.toList());
            boolean allMatch = shopProdSpecIds.stream().allMatch(shopProdSpecIdList::contains);
            if(allMatch){
                //增加数量
                num += 1;
                cartId = cartComboDishSpecVos.get(0).getCartId();
                Cart cart = getById(cartId);
                cart.setProdNum(cart.getProdNum() + dto.getProdNum());
                updateById(cart);
                //套餐价格
                BigDecimal price = BigDecimalUtil.multiply(BigDecimalUtil.fenToYuan(unitPrice), new BigDecimal(cart.getProdNum()));
                totalPrice = BigDecimalUtil.sum(totalPrice,price);
            }else {
                //新增购物车
                num = num + dto.getProdNum();
                Cart cart = new Cart();
                BeanUtils.copyProperties(dto, cart);
                cart.setCartStatus(1);
                cart.setCustomerId(customerId);
                cart.setDishId(shopProduct.getProdId());
                cart.setDishType(shopProduct.getDishType());
                cart.setUnit(shopProduct.getUnit());
                save(cart);
                List<AddCartDto> comboDetails = dto.getComboDetails();
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
                totalPrice = BigDecimalUtil.sum(totalPrice,price);
            }
        }else if(DishType.SINGLE.equalsIgnoreCase(shopProduct.getDishType())){
            //单品
            List<Long> singleProdSpecIds = Convert.toList(Long.class, dto.getShopProdSpecIds());
            //判断存不存在
            List<CartProductSpec> cartProductSpecs = cartProductSpecService.getByUserIdAndShopProdId(customerId, dto.getShopProdId());
            List<Long> dBShopProdSpecIds = cartProductSpecs.stream().map(CartProductSpec::getShopProdSpecId).collect(Collectors.toList());
            boolean allMatch = singleProdSpecIds.stream().allMatch(dBShopProdSpecIds::contains);

            if(allMatch){
                //购物车已存在，新增数量
                num += 1;
                cartId = cartProductSpecs.get(0).getCartId();
                Cart cart = getById(cartId);
                cart.setProdNum(cart.getProdNum()+dto.getProdNum());
                updateById(cart);

                //查询单品价格
                BigDecimal price = shopProductSpecService.getPriceByShopProdId(shopProduct.getId());
                totalPrice = BigDecimalUtil.sum(totalPrice,price);

            }else {
                //购物车不存在，新增购物车
                num += dto.getProdNum();
                Cart cart = new Cart();
                BeanUtils.copyProperties(dto, cart);
                cart.setCartStatus(1);
                cart.setCustomerId(customerId);
                cart.setDishId(shopProduct.getProdId());
                cart.setDishType(shopProduct.getDishType());
                cart.setUnit(shopProduct.getUnit());
                save(cart);

                List<Long> shopProdSpecIds = Convert.toList(Long.class, dto.getShopProdSpecIds());
                for(Long specId : shopProdSpecIds){
                    CartProductSpec cartProductSpec = new CartProductSpec();
                    cartProductSpec.setCartId(cart.getId());
                    cartProductSpec.setShopProdSpecId(specId);
                    ShopProductSpec shopProductSpec = shopProductSpecService.getById(specId);
                    if (shopProductSpec == null) {
                        throw new PinetException("样式不存在");
                    }
                    cartProductSpec.setShopProdSpecName(shopProductSpec.getSpecName());
                    cartProductSpecService.save(cartProductSpec);
                    BigDecimal price = BigDecimalUtil.multiply(shopProductSpec.getPrice(), new BigDecimal(cart.getProdNum()));
                    totalPrice = BigDecimalUtil.sum(totalPrice,price);
                }
            }
        }

        AddCartVo addCartVo = new AddCartVo();
        addCartVo.setProdNum(num);
        addCartVo.setPrice(totalPrice);
        return addCartVo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean editCartProdNum(EditCartProdNumDto dto) {
        Cart cart = getById(dto.getCartId());
        if (cart == null || cart.getDelFlag() == 1) {
            throw new PinetException("购物车不存在");
        }
        if(DishType.SINGLE.equalsIgnoreCase(cart.getDishType())){
            //如果数量为0则删除
            if (dto.getProdNum() == 0) {
                cartProductSpecService.remove(new LambdaQueryWrapper<CartProductSpec>().eq(CartProductSpec::getCartId,dto.getCartId()));
                return removeById(dto.getCartId());
            }
        }else if(DishType.COMBO.equalsIgnoreCase(cart.getDishType())){
            if (dto.getProdNum() == 0) {
                cartComboDishService.deleteByCartId(dto.getCartId());
                cartComboDishSpecService.deleteByCartId(dto.getCartId());
                return true;
            }
        }
        cart.setProdNum(dto.getProdNum());
        return updateById(cart);
    }

    @Override
    public List<Cart> getByUserIdAndShopId(Long userId, Long shopId) {
        LambdaQueryWrapper<Cart> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Cart::getCustomerId, userId).eq(Cart::getShopId, shopId);
        return list(lambdaQueryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delCartByShopId(Long shopId, Long customerId) {
        LambdaQueryWrapper<Cart> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Cart::getShopId, shopId).eq(Cart::getCustomerId, customerId).eq(BaseEntity::getDelFlag, 0);

        List<Cart> cartList = list(lambdaQueryWrapper);

        //删除购物车
        remove(lambdaQueryWrapper);

        //删除cart_product_spec表数据
        List<Long> cartIds = cartList.stream().map(BaseEntity::getId).collect(Collectors.toList());
        LambdaQueryWrapper<CartProductSpec> removeWrapper = new LambdaQueryWrapper<>();
        removeWrapper.in(CartProductSpec::getCartId,cartIds).eq(BaseEntity::getDelFlag,0);
        cartProductSpecService.remove(removeWrapper);

        //删除套餐
        cartComboDishService.deleteByCartIds(cartIds);
        cartComboDishSpecService.deleteByCartIds(cartIds);
    }

    @Override
    public CartVo getCartByUserIdAndShopId(Long shopId, Long customerId) {
        return cartMapper.getCartByUserIdAndShopId(shopId,customerId);
    }

    @Override
    public boolean clearCart(ClearCartDto dto) {
        Long userId = ThreadLocalUtil.getUserLogin().getUserId();
        delCartByShopId(dto.getShopId(),userId);
        return true;
    }


    /**
     * 校验该门店是否有该商品
     * @param shopId
     * @param productId
     * @param productName
     * @return
     */
    private boolean verificationProductIsExist(Long shopId,String productId,String productName){
        QueryWrapper<ShopProduct> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("shop_id",shopId);
        queryWrapper.and(i->i.eq("prod_id",productId).or().eq("product_name",productName));
        return shopProductService.count(queryWrapper) > 0;
    }
}
