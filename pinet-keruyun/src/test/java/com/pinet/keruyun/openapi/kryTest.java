package com.pinet.keruyun.openapi;

import com.pinet.keruyun.openapi.param.CategoryParam;
import com.pinet.keruyun.openapi.service.IKryApiService;
import com.pinet.keruyun.openapi.util.JsonUtil;
import com.pinet.keruyun.openapi.vo.BrandStoreVO;
import com.pinet.keruyun.openapi.vo.CategoryVO;
import com.pinet.keruyun.openapi.vo.KryResult;
import com.pinet.keruyun.openapi.vo.ShopDetailVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;


@SpringBootTest(classes = KryApplication.class)
public class kryTest {
    @Autowired
    private IKryApiService kryApiService;


    @Test
    public void test(){
        //品牌门店
        List<BrandStoreVO.Shop> brandStoreVOS = kryApiService.queryBrandStores(1L,"");

        System.err.println("===================================");
        System.err.println(JsonUtil.toJson(brandStoreVOS));
    }

    @Test
    public void test2(){
        //门店详情
        ShopDetailVO shopDetailVO = kryApiService.queryShopDetail(1L,"");

        System.err.println("===================================");
        System.err.println(JsonUtil.toJson(shopDetailVO));

    }


    @Test
    public void test3(){
        //门店分类
        CategoryParam param = new CategoryParam();
        param.setCategoryTypes(Arrays.asList("DISH","SIDE_DISH_GROUP"));
        KryResult<List<CategoryVO>> result = kryApiService.listQueryCategory(1L,"", param);

        System.err.println("===================================");
        System.err.println(JsonUtil.toJson(result));
    }
}
