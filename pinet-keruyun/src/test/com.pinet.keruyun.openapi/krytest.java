package com.pinet.keruyun.openapi;


import com.pinet.keruyun.openapi.param.DetailDishParam;
import com.pinet.keruyun.openapi.service.IKryApiService;
import com.pinet.keruyun.openapi.service.KryCallService;
import com.pinet.keruyun.openapi.type.AuthType;
import com.pinet.keruyun.openapi.type.KryAPI;
import com.pinet.keruyun.openapi.util.JsonUtil;
import com.pinet.keruyun.openapi.vo.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest(classes = kryApplication.class)
public class krytest {

    @Autowired
    private KryCallService kryCallService ;

    @Autowired
    private IKryApiService kryApiService;

    String token = "97b6428f0aa494666462dd0dc92f6f9d";


    @Test
    public void test(){
        DetailDishParam param = new DetailDishParam();
//        param.setDishIds(Arrays.asList("958810830555"));
//        param.setDishIds(Arrays.asList("959210910555"));
        param.setDishIds(Arrays.asList("959210910555"));

//        String s = kryCallService.postCall(KryAPI.BRAND_SHOP_DISH, AuthType.SHOP, 13290197L, "97b6428f0aa494666462dd0dc92f6f9d", null);
//        System.out.println(s);

//        KryResult<DishListVO> dishListVOKryResult = kryApiService.pageQueryBaseDish(13290197L, token, null);
//        System.out.println(JsonUtil.toJson(dishListVOKryResult));

//        KryResult<List<DetailDishVO>> result = kryApiService.listQueryDetailDish(13290197L, token, param);
//        System.out.println(JsonUtil.toJson(result));

//        KryResult<List<CategoryVO>> result = kryApiService.listQueryCategory(13290197L, token, null);
//        System.out.println(JsonUtil.toJson(result));

        Map<String,Object> map = new HashMap<>();
//        map.put("orderId","20230830061316000169325721950184");
        map.put("orderId","20230831061316000169886642340184");
//        map.put("orderId","20230831132901971409190000000184");
//        map.put("orderId","102308310901329019700001");
//        String s = kryCallService.postCall(KryAPI.ORDER_DETAIL, AuthType.SHOP, 13290197L, "97b6428f0aa494666462dd0dc92f6f9d", map);
//        System.out.println(s);


        map.put("dateType","FINISH_TIME");
        map.put("startDate","2023-08-20 00:00:00");
        map.put("endDate","2023-09-01 23:59:59");
//        map.put("orderTypeList",Arrays.asList("FOR_HERE","SELF_TAKE_OUT"));


        String s = kryCallService.postCall(KryAPI.ORDER_LIST, AuthType.SHOP, 13290197L, "97b6428f0aa494666462dd0dc92f6f9d", map);
        System.out.println(s);
    }
}
