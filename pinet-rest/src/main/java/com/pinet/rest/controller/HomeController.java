package com.pinet.rest.controller;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pinet.core.controller.BaseController;
import com.pinet.core.exception.PinetException;
import com.pinet.core.page.PageRequest;
import com.pinet.core.result.Result;
import com.pinet.core.util.StringUtil;
import com.pinet.core.util.ThreadLocalUtil;
import com.pinet.core.version.ApiVersion;
import com.pinet.inter.annotation.NotTokenSign;
import com.pinet.rest.entity.Customer;
import com.pinet.rest.entity.CustomerBalance;
import com.pinet.rest.entity.CustomerBalanceRecord;
import com.pinet.rest.entity.dto.BalanceRecordListDto;
import com.pinet.rest.entity.dto.ForgetPayPasswordDto;
import com.pinet.rest.entity.dto.SetPayPasswordDto;
import com.pinet.rest.entity.dto.TopCountDto;
import com.pinet.rest.entity.param.HomeProductParam;
import com.pinet.rest.entity.param.RecommendProductParam;
import com.pinet.rest.entity.vo.BalanceVo;
import com.pinet.rest.entity.vo.HotProductVo;
import com.pinet.rest.entity.vo.RecommendProductVo;
import com.pinet.rest.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/{version}/home")
@Api(tags = "首页")
@Slf4j
public class HomeController extends BaseController {
    @Autowired
    private IShopProductService shopProductService;

    @Resource
    private ICustomerCouponService customerCouponService;

    @Resource
    private ICustomerBalanceService customerBalanceService;

    @Resource
    private ICustomerBalanceRecordService customerBalanceRecordService;

    @Resource
    private ICustomerService customerService;


    @ApiOperation("热卖排行版")
    @RequestMapping(value = "/hotSell/list", method = RequestMethod.GET)
    @NotTokenSign
    @ApiVersion(1)
    public Result<List<HotProductVo>> hotSell(HomeProductParam param) {
        List<HotProductVo> list = shopProductService.hotSellList(param);
        return Result.ok(list);
    }


    /**
     * 首页推荐商品
     *
     * @return
     */
    @ApiOperation("推荐商品")
    @RequestMapping(value = "/recommend/list", method = RequestMethod.GET)
    @NotTokenSign
    @ApiVersion(1)
    public Result<Page<RecommendProductVo>> recommendList(RecommendProductParam param) {
        Page<RecommendProductVo> pageList = shopProductService.recommendList(param);
        return Result.ok(pageList);
    }

    @ApiOperation("我的页面上方统计")
    @RequestMapping("/topCount")
    @ApiVersion(1)
    public Result<?> topCount() {
        Long customerId = ThreadLocalUtil.getUserLogin().getUserId();
        TopCountDto topCountDto = new TopCountDto();
        Integer couponCount = customerCouponService.countByCustomerId(customerId);
        topCountDto.setCouponCount(couponCount);

        //余额
        CustomerBalance customerBalance = customerBalanceService.getByCustomerId(customerId);
        if (ObjectUtil.isNotNull(customerBalance)){
            topCountDto.setBalance(customerBalance.getAvailableBalance());
        }
        return Result.ok(topCountDto);
    }




    @PostMapping("/balance")
    @ApiOperation("余额")
    @ApiVersion(1)
    public Result<?> balance(){
        Long customerId = ThreadLocalUtil.getUserLogin().getUserId();
        BalanceVo vo = new BalanceVo();
        CustomerBalance customerBalance = customerBalanceService.getByCustomerId(customerId);
        vo.setBalance(customerBalance.getAvailableBalance());
        List<CustomerBalanceRecord> customerBalanceRecords = customerBalanceRecordService.getListLimit5(customerId);
        vo.setCustomerBalanceRecords(customerBalanceRecords);
        return Result.ok(vo);
    }

    @PostMapping("/balanceRecordList")
    @ApiOperation("余额变动明细")
    @ApiVersion(1)
    public Result<List<CustomerBalanceRecord>> balanceRecordList(@RequestBody BalanceRecordListDto dto){
        List<CustomerBalanceRecord> customerBalanceRecords = customerBalanceRecordService.balanceRecordList(dto);
        return Result.ok(customerBalanceRecords);
    }


    @PostMapping("/setPayPassword")
    @ApiOperation("设置支付密码")
    @ApiVersion(1)
    public Result<?> setPayPassword(@RequestBody @Validated SetPayPasswordDto dto){
        customerService.setPayPassword(dto);
        return Result.ok();
    }

    @PostMapping("/updatePayPassword")
    @ApiOperation("修改支付密码")
    @ApiVersion(1)
    public Result<?> updatePayPassword(@RequestBody @Validated SetPayPasswordDto dto){
        customerService.updatePayPassword(dto);
        return Result.ok();
    }

    @PostMapping("/forgetPayPassword")
    @ApiOperation("忘记密码")
    @ApiVersion(1)
    public Result<?> forgetPayPassword(@Validated @RequestBody ForgetPayPasswordDto dto){
        customerService.forgetPayPassword(dto);
        return Result.ok();
    }

    @PostMapping("/isPayPassword")
    @ApiOperation("是否设置支付密码")
    @ApiVersion(1)
    public Result<Boolean> isPayPassword(){
        Long customerId = ThreadLocalUtil.getUserLogin().getUserId();
        Customer customer = customerService.getById(customerId);
        if (customer.getPayPassword() != null && StringUtil.isNotBlank(customer.getPayPassword())){
            return Result.ok(true);
        }
        return Result.ok(false);
    }


    @GetMapping("/checkPayPassword")
    @ApiOperation("校验支付密码")
    @ApiVersion(1)
    public Result<?> checkPayPassword(String payPassword){
        Long userId = ThreadLocalUtil.getUserLogin().getUserId();
        Customer customer = customerService.getById(userId);
        if (customer.getPayPassword() == null || StringUtil.isBlank(customer.getPayPassword())){
            throw new PinetException("请先设置支付密码");
        }
        return Result.ok(customer.getPayPassword().equals(payPassword));
    }


}


