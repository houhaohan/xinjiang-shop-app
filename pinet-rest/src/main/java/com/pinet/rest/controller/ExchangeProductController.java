package com.pinet.rest.controller;


import com.pinet.core.result.Result;
import com.pinet.core.version.ApiVersion;
import com.pinet.rest.entity.ExchangeProduct;
import com.pinet.rest.entity.dto.ExchangeProductListDto;
import com.pinet.rest.service.IExchangeProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.pinet.core.controller.BaseController;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 兑换商品表 前端控制器
 * </p>
 *
 * @author wlbz
 * @since 2024-01-16
 */
@RestController
@RequestMapping("/{version}/exchange_product")
@Api(tags = "积分商城")
public class ExchangeProductController extends BaseController {
    @Resource
    private IExchangeProductService exchangeProductService;

    @PostMapping("/exchangeProductList")
    @ApiVersion(1)
    @ApiOperation("兑换商品列表")
    public Result<List<ExchangeProduct>> exchangeProductList(@Validated @RequestBody ExchangeProductListDto dto){
        List<ExchangeProduct> exchangeProducts = exchangeProductService.exchangeProductList(dto);
        return Result.ok(exchangeProducts);
    }


    @GetMapping("/exchangeProductDetail")
    @ApiVersion(1)
    @ApiOperation("兑换商品详情")
    public Result<ExchangeProduct> exchangeProductDetail(Long id){
        ExchangeProduct exchangeProduct = exchangeProductService.exchangeProductDetail(id);
        return Result.ok(exchangeProduct);
    }

}
