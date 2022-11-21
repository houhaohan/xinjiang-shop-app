package com.pinet.rest.controller;

import com.pinet.common.redis.util.RedisUtil;
import com.pinet.rest.entity.Shop;
import com.pinet.rest.service.ShopService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/shop")
public class ShopController {

    @Autowired
    private ShopService shopService;
    @Autowired
    private RedisUtil redisUtil;


    @PostMapping("/getById")
    public Shop getById(){
        return shopService.getById(22);
    }
    @GetMapping("/getCache")
    public String getCache(){
        return redisUtil.get("aaa");
    }


}

