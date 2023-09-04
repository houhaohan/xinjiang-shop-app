package com.pinet.rest.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pinet.core.result.Result;
import com.pinet.core.version.ApiVersion;
import com.pinet.inter.annotation.NotTokenSign;
import com.pinet.rest.entity.Agreement;
import com.pinet.rest.entity.dto.OrderListDto;
import com.pinet.rest.entity.vo.OrderListVo;
import com.pinet.rest.service.IAgreementService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.pinet.core.controller.BaseController;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 协议表 前端控制器
 * </p>
 *
 * @author wlbz
 * @since 2023-09-04
 */
@RestController
@RequestMapping("/{version}/agreement")
public class AgreementController extends BaseController {
    @Resource
    private IAgreementService agreementService;

    @RequestMapping("/getAgreementById")
    @ApiVersion(1)
    @NotTokenSign
    public Result<?> getAgreementById(Integer id) {
        Agreement agreement = agreementService.getById(id);
        return Result.ok(agreement);
    }

}
