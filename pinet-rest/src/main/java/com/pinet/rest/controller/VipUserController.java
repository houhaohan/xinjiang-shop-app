package com.pinet.rest.controller;


import com.pinet.core.result.Result;
import com.pinet.core.version.ApiVersion;
import com.pinet.inter.annotation.NotTokenSign;
import com.pinet.rest.entity.dto.VipRechargeDTO;
import com.pinet.rest.entity.request.SmsSendRequest;
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
    public Result<?> recharge(@Validated @RequestBody VipRechargeDTO dto){
        vipUserService.recharge(dto);
        return Result.ok();
    }

    @ApiOperation("会员信息")
    @RequestMapping(value = "/info",method = RequestMethod.GET)
    @ApiVersion(1)
    public Result<VipUserVO> info(){
        VipUserVO vipUserVO = vipUserService.info(currentUserId());
        return Result.ok(vipUserVO);
    }
}
