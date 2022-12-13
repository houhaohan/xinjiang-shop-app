package com.pinet.rest.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pinet.core.result.Result;
import com.pinet.rest.entity.CustomerAddress;
import com.pinet.rest.entity.dto.CustomerAddressDto;
import com.pinet.rest.service.ICustomerAddressService;
import com.pinet.rest.util.LoginUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.pinet.core.controller.BaseController;

import java.util.List;

/**
 * <p>
 * 地址管理表 前端控制器
 * </p>
 *
 * @author wlbz
 * @since 2022-12-06
 */
@RestController
@RequestMapping("/customer/address")
@Api(tags = "收获地址")
public class CustomerAddressController extends BaseController {

    @Autowired
    private ICustomerAddressService customerAddressService;

    @Autowired
    private LoginUser loginUser;


    @ApiOperation("列表")
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public Result list(){
        Long userId = loginUser.currentUserId();
        QueryWrapper<CustomerAddress> wrapper = new QueryWrapper<>();
        wrapper.eq("customer_id",userId);
        List<CustomerAddress> list = customerAddressService.list(wrapper);
        return Result.ok(list);
    }

    @ApiOperation("新增")
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public Result save(@RequestBody CustomerAddressDto customerAddressDto){
        boolean success = customerAddressService.add(customerAddressDto);
        if(success){
            return Result.ok("操作成功");
        }
        return Result.error("操作失败");
    }

    @ApiOperation("修改")
    @RequestMapping(value = "/updateById",method = RequestMethod.POST)
    public Result updateById(@RequestBody CustomerAddressDto customerAddressDto){
        boolean success = customerAddressService.edit(customerAddressDto);
        if(success){
            return Result.ok("操作成功");
        }
        return Result.error("操作失败");
    }

    @ApiOperation("删除")
    @RequestMapping(value = "/deleteById",method = RequestMethod.DELETE)
    public Result deleteById(@RequestBody Long id){
        boolean success = customerAddressService.removeById(id);
        if(success){
            return Result.ok("删除成功");
        }
        return Result.error("删除失败");
    }


    @ApiOperation("设置默认地址")
    @RequestMapping(value = "/default",method = RequestMethod.POST)
    public Result save(@RequestBody Long id){
        CustomerAddress entity = new CustomerAddress();
        entity.setId(id);
        entity.setStatus(1);
        boolean success = customerAddressService.updateById(entity);
        if(success){
            return Result.ok("操作成功");
        }
        return Result.error("操作失败");
    }

}
