package com.pinet.rest.controller;


import com.pinet.core.result.Result;
import com.pinet.core.version.ApiVersion;
import com.pinet.rest.entity.VipEquity;
import com.pinet.rest.service.IVipEquityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.pinet.core.controller.BaseController;

import java.util.List;

/**
 * <p>
 * VIP权益表 前端控制器
 * </p>
 *
 * @author wlbz
 * @since 2024-06-11
 */
@RestController
@RequestMapping("/{version}/vip/equity")
@Api(tags = "VIP等级权益")
@RequiredArgsConstructor
public class VipEquityController extends BaseController {
    private final IVipEquityService vipEquityService;

    @ApiOperation("会员权益列表")
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @ApiVersion(1)
    public Result<List<VipEquity>> recharge(){
        List<VipEquity> list = vipEquityService.equityList();
        return Result.ok(list);
    }
}
