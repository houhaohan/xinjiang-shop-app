import com.alibaba.fastjson.JSON;
import com.pinet.PinetApplication;
import com.pinet.keruyun.openapi.param.CustomerParam;
import com.pinet.keruyun.openapi.param.CustomerPropertyParam;
import com.pinet.keruyun.openapi.service.IKryApiService;
import com.pinet.keruyun.openapi.type.AuthType;
import com.pinet.keruyun.openapi.vo.customer.CustomerPropertyVO;
import com.pinet.keruyun.openapi.vo.customer.CustomerQueryVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @description:
 * @author: chengshuanghui
 * @date: 2024-05-09 16:53
 */
@SpringBootTest(classes = PinetApplication.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class VIPCustomer {
    @Autowired
    private IKryApiService kryApiService;

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
        param.setMobile("15868805739");
        CustomerQueryVO customerQueryVO = kryApiService.queryByMobile(12698040L, token, param);
        System.err.println(JSON.toJSONString(customerQueryVO));
        //{"customerId":"3324054679950","gender":1,"levelDTO":{"levelName":"VIP1","levelNo":"1"},"mobile":"15868805739","state":1}
    }
}
