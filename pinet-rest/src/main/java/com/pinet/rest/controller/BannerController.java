package com.pinet.rest.controller;


import com.pinet.core.result.Result;
import com.pinet.rest.entity.Banner;
import com.pinet.rest.service.IBannerService;
import io.swagger.annotations.ApiOperation;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.pinet.core.controller.BaseController;

import java.util.List;

/**
 * <p>
 * banner表 前端控制器
 * </p>
 *
 * @author wlbz
 * @since 2022-12-14
 */
@RestController
@RequestMapping("/banner")
public class BannerController extends BaseController {
    @Autowired
    private IBannerService bannerService;

    @ApiOperation("轮播图列表")
    @GetMapping("/bannerList")
    public Result<List<Banner>> bannerList(){
        List<Banner> bannerList = bannerService.bannerList();
        return Result.ok(bannerList);
    }
}
