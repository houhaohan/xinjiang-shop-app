package com.pinet.rest.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pinet.core.entity.BaseEntity;
import com.pinet.core.exception.PinetException;
import com.pinet.core.util.ThreadLocalUtil;
import com.pinet.rest.entity.*;
import com.pinet.rest.entity.bo.OrderProductBo;
import com.pinet.rest.entity.bo.QueryOrderProductBo;
import com.pinet.rest.mapper.OrderProductMapper;
import com.pinet.rest.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

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

    @Override
    public List<OrderProduct> getByOrderId(Long orderId) {
        LambdaQueryWrapper<OrderProduct> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BaseEntity::getDelFlag, 0).eq(OrderProduct::getOrderId, orderId);

        List<OrderProduct> orderProducts = list(queryWrapper);
        return orderProducts;
    }

    @Override
    public List<OrderProduct> getByCartAndShop(Long shopId) {
        List<OrderProduct> orderProducts = new ArrayList<>();
        Long userId = ThreadLocalUtil.getUserLogin().getUserId();
        List<Cart> cartList = cartService.getByUserIdAndShopId(userId, shopId);
        cartList.forEach(k -> {
            QueryOrderProductBo queryOrderProductBo = new QueryOrderProductBo(k.getShopProdId(),k.getProdNum(),k.getShopProdSpecId());
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
        if (shopProduct.getShopProdStatus() == 2){
            throw new PinetException(shopProduct.getProductName() + "已下架,请重新选择");
        }

        //判断店铺商品是否删除
        if (shopProduct.getDelFlag() == 1){
            throw new PinetException(shopProduct.getProductName() + "已下架,请重新选择");
        }


        orderProduct.setShopProdId(shopProduct.getId());
        orderProduct.setProdName(shopProduct.getProductName());
        orderProduct.setProdNum(queryOrderProductBo.getProdNum());

        //查询具体的样式并且校验
        ShopProductSpec shopProductSpec = shopProductSpecService.getById(queryOrderProductBo.getShopProdSpecId());
        if (shopProductSpec.getStock() < queryOrderProductBo.getProdNum()) {
            throw new PinetException(shopProduct.getProductName() + "库存不足,剩余库存:" + shopProductSpec.getStock());
        }

        orderProduct.setProdSkuId(shopProductSpec.getSkuId());
        orderProduct.setProdUnitPrice(shopProductSpec.getPrice());
        //计算总价
        BigDecimal prodPrice = shopProductSpec.getPrice().multiply(new BigDecimal(queryOrderProductBo.getProdNum())).setScale(2, RoundingMode.DOWN);
        orderProduct.setProdPrice(prodPrice);

        ProductSku productSku =  productSkuService.getById(shopProductSpec.getSkuId());
        orderProduct.setProdSkuName(productSku.getSkuName());
        orderProduct.setShopProdSpecId(queryOrderProductBo.getShopProdSpecId());
        orderProduct.setProdSpecName(shopProductSpec.getSpecName());
        orderProduct.setProdImg(shopProduct.getProductImg());
        return orderProduct;
    }

}
