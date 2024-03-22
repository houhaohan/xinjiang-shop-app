package com.pinet.rest.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pinet.core.constants.OrderConstant;
import com.pinet.core.exception.PinetException;
import com.pinet.core.util.BigDecimalUtil;
import com.pinet.core.util.ThreadLocalUtil;
import com.pinet.keruyun.openapi.constants.DishType;
import com.pinet.rest.entity.*;
import com.pinet.rest.entity.bo.QueryOrderProductBo;
import com.pinet.rest.entity.dto.OrderComboDishDto;
import com.pinet.rest.entity.dto.OrderComboDishSpecDto;
import com.pinet.rest.entity.dto.OrderProductDto;
import com.pinet.rest.entity.enums.CartStatusEnum;
import com.pinet.rest.entity.enums.OrderTypeEnum;
import com.pinet.rest.entity.enums.ShopProdStatusEnum;
import com.pinet.rest.entity.vo.ComboDishSpecVo;
import com.pinet.rest.entity.vo.OrderProductSpecVo;
import com.pinet.rest.entity.vo.OrderProductVo;
import com.pinet.rest.mapper.OrderProductMapper;
import com.pinet.rest.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 订单商品表 服务实现类
 * </p>
 *
 * @author wlbz
 * @since 2022-12-08
 */
@Service
public class OrderProductServiceImpl extends ServiceImpl<OrderProductMapper, OrderProduct> implements IOrderProductService {
    @Resource
    private OrderProductMapper orderProductMapper;

    @Resource
    private ICartService cartService;

    @Resource
    private IShopProductService shopProductService;

    @Resource
    private IShopProductSpecService shopProductSpecService;

    @Resource
    private IProductSkuService productSkuService;

    @Resource
    private ICartProductSpecService cartProductSpecService;

    @Resource
    private IKryComboGroupDetailService kryComboGroupDetailService;

    @Autowired
    private IKryComboGroupService kryComboGroupService;

    @Autowired
    private ICartComboDishService cartComboDishService;

    @Autowired
    private ICartComboDishSpecService cartComboDishSpecService;


    @Override
    public List<OrderProduct> getByOrderId(Long orderId) {
        List<OrderProduct> orderProducts = orderProductMapper.selectByOrderId(orderId);
        orderProducts.forEach(k -> k.setOrderProductSpecStr(k.getOrderProductSpecs().stream().map(OrderProductSpec::getProdSpecName).collect(Collectors.joining(","))));
        return orderProducts;
    }

    @Override
    public List<OrderProduct> getComboByOrderId(Long orderId) {
        List<OrderProduct> orderProducts = orderProductMapper.getComboByOrderId(orderId);
        for(OrderProduct orderProduct : orderProducts){
            String orderProductSpecStr = orderProduct.getComboDishDetails().stream().map(ComboDishSpecVo::getSingleDishName).collect(Collectors.joining(","));
            orderProduct.setOrderProductSpecStr(orderProductSpecStr);
        }
        return orderProducts;
    }

    @Override
    public List<OrderProduct> getProductByOrderId(Long orderId) {

        return null;
    }

    @Override
    public List<OrderProduct> getByCartAndShop(Long shopId, Integer orderType) {
        List<OrderProduct> orderProducts = new ArrayList<>();
//        Long userId = ThreadLocalUtil.getUserLogin().getUserId();
//        List<Cart> cartList = cartService.getByUserIdAndShopId(userId, shopId);
//        if (CollectionUtils.isEmpty(cartList)) {
//            throw new PinetException("购物车内没有需要结算的商品");
//        }
//        cartList.forEach(k -> {
//            if (Objects.equals(k.getCartStatus(), CartStatusEnum.EXPIRE.getCode())) {
//                throw new PinetException("购物车内有失效的商品,请删除后在结算");
//            }
//            if (DishType.COMBO.equalsIgnoreCase(k.getDishType())) {
//                OrderProduct comboOrderItemInfo = getComboOrderItemInfo(k, orderType);
//                orderProducts.add(comboOrderItemInfo);
//            } else {
//                List<CartProductSpec> cartProductSpecs = cartProductSpecService.getByCartId(k.getId());
//                List<Long> shopProdSpecIds = cartProductSpecs.stream().map(CartProductSpec::getShopProdSpecId).collect(Collectors.toList());
//                QueryOrderProductBo queryOrderProductBo = new QueryOrderProductBo(k.getShopProdId(), k.getProdNum(), shopProdSpecIds, orderType,null);
//                OrderProduct orderProduct = this.getByQueryOrderProductBo(queryOrderProductBo);
//                orderProducts.add(orderProduct);
//            }
//        });
        return orderProducts;
    }

