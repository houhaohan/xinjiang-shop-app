import com.alibaba.fastjson.JSONObject;
import com.pinet.PinetApplication;
import com.pinet.rest.entity.dto.OrderListDto;
import com.pinet.rest.entity.vo.OrderListVo;
import com.pinet.rest.service.IOrderService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

/**
 * @program: xinjiang-shop-app
 * @description:
 * @author: hhh
 * @create: 2022-12-15 15:40
 **/
@SpringBootTest(classes = PinetApplication.class)
public class OrderTest {
    @Resource
    IOrderService orderService;

    @Test
    public void test() {
        OrderListDto dto = new OrderListDto();
        dto.setPageNum(1);
        dto.setPageSize(1);
        List<OrderListVo> orderListVos = orderService.orderList(dto);
        System.out.println(JSONObject.toJSONString(orderListVos));
    }
}
