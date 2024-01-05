package com.pinet.rest.controller;


import com.pinet.core.result.Result;
import com.pinet.inter.annotation.NotTokenSign;
import com.pinet.core.version.ApiVersion;
import com.pinet.rest.entity.vo.AreaPyGroupVo;
import com.pinet.rest.service.IAreaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.pinet.core.controller.BaseController;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * <p>
 * 地址区域表 前端控制器
 * </p>
 *
 * @author wlbz
 * @since 2024-01-04
 */
@RestController
@RequestMapping("/{version}/area")
@Api(tags = "地址区域")
public class AreaController extends BaseController {
    @Autowired
    private IAreaService areaService;


    @ApiOperation("城市根据字母分组")
    @GetMapping("/cityPyGroupList")
    @ApiVersion(1)
    @NotTokenSign
    public Result<List<AreaPyGroupVo>> cityPyGroupList(String name) {
        List<AreaPyGroupVo> areas = areaService.cityPyGroupList(name);
        return Result.ok(areas);
    }

}
