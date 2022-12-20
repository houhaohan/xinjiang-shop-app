package com.pinet.rest.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pinet.core.result.Result;
import com.pinet.rest.entity.ProductType;
import com.pinet.rest.entity.ShopProduct;
import com.pinet.rest.entity.dto.ProductTypeDto;
import com.pinet.rest.entity.vo.OrderListVo;
import com.pinet.rest.service.IProductTypeService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.pinet.core.controller.BaseController;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商品分类表 前端控制器
 * </p>
 *
 * @author wlbz
 * @since 2022-12-06
 */
@RestController
@RequestMapping("/productType")
public class ProductTypeController extends BaseController {
    @Autowired
    private IProductTypeService productTypeService;

    /**
     * 店铺分类及分类商品列表
     * @param dto
     * @return
     */
    @ApiOperation("店铺分类及分类商品列表")
    @RequestMapping(value = "/productTypeProdList",method = RequestMethod.GET)
    public Result<Map<String,List<ShopProduct>>> productType(ProductTypeDto dto){
        Map<String, List<ShopProduct>> productTypeMap = productTypeService.productType(dto);
        return Result.ok(productTypeMap);
    }

}
