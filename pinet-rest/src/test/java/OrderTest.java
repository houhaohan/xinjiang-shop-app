import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pinet.PinetApplication;
import com.pinet.keruyun.openapi.dto.KryOrderDetailDTO;
import com.pinet.keruyun.openapi.param.DetailDishParam;
import com.pinet.keruyun.openapi.service.IKryApiService;
import com.pinet.keruyun.openapi.type.AuthType;
import com.pinet.keruyun.openapi.vo.DetailDishVO;
import com.pinet.keruyun.openapi.vo.KryResult;
import com.pinet.keruyun.openapi.vo.OrderDetailVO;
import com.pinet.rest.entity.CustomerMember;
import com.pinet.rest.entity.Orders;
import com.pinet.rest.service.ICustomerMemberService;
import com.pinet.rest.service.IOrdersService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * @program: xinjiang-shop-app
 * @description: 订单test
 * @author: hhh
 * @create: 2022-12-16 18:15
 **/
@SpringBootTest(classes = PinetApplication.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrderTest {
    @Resource
    private IOrdersService ordersService;

    @Resource
    private ICustomerMemberService customerMemberService;

    @Autowired
    private IKryApiService kryApiService;

    @Test
    public void test() {
        CustomerMember customerMember =  customerMemberService.getByCustomerId(12011L);
        System.out.println(JSONObject.toJSONString(customerMember));
    }

    @Test
    public void test2(){
        String token = kryApiService.getToken(AuthType.SHOP, 13290197L);
//        KryOrderDetailDTO kryOrderDetailDTO = new KryOrderDetailDTO();
//        kryOrderDetailDTO.setOrderId("20231122061316000176021590390183");
//        OrderDetailVO orderDetail = kryApiService.getOrderDetail(13290198L, token, kryOrderDetailDTO);

        DetailDishParam param = new DetailDishParam();
        param.setDishIds(Arrays.asList("1065757360555"));
        KryResult<List<DetailDishVO>> listKryResult = kryApiService.listQueryDetailDish(13290197L, token, param);
        System.err.println(JSON.toJSONString(listKryResult));
    }
}
