package com.pinet.rest.controller;


import com.pinet.core.result.Result;
import com.pinet.core.version.ApiVersion;
import com.pinet.rest.entity.dto.OrderPayDto;
import com.pinet.rest.entity.dto.PayDto;
import com.pinet.rest.entity.param.PayParam;
import com.pinet.rest.service.ICustomerMemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.pinet.core.controller.BaseController;

import javax.annotation.Resource;

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

    @ApiOperation("充值")
    @PostMapping("/recharge")
    @ApiVersion(1)
    public Result<?> recharge(@Validated @RequestBody PayDto dto){
        Object res = customerMemberService.recharge(dto);
        return Result.ok(res);
    }



}
