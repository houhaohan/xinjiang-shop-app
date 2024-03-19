package com.pinet.rest.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pinet.core.constants.CommonConstant;
import com.pinet.core.constants.OrderPackageFeeConstant;
import com.pinet.core.exception.PinetException;
import com.pinet.core.util.BigDecimalUtil;
import com.pinet.core.util.ThreadLocalUtil;
import com.pinet.keruyun.openapi.constants.DishType;
import com.pinet.rest.entity.*;
import com.pinet.rest.entity.bo.QueryOrderProductBo;
import com.pinet.rest.entity.dto.OrderProductDto;
import com.pinet.rest.entity.enums.OrderTypeEnum;
import com.pinet.rest.entity.enums.ShopProdStatusEnum;
import com.pinet.rest.handler.OrderContext;
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
    private ICartComboDishSpecService cartComboDishSpecService;

    @Override
    public List<OrderProduct> getByOrderId(Long orderId) {
        List<OrderProduct> orderProducts = orderProductMapper.selectByOrderId(orderId);
        orderProducts.forEach(k -> k.setOrderProductSpecStr(k.getOrderProductSpecs().stream().map(OrderProductSpec::getProdSpecName).collect(Collectors.joining(","))));
        return orderProducts;
    }

    @Override
    public List<OrderProduct> getByCartAndShop(Long shopId,Integer orderType) {
        List<OrderProduct> orderProducts = new ArrayList<>();
        Long userId = ThreadLocalUtil.getUserLogin().getUserId();
        List<Cart> cartList = cartService.getByUserIdAndShopId(userId, shopId);
        if (CollectionUtils.isEmpty(cartList)){
            throw new PinetException("购物车内没有需要结算的商品");
        }
        cartList.forEach(k -> {
            if (k.getCartStatus() == 2) {
                throw new PinetException("购物车内有失效的商品,请删除后在结算");
            }
            //查询购物车商品样式
            List<Long> shopProdSpecIds;
            if(DishType.COMBO.equalsIgnoreCase(k.getDishType())){
                //套餐
                List<KryComboGroupDetail> kryComboGroupDetailList = kryComboGroupDetailService.getByShopProdId(k.getShopProdId());
                shopProdSpecIds = kryComboGroupDetailList.stream().map(KryComboGroupDetail::getId).collect(Collectors.toList());
            }else {
                List<CartProductSpec> cartProductSpecs = cartProductSpecService.getByCartId(k.getId());
                shopProdSpecIds = cartProductSpecs.stream().map(CartProductSpec::getShopProdSpecId).collect(Collectors.toList());
            }
            QueryOrderProductBo queryOrderProductBo = new QueryOrderProductBo(k.getShopProdId(), k.getProdNum(), shopProdSpecIds,orderType);
            OrderProduct orderProduct = this.getByQueryOrderProductBo(queryOrderProductBo);
            orderProducts.add(orderProduct);
        });
        return orderProducts;
    }

    @Override
    public OrderProduct getByQueryOrderProductBo(QueryOrderProductBo queryOrderProductBo) {
        OrderProduct orderProduct = new OrderProduct();
        ShopProduct shopProduct = shopProductService.getById(queryOrderProductBo.getShopProdId());

        //判断店铺商品是否下架
        if (Objects.equals(shopProduct.getShopProdStatus(),ShopProdStatusEnum.OFF_SHELF.getCode())) {
            throw new PinetException(shopProduct.getProductName() + "已下架,请重新选择");
        }

        //判断店铺商品是否删除
        if (shopProduct.getDelFlag() == 1) {
            throw new PinetException(shopProduct.getProductName() + "已下架,请重新选择");
        }

        //设置打包费   //自提没有打包费
        if (queryOrderProductBo.getOrderType().equals(OrderTypeEnum.TAKEAWAY.getCode())){
            BigDecimal packageFee = OrderPackageFeeConstant.packageFee(shopProduct.getDishType());
            orderProduct.setPackageFee(BigDecimalUtil.multiply(packageFee, queryOrderProductBo.getProdNum(),RoundingMode.HALF_UP));
        }
        orderProduct.setDishId(shopProduct.getProdId());
        orderProduct.setShopProdId(shopProduct.getId());
        orderProduct.setProdName(shopProduct.getProductName());
        orderProduct.setProdNum(queryOrderProductBo.getProdNum());
        orderProduct.setUnit(shopProduct.getUnit());

        List<OrderProductSpec> orderProductSpecs = new ArrayList<>();
        //单价
        BigDecimal unitPrice = BigDecimal.ZERO;
        for (Long shopProdSpecId : queryOrderProductBo.getShopProdSpecIds()) {
            //查询具体的样式并且校验
            OrderProductSpec orderProductSpec = new OrderProductSpec();
            if("COMBO".equalsIgnoreCase(shopProduct.getDishType())){
                KryComboGroupDetail kryComboGroupDetail = kryComboGroupDetailService.getById(shopProdSpecId);
                unitPrice = unitPrice.add(BigDecimalUtil.fenToYuan(kryComboGroupDetail.getPrice()));

                KryComboGroup kryComboGroup = kryComboGroupService.getById(kryComboGroupDetail.getComboGroupId());
                orderProductSpec.setProdSkuId(kryComboGroup.getId());
                orderProductSpec.setProdSkuName(kryComboGroup.getGroupName());
                orderProductSpec.setShopProdSpecId(shopProdSpecId);
                orderProductSpec.setProdSpecName(kryComboGroupDetail.getDishName());
                orderProductSpecs.add(orderProductSpec);
            }else {
                ShopProductSpec shopProductSpec = shopProductSpecService.getById(shopProdSpecId);
                if (shopProductSpec.getStock() < queryOrderProductBo.getProdNum()) {
                    throw new PinetException(shopProduct.getProductName() + ":" + shopProductSpec.getSpecName() + "库存不足,剩余库存:" + shopProductSpec.getStock());
                }
                unitPrice = BigDecimalUtil.sum(unitPrice,shopProductSpec.getPrice());
                ProductSku productSku = productSkuService.getById(shopProductSpec.getSkuId());
                orderProductSpec.setProdSkuId(shopProductSpec.getSkuId());
                orderProductSpec.setProdSkuName(productSku.getSkuName());
                orderProductSpec.setShopProdSpecId(shopProdSpecId);
                orderProductSpec.setProdSpecName(shopProductSpec.getSpecName());
                orderProductSpecs.add(orderProductSpec);
            }
        }
        orderProduct.setOrderProductSpecs(orderProductSpecs);
        orderProduct.setOrderProductSpecStr(orderProductSpecs.stream().map(OrderProductSpec::getProdSpecName).collect(Collectors.joining(",")));
        orderProduct.setProdUnitPrice(unitPrice);
        //计算总价
        BigDecimal prodPrice = BigDecimalUtil.multiply(unitPrice,queryOrderProductBo.getProdNum(),RoundingMode.DOWN);
        orderProduct.setProdPrice(prodPrice);
        orderProduct.setProdImg(shopProduct.getProductImg());
        return orderProduct;
    }

    @Override
    public List<OrderProductDto> selectByOrderId(Long orderId) {
        return orderProductMapper.getByOrderId(orderId);
    }

}
