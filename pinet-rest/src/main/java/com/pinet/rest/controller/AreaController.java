package com.pinet.rest.controller;


import com.pinet.core.result.Result;
<<<<<<< HEAD
import com.pinet.inter.annotation.NotTokenSign;
import com.pinet.rest.entity.vo.AreaTree;
=======
import com.pinet.core.version.ApiVersion;
import com.pinet.inter.annotation.NotTokenSign;
import com.pinet.rest.entity.vo.AreaPyGroupVo;
>>>>>>> origin/master
import com.pinet.rest.service.IAreaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
<<<<<<< HEAD
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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

    @RequestMapping(value = "/treeList", method = RequestMethod.GET)
    @ApiOperation("省市区N级联动")
    @NotTokenSign
    public Result<List<AreaTree>> treeList(@RequestParam List<Integer> levels) {
        List<AreaTree> areaTrees = areaService.treeList(levels);
        return Result.ok(areaTrees);
    }



}
