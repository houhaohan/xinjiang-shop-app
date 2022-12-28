package com.pinet.rest.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pinet.core.result.Result;
import com.pinet.core.util.LatAndLngUtils;
import com.pinet.core.version.ApiVersion;
import com.pinet.inter.annotation.NotTokenSign;
import com.pinet.rest.entity.Shop;
import com.pinet.rest.entity.param.ShopProductParam;
import com.pinet.rest.entity.vo.ShopProductListVo;
import com.pinet.rest.entity.vo.ShopProductVo;
import com.pinet.rest.service.IShopProductService;
import com.pinet.rest.service.IShopService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.pinet.core.controller.BaseController;
import java.math.BigDecimal;
import java.util.List;


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
public class ShopProductController extends BaseController {

    @Autowired
    private IShopProductService shopProductService;
    @Autowired
    private IShopService shopService;


    /**
     * 商品列表
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
                                                @RequestParam(value = "lat",defaultValue = "30") BigDecimal lat,
                                                @RequestParam(value = "lng",defaultValue = "120") BigDecimal lng){
        if(shopId == null && lat == null && lng == null){
            return Result.error("参数不能为空");
        }
        if(shopId == null){
            shopId = shopService.getMinDistanceShop(lat, lng);
            if(shopId == null){
                return Result.ok();
            }
        }
        Shop shop = shopService.getById(shopId);
        ShopProductListVo result = shopProductService.productListByShopId(shopId);
        if(result != null){
            double distance =  LatAndLngUtils.getDistance(lng.doubleValue(),lat.doubleValue(),
                    Double.parseDouble(shop.getLng()),Double.parseDouble(shop.getLat()));
            result.setDistance(BigDecimal.valueOf(distance));
        }
        return Result.ok(result);
    }


    @RequestMapping(value = "/search",method = RequestMethod.GET)
    @NotTokenSign
    @ApiOperation(("店铺商品搜索"))
    @ApiVersion(1)
    public Result<List<ShopProductVo>> search(ShopProductParam param){
        List<ShopProductVo> page = shopProductService.search(param);
        return Result.ok(page);
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

}
