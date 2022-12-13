package com.pinet.rest.controller;


import com.pinet.core.result.Result;
import com.pinet.rest.entity.Cart;
import com.pinet.rest.entity.dto.AddCartDto;
import com.pinet.rest.entity.dto.CartListDto;
import com.pinet.rest.entity.dto.EditCartProdNumDto;
import com.pinet.rest.entity.vo.CartListVo;
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
@RequestMapping("/cart")
@Api(tags = "购物车模块")
public class CartController extends BaseController {
    @Resource
    private ICartService cartService;

    @ApiOperation("购物车列表")
    @PostMapping("/cartList")
    public Result<List<CartListVo>> cartList(@Validated @RequestBody CartListDto dto){
        Long customerId = currentUserId();
        dto.setCustomerId(customerId);
        List<CartListVo> cartListVos = cartService.cartList(dto);
        return Result.ok(cartListVos);
    }


    @ApiOperation("添加购物车")
    @PostMapping("/addCart")
    public Result<Boolean> addCart(@Validated @RequestBody AddCartDto dto){
        Boolean res = cartService.addCart(dto);
        if (!res){
            return Result.error("添加失败");
        }
        return Result.ok();
    }


    @ApiOperation("购物车数量修改")
    @PostMapping("/editCartProdNum")
    public Result<Boolean> editCartProdNum(@Validated @RequestBody EditCartProdNumDto dto){
        Boolean res = cartService.editCartProdNum(dto);
        if (!res){
            return Result.error("添加失败");
        }
        return Result.ok();
    }









}
