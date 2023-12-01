package com.pinet.rest.controller;


import cn.hutool.core.lang.UUID;
import com.alibaba.fastjson.JSONObject;
import com.pinet.core.constants.CommonConstant;
import com.pinet.core.result.Result;
import com.pinet.core.util.LatAndLngUtils;
import com.pinet.core.util.ThreadLocalUtil;
import com.pinet.core.version.ApiVersion;
import com.pinet.inter.annotation.NotTokenSign;
import com.pinet.keruyun.openapi.vo.KryResponse;
import com.pinet.rest.entity.CustomerAddress;
import com.pinet.rest.entity.Shop;
import com.pinet.rest.entity.dto.GetShopIdAndShopProdIdDto;
import com.pinet.rest.entity.param.ShopProductParam;
import com.pinet.rest.entity.vo.CartVo;
import com.pinet.rest.entity.vo.GetShopProdIdByProdIdVo;
import com.pinet.rest.entity.vo.ShopProductListVo;
import com.pinet.rest.entity.vo.ShopProductVo;
import com.pinet.rest.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.pinet.core.controller.BaseController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


/**
 * <p>
 * 店铺商品表 前端控制器
 * </p>
 *
 * @author wlbz
 * @since 2022-12-08
 */
@RestController
@RequestMapping("/{version}/shop/product")
@Api(tags = "商品")
@Slf4j
public class ShopProductController extends BaseController {

    @Autowired
    private IShopProductService shopProductService;

    @Autowired
    private IShopService shopService;

    @Autowired
    private ICartService cartService;

    @Autowired
    private ICustomerAddressService customerAddressService;

    @Resource
    private IShopBrowseLogService shopBrowseLogService;

    /**
     * 商品列表，经纬度默认是杭州滨江的经纬度
     * @param shopId 店铺Id
     * @param lat 纬度
     * @param lng 经度
     * @return
     */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @NotTokenSign
    @ApiOperation("商品列表")
    @ApiVersion(1)
    public Result<ShopProductListVo> list(@RequestParam(value = "shopId",required = false) Long shopId,
                                          @RequestParam(value = "lat",defaultValue = CommonConstant.DEFAULT_LAT) BigDecimal lat,
                                          @RequestParam(value = "lng",defaultValue = CommonConstant.DEFAULT_LNG) BigDecimal lng){
      log.info("商品列表参数:shopId={},lat={},lng={}",shopId,lat,lng);

        if(shopId == null){
            shopId = shopService.getMinDistanceShop(lat, lng);
            if(shopId == null){
                return Result.ok();
            }
        }
        Shop shop = shopService.getById(shopId);
        Long userId = ThreadLocalUtil.getUserLogin().getUserId();
        ShopProductListVo result = shopProductService.productListByShopId(shopId);
        if(result != null){
            double distance =  LatAndLngUtils.getDistance(lng.doubleValue(),lat.doubleValue(),
                    Double.parseDouble(shop.getLng()),Double.parseDouble(shop.getLat()));
            result.setDistance(BigDecimal.valueOf(distance));
            //当前用户在这个店铺加的购物车

            if(userId != null && userId != 0){
                CartVo cartVo = cartService.getCartByUserIdAndShopId(shopId, userId);
                result.setTotalPrice(cartVo.getPrice());
                result.setProdNum(cartVo.getProdNum());
            }
            CustomerAddress defaultAddress = customerAddressService.getDefaultAddress(userId);
            result.setDefaultAddress(defaultAddress);
            shop.setDistance(distance);
            result.setShopInfo(shop);
        }

        //判断下已登录的用户插入店铺浏览记录表
        Long customerId = ThreadLocalUtil.getUserLogin().getUserId();
        if (customerId != null && customerId > 0){
            shopBrowseLogService.addBrowseLog(shopId,customerId);
        }
        return Result.ok(result);
    }


    @RequestMapping(value = "/search",method = RequestMethod.GET)
    @NotTokenSign
    @ApiOperation(("店铺商品搜索"))
    @ApiVersion(1)
    public Result<List<ShopProductVo>> search(ShopProductParam param){
        List<ShopProductVo> list = shopProductService.search(param);
        return Result.ok(list);
    }

    @RequestMapping(value = "/sellwell",method = RequestMethod.GET)
    @NotTokenSign
    @ApiOperation("店铺畅销商品")
    @ApiVersion(1)
    public Result<List<String>> sellwell(@RequestParam Long shopId){
        List<String> list = shopProductService.sellwell(shopId);
        return Result.ok(list);
    }

    /**
     * 商品详情
     * @param id 商品ID
     * @return
     */
    @RequestMapping(value = "/getById",method = RequestMethod.GET)
    @NotTokenSign
    @ApiOperation("商品详情")
    @ApiVersion(1)
    public Result<ShopProductVo> getById(@RequestParam Long id){
        ShopProductVo shopProductVo = shopProductService.getDetailById(id);
        return Result.ok(shopProductVo);
    }

    @PostMapping("/getShopIdAndShopProdId")
    @ApiVersion(1)
    @ApiOperation("根据商品id获取店铺id和店铺商品id")
    @NotTokenSign
    public Result<GetShopProdIdByProdIdVo> getShopIdAndShopProdId(@Validated @RequestBody GetShopIdAndShopProdIdDto dto){
        GetShopProdIdByProdIdVo getShopProdIdByProdIdVo = shopProductService.getShopIdAndShopProdId(dto);
        return Result.ok(getShopProdIdByProdIdVo);
    }

    @PostMapping("/brandDishOpenMsg")
    @ApiOperation("品牌商品开放消息")
    @ApiVersion(1)
    @NotTokenSign
    public KryResponse brandDishOpenMsg(@RequestParam(required = false) String validate, @RequestBody Map<String,Object> map){
        System.out.println("品牌商品开放消息");
        System.out.println(JSONObject.toJSONString(map));

        KryResponse response = new KryResponse();
        response.setMessage("成功[OK]");
        response.setMessageUuid(UUID.randomUUID().toString());
        if("success".equals(validate)){
            response.setCode(0);
            return response;
        }
        response.setCode(0);
        return response;
    }

    @PostMapping("/shopDishOpenMsg")
    @ApiOperation("门店商品开放消息")
    @ApiVersion(1)
    @NotTokenSign
    public KryResponse shopDishOpenMsg(@RequestParam(required = false) String validate, @RequestBody Map<String,Object> map){
        System.out.println("门店商品开放消息");
        System.out.println(JSONObject.toJSONString(map));

        KryResponse response = new KryResponse();
        response.setMessage("成功[OK]");
        response.setMessageUuid(UUID.randomUUID().toString());
        if("success".equals(validate)){
            response.setCode(0);
            return response;
        }
        response.setCode(-1);
        return response;
    }

    @PostMapping("/updateDishStock")
    @ApiOperation("商品库存信息变更")
    @ApiVersion(1)
    @NotTokenSign
    public KryResponse updateDishStock(@RequestParam(required = false) String validate, @RequestBody Map<String,Object> map){
        System.out.println("门店商品开放消息");
        System.out.println(JSONObject.toJSONString(map));

        KryResponse response = new KryResponse();
        response.setMessage("成功[OK]");
        response.setMessageUuid(UUID.randomUUID().toString());
        if("success".equals(validate)){
            response.setCode(0);
            return response;
        }
        response.setCode(-1);
        return response;
    }

}
