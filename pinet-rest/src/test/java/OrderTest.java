import com.alibaba.fastjson.JSONObject;
import com.pinet.PinetApplication;
import com.pinet.rest.entity.CustomerMember;
import com.pinet.rest.entity.Orders;
import com.pinet.rest.service.ICustomerMemberService;
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

    @Resource
    private ICustomerMemberService customerMemberService;

    @Test
    public void test() {
        CustomerMember customerMember =  customerMemberService.getByCustomerId(12011L);
        System.out.println(JSONObject.toJSONString(customerMember));
    }
}
