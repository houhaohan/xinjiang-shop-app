import com.alibaba.fastjson.JSONObject;
import com.imdada.open.platform.client.internal.req.order.AddOrderReq;
import com.imdada.open.platform.client.internal.req.order.CancelOrderReq;
import com.imdada.open.platform.client.internal.req.order.internal.ProductDetail;
import com.imdada.open.platform.client.internal.resp.order.AddOrderResp;
import com.imdada.open.platform.client.internal.resp.order.CancelOrderResp;
import com.imdada.open.platform.client.order.AddOrderClient;
import com.imdada.open.platform.client.order.CancelOrderClient;
import com.imdada.open.platform.config.Configuration;
import com.imdada.open.platform.exception.RpcException;
import com.pinet.PinetApplication;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;

import static net.sf.jsqlparser.parser.feature.Feature.execute;


@SpringBootTest(classes = PinetApplication.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DaDaTest {


    public static void main(String[] args) throws RpcException {

        //{"order_status":1,"cancel_reason":"","update_time":1694158211,"cancel_from":0,"signature":"d0c956211692ea68d11daf0d81bfdec0","dm_id":0,"is_finish_code":false,"order_id":"2131312311","client_id":"1526014050948743168"}
        createOrder();
//        cancelOrder();
    }

    /**
     * 创建订单
     * @throws RpcException
     */
    private static void createOrder() throws RpcException {
        //创建订单
        ProductDetail productDetail = ProductDetail.builder()
                .skuName("高原风干牛肉干(麻辣味)250克/袋 | 250克")
                .count(2.0)
                .srcProductNo("xxxx")
                .unit("件")
                .build();
        AddOrderReq req = AddOrderReq.builder()
                .shopNo(Configuration.getInstance().getShopNo())
                .originId("2131312311")
                .cityCode("021")
                .cargoPrice(50d)
                .cargoNum(1)
                .tips(1.0)
                .info("设备信息")
                .cargoWeight(3.2)
                .isUseInsurance(0)
                .prepay(0)
                .finishCodeNeeded(0)
                .cargoType(50)
                .invoiceTitle("invoiceTitle")
                .receiverName("张三")
                .receiverAddress("上海市浦东新区东方渔人码头")
                .receiverPhone("15868805739")
                .receiverLat(31.257801)
                .receiverLng(121.538842)
                .callback(Configuration.getInstance().getCallback())
                .pickUpPos("1号货架")
                .productList(Collections.singletonList(productDetail))
                .build();

        //{"deliverFee":13.0,"distance":1097.0,"fee":13.0,"insuranceFee":0.0,"tips":1.0}
        AddOrderResp execute = AddOrderClient.execute(req);
        System.out.println(JSONObject.toJSONString(execute));
    }

    /**
     * 取消订单
     * @throws RpcException
     */
    private static void cancelOrder() throws RpcException {
        CancelOrderReq build = CancelOrderReq.builder()
                .orderId("213131231")
                .cancelReasonId(4)
                .cancelReason("顾客取消订单")
                .build();
        CancelOrderResp execute = CancelOrderClient.execute(build);
        System.out.println(JSONObject.toJSONString(execute));
    }

    private static void queryOrder() throws RpcException {
        CancelOrderReq build = CancelOrderReq.builder()
                .orderId("213131231")
                .cancelReasonId(4)
                .cancelReason("顾客取消订单")
                .build();
        CancelOrderResp execute = CancelOrderClient.execute(build);
        System.out.println(JSONObject.toJSONString(execute));
    }
}
