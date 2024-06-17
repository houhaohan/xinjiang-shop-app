package com.pinet.rest.controller;


import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.pinet.core.result.Result;
import com.pinet.core.version.ApiVersion;
import com.pinet.inter.annotation.NotTokenSign;
import com.pinet.rest.entity.dto.VipRechargeDTO;
import com.pinet.rest.entity.enums.VipLevelEnum;
import com.pinet.rest.entity.vo.VipUserVO;
import com.pinet.rest.service.IVipUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.pinet.core.controller.BaseController;

import java.math.BigDecimal;
import java.util.Arrays;

/**
 * <p>
 * VIP用户 前端控制器
 * </p>
 *
 * @author chengshaunghui
 * @since 2024-06-04
 */
@RestController
@RequestMapping("/{version}/vip/user")
@RequiredArgsConstructor
@Api(tags = "会员")
public class VipUserController extends BaseController {
    private final IVipUserService vipUserService;


    @ApiOperation("会员充值")
    @RequestMapping(value = "/recharge",method = RequestMethod.POST)
    @ApiVersion(1)
    public Result<WxPayMpOrderResult> recharge(@Validated @RequestBody VipRechargeDTO dto){
        WxPayMpOrderResult result = vipUserService.recharge(dto);
        return Result.ok(result);
    }

    @ApiOperation("会员信息")
    @RequestMapping(value = "/info",method = RequestMethod.GET)
    @ApiVersion(1)
    public Result<VipUserVO> info(){
        Long customerId = currentUser().getUserId();
        if(customerId == null || customerId == 0L){
            VipUserVO vipUserVO = new VipUserVO();
            vipUserVO.setLevel(VipLevelEnum.VIP1.getLevel());
            vipUserVO.setVipName(VipLevelEnum.VIP1.getName());
            vipUserVO.setNextLevelDiffAmount(VipLevelEnum.VIP2.getMinAmount());
            return Result.ok(vipUserVO);
        }
        VipUserVO vipUserVO = vipUserService.info(customerId);
        return Result.ok(vipUserVO);
    }
}
