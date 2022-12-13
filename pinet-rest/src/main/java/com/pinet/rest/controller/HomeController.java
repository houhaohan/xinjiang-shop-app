package com.pinet.rest.controller;

import com.pinet.core.controller.BaseController;
import com.pinet.core.result.Result;
import com.pinet.inter.annotation.NotTokenSign;
import com.pinet.rest.entity.param.HomeProductParam;
import com.pinet.rest.entity.vo.HotProductVo;
import com.pinet.rest.entity.vo.RecommendProductVo;
import com.pinet.rest.service.IShopProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/home")
@Api(tags = "首页")
public class HomeController extends BaseController {
    @Autowired
    private IShopProductService shopProductService;

    @ApiOperation("热卖排行版")
    @RequestMapping(value = "/hotSell/list",method = RequestMethod.GET)
    @NotTokenSign
    public Result<List<HotProductVo>> hotSell(HomeProductParam param){
        List<HotProductVo> list = shopProductService.hotSellList(param);
        return Result.ok(list);
    }


    /**
     * 根据当前用户商品浏览量排序，查找8条数据，如果没有浏览量，随机查找8条数据，没登入就随机查找8条数据
     * @return
     */
    @ApiOperation("推荐商品")
    @RequestMapping(value = "/recommend/list",method = RequestMethod.GET)
    @NotTokenSign
    public Result<List<RecommendProductVo>> recommendList(){
        Long userId = super.currentUserId();
        List<RecommendProductVo> list = shopProductService.recommendList(userId);
        return Result.ok(list);
    }


    @ApiOperation("首页banner图")
    @RequestMapping("/bannerList")
    @NotTokenSign
    public Result<List<RecommendProductVo>> bannerList(){
        return Result.ok();
    }
}


