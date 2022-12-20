package com.pinet.rest.service.impl;

import com.pinet.rest.entity.OrderPay;
import com.pinet.rest.mapper.OrderPayMapper;
import com.pinet.rest.service.IOrderPayService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 支付记录表 服务实现类
 * </p>
 *
 * @author chengshuanghui
 * @since 2022-12-20
 */
@Service
public class OrderPayServiceImpl extends ServiceImpl<OrderPayMapper, OrderPay> implements IOrderPayService {

}