    /**
     * 获取购物车套餐明细
     *
     * @param
     * @return
     */
    public OrderProduct getComboOrderItemInfo(Cart cart, Integer orderType) {
        ShopProduct shopProduct = shopProductService.getById(cart.getShopProdId());
        OrderProduct orderProductVo = new OrderProduct();
        orderProductVo.setShopProdId(cart.getShopProdId());
        orderProductVo.setDishId(cart.getDishId());
        orderProductVo.setProdName(shopProduct.getProductName());
        orderProductVo.setUnit(shopProduct.getUnit());
        Long unitPrice = kryComboGroupDetailService.getPriceByShopProdId(shopProduct.getId());
        orderProductVo.setProdUnitPrice(BigDecimalUtil.fenToYuan(unitPrice));
        orderProductVo.setProdNum(cart.getProdNum());
        orderProductVo.setProdPrice(BigDecimalUtil.multiply(orderProductVo.getProdUnitPrice(), new BigDecimal(orderProductVo.getProdNum())));
        orderProductVo.setProdImg(shopProduct.getProductImg());
        if (orderType.equals(OrderTypeEnum.TAKEAWAY.getCode())) {
            orderProductVo.setPackageFee(BigDecimalUtil.multiply(OrderConstant.COMBO_PACKAGE_FEE, cart.getProdNum(), RoundingMode.HALF_UP));
        }

        StringBuffer sb = new StringBuffer();
        List<ComboDishSpecVo> list = new ArrayList<>();
        List<CartComboDish> cartComboDishList = cartComboDishService.getByCartId(cart.getId());
        for (CartComboDish cartComboDish : cartComboDishList) {
            ComboDishSpecVo comboDishSpecVo = new ComboDishSpecVo();
            comboDishSpecVo.setShopProdId(shopProduct.getId());
            comboDishSpecVo.setSingleDishId(cartComboDish.getShopProdId());
            ShopProduct singleShopProduct = shopProductService.getById(cartComboDish.getShopProdId());
            comboDishSpecVo.setSingleDishName(singleShopProduct.getProductName());
            List<CartComboDishSpec> cartComboDishSpecs = cartComboDishSpecService.getByCartIdAndProdId(cart.getId(), cartComboDish.getShopProdId());

            List<OrderProductSpecVo> orderProductSpecVoList = new ArrayList<>();
            for (CartComboDishSpec cartComboDishSpec : cartComboDishSpecs) {
                OrderProductSpecVo orderProductSpecVo = new OrderProductSpecVo();
                ShopProductSpec shopProductSpec = shopProductSpecService.getById(cartComboDishSpec.getShopProdSpecId());
                orderProductSpecVo.setProdSkuId(shopProductSpec.getSkuId());
                orderProductSpecVo.setProdSkuName(shopProductSpec.getSkuName());
                orderProductSpecVo.setProdSpecName(cartComboDishSpec.getShopProdSpecName());
                orderProductSpecVo.setShopProdSpecId(cartComboDishSpec.getShopProdSpecId());
                orderProductSpecVoList.add(orderProductSpecVo);
            }
            comboDishSpecVo.setOrderProductSpecs(orderProductSpecVoList);
            list.add(comboDishSpecVo);
            sb.append(singleShopProduct.getProductName()).append(",");
        }
        orderProductVo.setOrderProductSpecStr(sb.substring(0,sb.length()-1));
        orderProductVo.setComboDishDetails(list);
        return orderProductVo;
    }


