package com.pinet.rest.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pinet.core.controller.BaseController;
import com.pinet.core.result.Result;
import com.pinet.core.version.ApiVersion;
import com.pinet.inter.annotation.NotTokenSign;
import com.pinet.rest.entity.param.HomeProductParam;
import com.pinet.rest.entity.param.RecommendProductParam;
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
@RequestMapping("/{version}/home")
@Api(tags = "首页")
public class HomeController extends BaseController {
    @Autowired
    private IShopProductService shopProductService;

    @ApiOperation("热卖排行版")
    @RequestMapping(value = "/hotSell/list",method = RequestMethod.GET)
    @NotTokenSign
    @ApiVersion(1)
    public Result<List<HotProductVo>> hotSell(HomeProductParam param){
        List<HotProductVo> list = shopProductService.hotSellList(param);
        return Result.ok(list);
    }


    /**
     * 首页推荐商品
     * @return
     */
    @ApiOperation("推荐商品")
    @RequestMapping(value = "/recommend/list",method = RequestMethod.GET)
    @NotTokenSign
    @ApiVersion(1)
    public Result<Page<RecommendProductVo>> recommendList(RecommendProductParam param){
        Page<RecommendProductVo> pageList = shopProductService.recommendList(param);
        return Result.ok(pageList);
    }

}


