package com.pinet.rest.controller;


import com.pinet.core.result.Result;
import com.pinet.core.version.ApiVersion;
import com.pinet.inter.annotation.NotTokenSign;
import com.pinet.rest.entity.Shop;
import com.pinet.rest.entity.vo.ProdTypeVo;
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
@RequestMapping("/api/{version}/shop/product")
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
    public Result<List<ProdTypeVo>> list(@RequestParam(value = "shopId",required = false) Long shopId,
                                         @RequestParam(value = "lat",required = false) BigDecimal lat,
                                         @RequestParam(value = "lng",required = false) BigDecimal lng){
        if(shopId == null && lat == null && lng == null){
            return Result.error("参数不能为空");
        }
        if(shopId == null){
            Shop shop = shopService.getMinDistanceShop(lat, lng);
            if(shop == null){
                return Result.ok();
            }
            shopId = shop.getId();
        }
        List<ProdTypeVo> list = shopProductService.productListByShopId(shopId);
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

    /**
     * 商品详情
     * @param id 商品ID
     * @return
     */
    @RequestMapping(value = "/getById",method = RequestMethod.GET)
    @NotTokenSign
    @ApiOperation("商品详情")
    @ApiVersion(2)
    public Result<String> getById2(@RequestParam Long id){

        return Result.ok("test");
    }

}