    @Override
    public OrderProduct getByQueryOrderProductBo(QueryOrderProductBo queryOrderProductBo) {
        OrderProduct orderProduct = new OrderProduct();
//        ShopProduct shopProduct = shopProductService.getById(queryOrderProductBo.getShopProdId());
//
//        //判断店铺商品是否下架
//        if (Objects.equals(shopProduct.getShopProdStatus(), ShopProdStatusEnum.OFF_SHELF.getCode())) {
//            throw new PinetException(shopProduct.getProductName() + "已下架,请重新选择");
//        }
//
//        //判断店铺商品是否删除
//        if (shopProduct.getDelFlag() == 1) {
//            throw new PinetException(shopProduct.getProductName() + "已下架,请重新选择");
//        }
//
//        //设置打包费   //自提没有打包费
//        if (queryOrderProductBo.getOrderType().equals(OrderTypeEnum.TAKEAWAY.getCode())) {
//            orderProduct.setPackageFee(BigDecimalUtil.multiply(OrderConstant.SINGLE_PACKAGE_FEE, queryOrderProductBo.getProdNum(), RoundingMode.HALF_UP));
//        }
//        orderProduct.setDishId(shopProduct.getProdId());
//        orderProduct.setShopProdId(shopProduct.getId());
//        orderProduct.setProdName(shopProduct.getProductName());
//        orderProduct.setProdNum(queryOrderProductBo.getProdNum());
//        orderProduct.setUnit(shopProduct.getUnit());
//        //orderProduct.setDishType(shopProduct.getDishType());
//        //单价
//        BigDecimal unitPrice = BigDecimal.ZERO;
//        List<ComboDishSpecVo> orderComboDishList = new ArrayList<>();
//        if(!CollectionUtils.isEmpty(queryOrderProductBo.getOrderComboDishList())){
//            for(OrderComboDishDto orderComboDishDto : queryOrderProductBo.getOrderComboDishList()){
//                ComboDishSpecVo comboDishSpecVo = new ComboDishSpecVo();
//                comboDishSpecVo.setShopProdId(orderComboDishDto.getShopProdId());
//                comboDishSpecVo.setSingleDishId(orderComboDishDto.getSingleProdId());
//                ShopProduct singleProduct = shopProductService.getById(orderComboDishDto.getSingleProdId());
//                comboDishSpecVo.setSingleDishName(singleProduct.getProductName());
//
//                List<OrderProductSpecVo> orderProductSpecs = new ArrayList<>();
//                for(OrderComboDishSpecDto orderComboDishSpecDto : orderComboDishDto.getOrderComboDishSpecList()) {
//                    OrderProductSpecVo orderProductSpecVo = new OrderProductSpecVo();
//                    ShopProductSpec singleSpec = shopProductSpecService.getById(orderComboDishSpecDto.getShopProdSpecId());
//                    orderProductSpecVo.setShopProdSpecId(orderComboDishSpecDto.getShopProdSpecId());
//                    orderProductSpecVo.setProdSpecName(orderComboDishSpecDto.getShopProdSpecName());
//                    orderProductSpecVo.setProdSkuId(singleSpec.getSkuId());
//                    orderProductSpecVo.setProdSkuName(singleSpec.getSkuName());
//                    orderProductSpecs.add(orderProductSpecVo);
//                }
//                comboDishSpecVo.setOrderProductSpecs(orderProductSpecs);
//                orderComboDishList.add(comboDishSpecVo);
//            }
//            Long price = kryComboGroupDetailService.getPriceByShopProdId(orderProduct.getShopProdId());
//            unitPrice = BigDecimalUtil.fenToYuan(price);
//        }
//        orderProduct.setComboDishDetails(orderComboDishList);
//
//        List<OrderProductSpec> orderProductSpecs = new ArrayList<>();
//
//        if(!CollectionUtils.isEmpty(queryOrderProductBo.getShopProdSpecIds())){
//            for (Long shopProdSpecId : queryOrderProductBo.getShopProdSpecIds()) {
//                //查询具体的样式并且校验
//                OrderProductSpec orderProductSpec = new OrderProductSpec();
//                if (DishType.COMBO.equalsIgnoreCase(shopProduct.getDishType())) {
//                    KryComboGroupDetail kryComboGroupDetail = kryComboGroupDetailService.getById(shopProdSpecId);
//                    unitPrice = unitPrice.add(BigDecimalUtil.fenToYuan(kryComboGroupDetail.getPrice()));
//
//                    KryComboGroup kryComboGroup = kryComboGroupService.getById(kryComboGroupDetail.getComboGroupId());
//                    orderProductSpec.setProdSkuId(kryComboGroup.getId());
//                    orderProductSpec.setProdSkuName(kryComboGroup.getGroupName());
//                    orderProductSpec.setShopProdSpecId(shopProdSpecId);
//                    orderProductSpec.setProdSpecName(kryComboGroupDetail.getDishName());
//                    orderProductSpecs.add(orderProductSpec);
//                } else {
//                    ShopProductSpec shopProductSpec = shopProductSpecService.getById(shopProdSpecId);
//                    if (shopProductSpec.getStock() < queryOrderProductBo.getProdNum()) {
//                        throw new PinetException(shopProduct.getProductName() + ":" + shopProductSpec.getSpecName() + "库存不足,剩余库存:" + shopProductSpec.getStock());
//                    }
//                    unitPrice = BigDecimalUtil.sum(unitPrice, shopProductSpec.getPrice());
//                    ProductSku productSku = productSkuService.getById(shopProductSpec.getSkuId());
//                    orderProductSpec.setProdSkuId(shopProductSpec.getSkuId());
//                    orderProductSpec.setProdSkuName(productSku.getSkuName());
//                    orderProductSpec.setShopProdSpecId(shopProdSpecId);
//                    orderProductSpec.setProdSpecName(shopProductSpec.getSpecName());
//                    orderProductSpecs.add(orderProductSpec);
//                }
//            }
//        }
//
//        orderProduct.setOrderProductSpecs(orderProductSpecs);
//        orderProduct.setOrderProductSpecStr(orderProductSpecs.stream().map(OrderProductSpec::getProdSpecName).collect(Collectors.joining(",")));
//        orderProduct.setProdUnitPrice(unitPrice);
//        //计算总价
//        BigDecimal prodPrice = BigDecimalUtil.multiply(unitPrice, queryOrderProductBo.getProdNum(), RoundingMode.DOWN);
//        orderProduct.setProdPrice(prodPrice);
//        orderProduct.setProdImg(shopProduct.getProductImg());
        return orderProduct;
    }

    @Override
    public List<OrderProductDto> selectByOrderId(Long orderId) {
        return orderProductMapper.getByOrderId(orderId);
    }

}
