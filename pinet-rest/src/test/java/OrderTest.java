import com.alibaba.fastjson.JSONObject;
import com.pinet.PinetApplication;
import com.pinet.rest.entity.Orders;
import com.pinet.rest.service.IOrdersService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @program: xinjiang-shop-app
 * @description: 订单test
 * @author: hhh
 * @create: 2022-12-16 18:15
 **/
@SpringBootTest(classes = PinetApplication.class)
public class OrderTest {
    @Resource
    private IOrdersService ordersService;

    @Test
    public void test() {
        Orders order = ordersService.getById(1);
        System.out.println(JSONObject.toJSONString(order));
    }
}
