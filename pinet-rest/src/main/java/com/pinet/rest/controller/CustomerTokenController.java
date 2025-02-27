package com.pinet.rest.controller;


import com.pinet.core.result.Result;
import com.pinet.core.util.ThreadLocalUtil;
import com.pinet.core.version.ApiVersion;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.pinet.core.controller.BaseController;

/**
 * <p>
 * 用户登录token 前端控制器
 * </p>
 *
 * @author wlbz
 * @since 2022-12-12
 */
@RestController
@RequestMapping("/{version}/customer-token")
public class CustomerTokenController extends BaseController {

    @RequestMapping("/getUserId")
    @ApiVersion(1)
    public Result<Long> getUserId(){
        Long userId = ThreadLocalUtil.getUserLogin().getUserId();
        return Result.ok(userId);
    }

}
