package com.pinet.rest.controller;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pinet.core.controller.BaseController;
import com.pinet.core.exception.PinetException;
import com.pinet.core.result.Result;
import com.pinet.core.util.StringUtil;
import com.pinet.core.util.ThreadLocalUtil;
import com.pinet.core.version.ApiVersion;
import com.pinet.inter.annotation.NotTokenSign;
import com.pinet.rest.entity.Customer;
import com.pinet.rest.entity.CustomerBalance;
import com.pinet.rest.entity.CustomerBalanceRecord;
import com.pinet.rest.entity.VipShopBalance;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/{version}/home")
@RequiredArgsConstructor
@Api(tags = "首页")
@Slf4j
public class HomeController extends BaseController {

    private final IShopProductService shopProductService;
    private final ICustomerCouponService customerCouponService;
    private final IVipShopBalanceService vipShopBalanceService;
    private final ICustomerBalanceRecordService customerBalanceRecordService;
    private final ICustomerService customerService;
    private final ICustomerScoreService customerScoreService;


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
    public Result<TopCountDto> topCount(Long shopId) {
        Long customerId = ThreadLocalUtil.getUserLogin().getUserId();
        TopCountDto topCountDto = new TopCountDto();
        Long couponCount = customerCouponService.countByCustomerId(customerId);
        topCountDto.setCouponCount(couponCount);

        //余额
        List<VipShopBalance> shopBalanceList = vipShopBalanceService.getByCustomerId(customerId);
        if(shopId == null){
            if(!CollectionUtils.isEmpty(shopBalanceList)){
                topCountDto.setBalance(shopBalanceList.get(0).getAmount());
            }
        }else {
            BigDecimal balance = shopBalanceList.stream()
                    .filter(item -> Objects.equals(item.getShopId(), shopId))
                    .map(VipShopBalance::getAmount)
                    .findFirst()
                    .orElse(BigDecimal.ZERO);
            topCountDto.setBalance(balance);
        }
        Double score = customerScoreService.getScoreByCustomerId(customerId);
        topCountDto.setPoint(score);
        return Result.ok(topCountDto);
    }

    @PostMapping("/balance")
    @ApiOperation("余额")
    @ApiVersion(1)
    public Result<BalanceVo> balance(Long shopId){
        Long customerId = ThreadLocalUtil.getUserLogin().getUserId();
        BalanceVo vo = new BalanceVo();
        List<VipShopBalance> shopBalanceList = vipShopBalanceService.getByCustomerId(customerId);
        if(shopId == null){
            if(!CollectionUtils.isEmpty(shopBalanceList)){
                vo.setBalance(shopBalanceList.get(0).getAmount());
            }
        }else {
            BigDecimal balance = shopBalanceList.stream()
                    .filter(item -> Objects.equals(item.getShopId(), shopId))
                    .map(VipShopBalance::getAmount)
                    .findFirst()
                    .orElse(BigDecimal.ZERO);
            vo.setBalance(balance);
        }
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
        boolean flag = StringUtil.isNotBlank(customer.getPayPassword());
        return Result.ok(flag);

    }


    @GetMapping("/checkPayPassword")
    @ApiOperation("校验支付密码")
    @ApiVersion(1)
    public Result<?> checkPayPassword(String payPassword){
        Long userId = ThreadLocalUtil.getUserLogin().getUserId();
        Customer customer = customerService.getById(userId);
        if (StringUtil.isBlank(customer.getPayPassword())){
            throw new PinetException("请先设置支付密码");
        }
        return Result.ok(customer.getPayPassword().equals(payPassword));
    }


}


