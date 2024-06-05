package com.pinet.rest.mq.consumer;

import com.alibaba.fastjson.JSON;
import com.pinet.core.exception.PinetException;
import com.pinet.keruyun.openapi.dto.CustomerCreateDTO;
import com.pinet.keruyun.openapi.param.CustomerParam;
import com.pinet.keruyun.openapi.service.IKryApiService;
import com.pinet.keruyun.openapi.type.AuthType;
import com.pinet.keruyun.openapi.vo.customer.CustomerCreateVO;
import com.pinet.keruyun.openapi.vo.customer.CustomerQueryVO;
import com.pinet.rest.entity.Shop;
import com.pinet.rest.entity.VipUser;
import com.pinet.rest.mapper.ShopMapper;
import com.pinet.rest.mq.constants.QueueConstants;
import com.pinet.rest.service.IVipUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @description: 创建客如云会员
 * @author: chengshuanghui
 * @date: 2024-06-05 10:04
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class KryVipCreateListener {
    private final IKryApiService kryApiService;
    private final IVipUserService vipUserService;
    private final ShopMapper shopMapper;

    @JmsListener(destination = QueueConstants.KRY_VIP_CREATE, containerFactory = "queueListener")
    @Transactional(rollbackFor = Exception.class)
    public void vipCreate(String message){
        VipUser user = vipUserService.getById(Long.valueOf(message));
        Shop shop = shopMapper.selectById(user.getShopId());
        String token = kryApiService.getToken(AuthType.SHOP, shop.getKryShopId());

        //查询下客如云是否已经注册过会员
        CustomerParam param = new CustomerParam();
        param.setMobile(user.getPhone());
        CustomerQueryVO customerQueryVO = kryApiService.queryByMobile(shop.getKryShopId(), token, param);
        if(customerQueryVO != null){
            user.setKryCustomerId(customerQueryVO.getCustomerId());
            vipUserService.save(user);
            return;
        }
        CustomerCreateDTO customerCreateDTO = new CustomerCreateDTO();
        customerCreateDTO.setShopId(shop.getKryShopId().toString());
        customerCreateDTO.setMobile(user.getPhone());
        customerCreateDTO.setName(user.getNickname());
        if(user.getSex() == 0){
            customerCreateDTO.setGender(2);
        }else if(user.getSex() == 1){
            customerCreateDTO.setGender(1);
        }else {
            customerCreateDTO.setGender(0);
        }
        CustomerCreateVO customerCreateVO = kryApiService.createCustomer(shop.getKryShopId(), token, customerCreateDTO);
        if(customerCreateVO == null){
            log.error("手机号{}创建会员失败",user.getPhone());
            throw new PinetException("创建会员失败");
        }
        user.setKryCustomerId(customerCreateVO.getCustomerId());
        vipUserService.save(user);
    }
}
