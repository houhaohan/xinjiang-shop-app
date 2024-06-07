import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pinet.PinetApplication;
import com.pinet.core.util.BigDecimalUtil;
import com.pinet.core.util.DateUtils;
import com.pinet.keruyun.openapi.dto.DirectChargeDTO;
import com.pinet.keruyun.openapi.param.CustomerParam;
import com.pinet.keruyun.openapi.param.CustomerPropertyParam;
import com.pinet.keruyun.openapi.service.IKryApiService;
import com.pinet.keruyun.openapi.type.AuthType;
import com.pinet.keruyun.openapi.vo.customer.CustomerPropertyVO;
import com.pinet.keruyun.openapi.vo.customer.CustomerQueryVO;;
import com.pinet.keruyun.openapi.vo.customer.DirectChargeVO;
import com.pinet.rest.entity.*;
import com.pinet.rest.mapper.OrdersMapper;
import com.pinet.rest.mapper.ShopMapper;
import com.pinet.rest.service.ICustomerBalanceService;
import com.pinet.rest.service.ICustomerScoreService;
import com.pinet.rest.service.IVipShopBalanceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * @description:
 * @author: chengshuanghui
 * @date: 2024-05-09 16:53
 */
@SpringBootTest(classes = PinetApplication.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class VIPCustomer {
    @Autowired
    private IKryApiService kryApiService;
    @Autowired
    private ShopMapper shopMapper;

    @Test
    public void test(){
        String token = kryApiService.getToken(AuthType.BRAND, 12698040L);

        CustomerPropertyParam param = new CustomerPropertyParam();
        param.setCustomerId("3324054679950");
        param.setShopId("13290197");
        CustomerPropertyVO customerPropertyVO = kryApiService.queryCustomerProperty(12698040L, token, param);
        System.err.println(JSON.toJSONString(customerPropertyVO));
        //{"customerDTO":{"customerId":"3324054679950","mobile":"15868805739","state":1},"normalVoucherInstanceCount":0,"pointAccountDTO":{"remainAvailableValue":0},"posCardDTOList":[{"cardId":"233608741961020434","cardType":"MEMBER","posRechargeAccountList":[{"remainAvailableValue":{"totalValue":10000.0,"realValue":10000.0,"giftValue":0.0}}],"status":"ACTIVED"}]}
    }


    @Test
    public void queryCustomer(){
        String token = kryApiService.getToken(AuthType.BRAND, 12698040L);

        CustomerParam param = new CustomerParam();
        param.setMobile("15868805730");
        CustomerQueryVO customerQueryVO = kryApiService.queryByMobile(12698040L, token, param);
        System.err.println(JSON.toJSONString(customerQueryVO));
        //{"customerId":"3324054679950","gender":1,"levelDTO":{"levelName":"VIP1","levelNo":"1"},"mobile":"15868805739","state":1}
    }

    /**
     * 充值
     */
    @Test
    public void recharge(){

        Shop shop = shopMapper.selectById(24);
//        String token = kryApiService.getToken(AuthType.SHOP, shop.getKryShopId());
        DirectChargeDTO directChargeDTO = new DirectChargeDTO();
        directChargeDTO.setShopId(shop.getKryShopId().toString());
        directChargeDTO.setBizDate(DateUtils.getTime());
        directChargeDTO.setOperatorName("管理员");
        directChargeDTO.setOperatorId("3324054679950");
        directChargeDTO.setRealValue(1L);
        directChargeDTO.setCustomerId("x");
        directChargeDTO.setBizId(IdUtil.simpleUUID());
        DirectChargeVO directChargeVO = kryApiService.directCharge(12698040L, "ae5f960130e9d2ed01b406be6988b576", directChargeDTO);
        if(directChargeVO == null){
            System.err.println("空空如也");
        }else {
            System.err.println(JSON.toJSONString(directChargeVO));
        }

    }


    @Autowired
    private ICustomerScoreService customerScoreService;
    @Autowired
    private IVipShopBalanceService vipShopBalanceService;
    @Autowired
    private ICustomerBalanceService customerBalanceService;
    @Autowired
    OrdersMapper ordersMapper;

    @Test
    public void syncScore(){
        List<CustomerBalance> list = customerBalanceService.list();
        List<CustomerScore> scoreList = new ArrayList<>();
        for(CustomerBalance customerBalance : list){
            CustomerScore customerScore = new CustomerScore();
            customerScore.setCreateBy(1L);
            customerScore.setScore(customerBalance.getScore().doubleValue());
            customerScore.setCustomerId(customerBalance.getCustomerId());
            scoreList.add(customerScore);
        }
        customerScoreService.saveBatch(scoreList);

    }

    @Test
    public void syncBalance(){
        List<CustomerBalance> list = customerBalanceService.list();
        List<VipShopBalance> shopBalances = new ArrayList<>();
        for(CustomerBalance customerBalance : list){
            QueryWrapper<Orders> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("customer_id",customerBalance.getCustomerId());
            List<Orders> orders = ordersMapper.selectList(queryWrapper);
            if(CollectionUtils.isEmpty(orders)){
                continue;
            }
            VipShopBalance vipShopBalance = new VipShopBalance();
            vipShopBalance.setCustomerId(customerBalance.getCustomerId());
            vipShopBalance.setShopId(orders.get(0).getShopId());
            vipShopBalance.setAmount(customerBalance.getAvailableBalance());
            shopBalances.add(vipShopBalance);
        }
        vipShopBalanceService.saveBatch(shopBalances);
    }

}
