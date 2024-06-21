package com.pinet.rest.controller;


import com.pinet.core.result.Result;
import com.pinet.core.version.ApiVersion;
import com.pinet.rest.entity.bo.RecommendTimeBo;
import com.pinet.rest.entity.dto.MemberRecommendProdDto;
import com.pinet.rest.entity.dto.PayDto;
import com.pinet.rest.entity.dto.RecommendListDto;
import com.pinet.rest.entity.vo.*;
import com.pinet.rest.service.ICustomerMemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.pinet.core.controller.BaseController;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author wlbz
 * @since 2023-06-13
 */
@RestController
@RequestMapping("/{version}/customer_member")
@Api(tags = "店帮主")
public class CustomerMemberController extends BaseController {
    @Resource
    private ICustomerMemberService customerMemberService;

//    @ApiOperation("充值")
//    @PostMapping("/recharge")
//    @ApiVersion(1)
//    public Result<?> recharge(@Validated @RequestBody PayDto dto){
//        Object res = customerMemberService.recharge(dto);
//        return Result.ok(res);
//    }

    @ApiOperation("会员中心")
    @PostMapping("/member")
    @ApiVersion(1)
    public Result<MemberVo> member(){
        MemberVo memberVo = customerMemberService.member();
        return Result.ok(memberVo);
    }


    @ApiOperation("更多记录")
    @PostMapping("/recommendList")
    @ApiVersion(1)
    public Result<?> recommendList(@RequestBody RecommendListDto dto){
        List<RecommendListVo> recommendListVos = customerMemberService.recommendList(dto);
        return Result.ok(recommendListVos);
    }

    @ApiOperation("推荐记录")
    @RequestMapping("/recommendIndexList")
    @ApiVersion(1)
    public Result<?> recommendIndexList(){
        List<RecommendTimeBo> recommendListVos = customerMemberService.recommendIndexList();
        return Result.ok(recommendListVos);
    }



    @ApiOperation("推荐商品")
    @PostMapping("/memberRecommendProd")
    @ApiVersion(1)
    public Result<?> recommendProd(@RequestBody MemberRecommendProdDto dto){
        List<MemberRecommendProdVo> memberRecommendProdVos = customerMemberService.memberRecommendProd(dto);
        return Result.ok(memberRecommendProdVos);
    }


    @ApiOperation("更多商品")
    @GetMapping("/prodList")
    @ApiVersion(1)
    public Result<?> prodList(@Param("shopId") Long shopId){
        List<ProductListVo> productListVos = customerMemberService.productList(shopId);
        return Result.ok(productListVos);
    }


}
