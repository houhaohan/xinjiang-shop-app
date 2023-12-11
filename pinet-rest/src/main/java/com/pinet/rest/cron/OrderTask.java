package com.pinet.rest.cron;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pinet.rest.entity.Orders;
import com.pinet.rest.service.IOrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class OrderTask {

    @Autowired
    private IOrdersService ordersService;

    @Scheduled(cron = "*/2 * * * * ?")
    public void task(){
//        System.out.println("定时任务开启");
//        QueryWrapper<Orders> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("","");
    }
}
