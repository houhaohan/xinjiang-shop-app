package com.pinet.rest.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pinet.core.result.Result;
import com.pinet.rest.entity.CustomerAddress;
import com.pinet.rest.entity.dto.BaseDto;
import com.pinet.rest.entity.dto.CustomerAddressDto;
import com.pinet.rest.service.ICustomerAddressService;
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

    @ApiOperation("列表")
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public Result<List<CustomerAddress>> list(){
        Long userId = super.currentUserId();
        if(userId == null){
            return Result.ok();
        }
        QueryWrapper<CustomerAddress> wrapper = new QueryWrapper<>();
        wrapper.eq("customer_id",106);
        List<CustomerAddress> list = customerAddressService.list(wrapper);
        return Result.ok(list);
    }

    @ApiOperation("新增")
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public Result<String> save(@RequestBody CustomerAddressDto customerAddressDto){
        Long userId = super.currentUserId();
        if(userId == null){
            return Result.error("用户没有登入，请先登入");
        }
        boolean success = customerAddressService.add(customerAddressDto,userId);
        if(success){
            return Result.ok("操作成功");
        }
        return Result.error("操作失败");
    }

    @ApiOperation("修改")
    @RequestMapping(value = "/updateById",method = RequestMethod.POST)
    public Result<String> updateById(@RequestBody CustomerAddressDto customerAddressDto){
        Long userId = super.currentUserId();
        if(userId == null){
            return Result.error("用户没有登入，请先登入");
        }
        boolean success = customerAddressService.edit(customerAddressDto,userId);
        if(success){
            return Result.ok("操作成功");
        }
        return Result.error("操作失败");
    }

    @ApiOperation("删除")
    @RequestMapping(value = "/deleteById",method = RequestMethod.DELETE)
    public Result<String> deleteById(@RequestBody BaseDto dto){
        Long userId = super.currentUserId();
        if(userId == null){
            return Result.error("用户没有登入，请先登入");
        }
        boolean success = customerAddressService.removeById(dto.getId());
        if(success){
            return Result.ok("删除成功");
        }
        return Result.error("删除失败");
    }


    @ApiOperation("设置默认地址")
    @RequestMapping(value = "/default",method = RequestMethod.POST)
    public Result<String> updateDefault(@RequestBody BaseDto baseDto){
        Long userId = super.currentUserId();
        if(userId == null){
            return Result.error("用户没有登入，请先登入");
        }
        CustomerAddress entity = new CustomerAddress();
        entity.setId(baseDto.getId());
        entity.setStatus(1);
        boolean success = customerAddressService.updateById(entity);
        if(success){
            return Result.ok("操作成功");
        }
        return Result.error("操作失败");
    }

}
