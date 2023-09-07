

import com.fasterxml.jackson.core.type.TypeReference;
import com.pinet.PinetApplication;
import com.pinet.keruyun.openapi.util.JsonUtil;
import com.pinet.keruyun.openapi.vo.KryResponse;
import com.pinet.keruyun.openapi.vo.OrderCreateVO;
import com.pinet.keruyun.openapi.vo.ScanCodePrePlaceOrderVo;
import com.pinet.keruyun.openapi.vo.TakeoutOrderCreateVo;
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
        Orders orders = ordersService.getById(334);
//        OrderCreateVO orderCreateVO = ordersService.pushOrderToKry(orders);
//        TakeoutOrderCreateVo orderCreateVO = ordersService.takeoutOrderCreate(orders);
//        System.out.println(JsonUtil.toJson(orderCreateVO));
        String s = ordersService.scanCodePrePlaceOrder(orders);
        //{"result":{"formatMsgInfo":"正常成功","msgCode":"SUCCESS","msgInfo":"正常成功","success":"true","data":{"orderNo":"20230831061316000169886642340184","orderBizTime":1693444668302,"paymentUpdateTime":1693444668362,"payStatementStatus":"PAY_SUCCESS"}},"code":0,"message":"成功[OK]","messageUuid":"330ef4f5-ab1b-46a9-9b46-9c4cf49ad1b8","apiMessage":"成功[OK][0]"}
//        String s = ordersService.takeoutOrderCreate(orders);
        System.out.println("结果==");
//        System.out.println(JSONObject.toJSONString(orderCreateVO));
        System.out.println(s);
        //{"domain":"ORDER","eventCode":"ORDER_WAIT_PAY","orderBody":{"bizSecondSource":"WECHAT_MINI_PROGRAM","bizSource":"OPEN_PLATFORM","logisticStatus":"INIT","orderProductConfig":"SCAN_CODE_ORDER_PRE","orderSecondType":"TEA_DRINK","outBizId":"16968314800248340480011","status":"WAIT_PAY"},"orderNo":"20230831061316000166920707540184"}
    }




}
