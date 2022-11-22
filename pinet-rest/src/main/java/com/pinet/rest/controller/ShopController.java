package com.pinet.rest.controller;


import com.pinet.inter.annotation.NotTokenSign;
import com.pinet.rest.entity.Shop;
import com.pinet.rest.service.IShopService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.pinet.core.controller.BaseController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wlbz
 * @since 2022-11-21
 */
@RestController
@RequestMapping("/shop")
@Api(tags = "店铺")
public class ShopController extends BaseController {
    @Autowired
    private IShopService shopService;

    @GetMapping("/getShop")
    @ApiOperation(value = "获取店铺")
    @NotTokenSign
    public Shop getShop(String id){
        return shopService.getById(22);

    }

}
