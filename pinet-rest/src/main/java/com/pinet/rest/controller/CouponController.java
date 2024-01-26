package com.pinet.rest.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pinet.core.result.Result;
import com.pinet.core.version.ApiVersion;
import com.pinet.inter.annotation.NotTokenSign;
import com.pinet.rest.entity.Coupon;
import com.pinet.rest.service.ICouponService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.pinet.core.controller.BaseController;


/**
 * <p>
 * 优惠券表 前端控制器
 * </p>
 *
 * @author wlbz
 * @since 2024-01-10
 */
@RestController
@RequestMapping("/{version}/coupon")
@Api(tags = "优惠券")
public class CouponController extends BaseController {
    @Autowired
    private ICouponService couponService;

    @ApiOperation("优惠券背景图")
    @GetMapping("/getImageUrl")
    @NotTokenSign
    @ApiVersion(1)
    public Result<Coupon> getImageUrl(@RequestParam("id") Long id) {
        QueryWrapper<Coupon> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id","name","quantity","claimed_num","image_url");
        queryWrapper.eq("id",id);
        Coupon coupon = couponService.getOne(queryWrapper);
        return Result.ok(coupon);
    }

}
