package com.pinet.rest.controller;


import com.pinet.core.result.Result;
import com.pinet.core.version.ApiVersion;
import com.pinet.rest.entity.dto.AddCartDto;
import com.pinet.rest.entity.dto.CartListDto;
import com.pinet.rest.entity.dto.EditCartProdNumDto;
import com.pinet.rest.entity.vo.AddCartVo;
import com.pinet.rest.entity.vo.CartListVo;
import com.pinet.rest.entity.vo.CartVo;
import com.pinet.rest.service.ICartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.pinet.core.controller.BaseController;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 购物车 前端控制器
 * </p>
 *
 * @author wlbz
 * @since 2022-12-08
 */
@RestController
@RequestMapping("/{version}/cart")
@Api(tags = "购物车模块")
public class CartController extends BaseController {
    @Resource
    private ICartService cartService;

    @ApiOperation("购物车列表")
    @PostMapping("/cartList")
    @ApiVersion(1)
    public Result<List<CartListVo>> cartList(@Validated @RequestBody CartListDto dto) {
        Long customerId = currentUserId();
        dto.setCustomerId(customerId);
        List<CartListVo> cartListVos = cartService.cartList(dto);
        return Result.ok(cartListVos);
    }


    @ApiOperation("添加购物车")
    @PostMapping("/addCart")
    @ApiVersion(1)
    public Result<AddCartVo> addCart(@Validated @RequestBody AddCartDto dto) {
        AddCartVo addCartVo = cartService.addCart(dto);
        return Result.ok(addCartVo);
    }


    @ApiOperation("获取购物车总价")
    @PostMapping("/getCartTotalPrice")
    @ApiVersion(1)
    public Result<CartVo> getCartPrice(@Validated @RequestBody CartListDto dto) {
        CartVo getCartTotalPriceVo = cartService.getCartByUserIdAndShopId(dto.getShopId(),currentUserId());
        return Result.ok(getCartTotalPriceVo);
    }


    @ApiOperation("购物车数量修改")
    @PostMapping("/editCartProdNum")
    @ApiVersion(1)
    public Result<Boolean> editCartProdNum(@Validated @RequestBody EditCartProdNumDto dto) {
        Boolean res = cartService.editCartProdNum(dto);
        if (!res) {
            return Result.error("添加失败");
        }
        return Result.ok();
    }


}
