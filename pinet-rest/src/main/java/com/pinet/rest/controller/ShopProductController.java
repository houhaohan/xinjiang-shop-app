package com.pinet.rest.controller;


import com.pinet.core.result.Result;
import com.pinet.inter.annotation.NotTokenSign;
import com.pinet.rest.entity.ShopProduct;
import com.pinet.rest.entity.vo.ShopProductVo;
import com.pinet.rest.service.IShopProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.pinet.core.controller.BaseController;

/**
 * <p>
 * 店铺商品表 前端控制器
 * </p>
 *
 * @author wlbz
 * @since 2022-12-08
 */
@RestController
@RequestMapping("/shop/product")
@Api(tags = "商品")
public class ShopProductController extends BaseController {

    @Autowired
    private IShopProductService shopProductService;

    /**
     * 商品详情
     * @param id 商品ID
     * @return
     */
    @RequestMapping(value = "/getById",method = RequestMethod.GET)
    @NotTokenSign
    @ApiOperation("商品详情")
    public Result<ShopProductVo> getById(@RequestParam Long id){
        ShopProductVo shopProductVo = shopProductService.getDetailById(id);
        return Result.ok(new ShopProductVo());
    }

}
