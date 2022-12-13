package com.pinet.rest.controller;


import com.pinet.core.result.Result;
import com.pinet.rest.entity.dto.CartListDto;
import com.pinet.rest.entity.dto.ShopListDto;
import com.pinet.rest.entity.vo.CartListVo;
import com.pinet.rest.entity.vo.ShopVo;
import com.pinet.rest.service.IShopService;
import com.pinet.rest.service.impl.ShopServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.pinet.core.controller.BaseController;

import java.util.List;

/**
 * <p>
 * 店铺表 前端控制器
 * </p>
 *
 * @author wlbz
 * @since 2022-12-06
 */
@RestController
@RequestMapping("/shop")
@Api(tags = "店铺")
public class ShopController extends BaseController {
    @Autowired
    private IShopService shopService;

    @ApiOperation("店铺列表")
    @PostMapping("/shopList")
    public Result<List<ShopVo>> cartList(@Validated @RequestBody ShopListDto dto){
        List<ShopVo> shopVoList = shopService.shopList(dto);
        return Result.ok(shopVoList);
    }
}
