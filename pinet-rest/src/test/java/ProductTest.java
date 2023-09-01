

import com.pinet.PinetApplication;
import com.pinet.rest.entity.*;
import com.pinet.rest.service.impl.OrderServicesImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = PinetApplication.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductTest {

    @Autowired
    private OrderServicesImpl ordersService;


    @Test
    public void test(){
        Orders orders = ordersService.getById(48);
//        OrderCreateVO orderCreateVO = ordersService.pushOrderToKry(orders);
//        String s = ordersService.takeoutOrderCreate(orders);
        String s = ordersService.scanCodePrePlaceOrder(orders);
        //{"result":{"formatMsgInfo":"正常成功","msgCode":"SUCCESS","msgInfo":"正常成功","success":"true","data":{"orderNo":"20230831061316000169886642340184","orderBizTime":1693444668302,"paymentUpdateTime":1693444668362,"payStatementStatus":"PAY_SUCCESS"}},"code":0,"message":"成功[OK]","messageUuid":"330ef4f5-ab1b-46a9-9b46-9c4cf49ad1b8","apiMessage":"成功[OK][0]"}
//        String s = ordersService.takeoutOrderCreate(orders);
        System.out.println("结果==");
//        System.out.println(JSONObject.toJSONString(orderCreateVO));
        System.out.println(s);
    }

    public static void main(String[] args) {

    }
}
