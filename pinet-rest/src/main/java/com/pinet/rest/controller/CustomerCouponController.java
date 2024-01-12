package com.pinet.rest.controller;


import com.pinet.core.page.PageRequest;
import com.pinet.core.result.Result;
import com.pinet.core.version.ApiVersion;
import com.pinet.inter.annotation.NotTokenSign;
import com.pinet.rest.entity.CustomerCoupon;
import com.pinet.rest.entity.dto.UpdateCouponStatusDto;
import com.pinet.rest.entity.vo.CustomerCouponListVo;
import com.pinet.rest.service.ICustomerCouponService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.pinet.core.controller.BaseController;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 用户优惠券 前端控制器
 * </p>
 *
 * @author wlbz
 * @since 2023-08-14
 */
@RestController
@RequestMapping("/{version}/customer_coupon")
@Api(tags = "用户优惠券")
public class CustomerCouponController extends BaseController {
    @Resource
    private ICustomerCouponService customerCouponService;

    @PostMapping("/customerCouponList")
    @ApiOperation("优惠券列表")
    @ApiVersion(1)
    public Result<List<CustomerCouponListVo>> customerCouponList(@RequestBody PageRequest pageRequest){
        List<CustomerCouponListVo> customerCouponList = customerCouponService.customerCouponList(pageRequest);
        return Result.ok(customerCouponList);
    }

    @PostMapping("/updateCouponStatus")
    @ApiOperation("更新优惠券状态")
    @ApiVersion(1)
    public Result<?> drawCoupon(@RequestBody @Validated UpdateCouponStatusDto dto){
        boolean res = customerCouponService.updateCouponStatus(dto);
        if (!res){
            return Result.error();
        }
        return Result.ok();
    }


    @PostMapping("/customerCouponListDetailList")
    @ApiOperation("优惠券明细列表")
    @ApiVersion(1)
    public Result<List<CustomerCouponListVo>> customerCouponListDetailList(@RequestBody PageRequest pageRequest){
        List<CustomerCouponListVo> customerCouponList = customerCouponService.customerCouponListDetailList(pageRequest);
        return Result.ok(customerCouponList);
    }

    @PostMapping("/customerCouponInvalidList")
    @ApiOperation("优惠券失效列表")
    @ApiVersion(1)
    public Result<List<CustomerCouponListVo>> customerCouponInvalidList(@RequestBody PageRequest pageRequest){
        List<CustomerCouponListVo> customerCouponList = customerCouponService.customerCouponInvalidList(pageRequest);
        return Result.ok(customerCouponList);
    }

    @PostMapping("/indexCouponList")
    @ApiOperation("首页优惠券列表弹窗")
    @ApiVersion(1)
    public Result<?> indexCouponList(){
        List<CustomerCoupon> customerCouponList = customerCouponService.indexCouponList();
        return Result.ok(customerCouponList);
    }


    @RequestMapping(value = "/couponWarn",method = RequestMethod.GET)
    @ApiOperation("优惠券过期提醒")
    @ApiVersion(1)
    public Result<?> couponWarn(Long customerCouponId){
        customerCouponService.couponWarn(customerCouponId);
        return Result.ok();
    }

    @RequestMapping(value = "/receive",method = RequestMethod.POST)
    @ApiOperation("优惠券领取")
    @ApiVersion(1)
    public Result<?> receive(@RequestParam("couponId") Long couponId){
        customerCouponService.receive(couponId);
        return Result.ok();
    }
}
