package com.pinet.rest.controller;


import com.pinet.core.result.Result;
import com.pinet.core.version.ApiVersion;
import com.pinet.rest.entity.vo.VipRechargeTemplateVO;
import com.pinet.rest.service.IVipRechargeTemplateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import com.pinet.core.controller.BaseController;

import java.util.List;

/**
 * <p>
 * VIP充值模板 前端控制器
 * </p>
 *
 * @author wlbz
 * @since 2024-06-04
 */
@RestController
@RequestMapping("/{version}/vip/rechargeTemplate")
@RequiredArgsConstructor
@Api(tags = "会员充值模板")
public class VipRechargeTemplateController extends BaseController {

    private final IVipRechargeTemplateService vipRechargeTemplateService;

    @ApiOperation("门店充值模板列表")
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @ApiVersion(1)
    public Result<List<VipRechargeTemplateVO>> list(@RequestParam("shopId") Long shopId){
        List<VipRechargeTemplateVO> list = vipRechargeTemplateService.templateList(shopId);
        return Result.ok(list);
    }
}
