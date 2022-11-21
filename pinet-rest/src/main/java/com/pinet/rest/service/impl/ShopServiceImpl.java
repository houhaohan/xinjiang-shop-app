package com.pinet.rest.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pinet.rest.entity.Shop;
import com.pinet.rest.mapper.ShopMapper;
import com.pinet.rest.service.ShopService;
import org.springframework.stereotype.Service;

@Service
public class ShopServiceImpl extends ServiceImpl<ShopMapper, Shop> implements ShopService {
}
