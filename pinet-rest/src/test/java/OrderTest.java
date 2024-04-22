import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.binarywang.wxpay.bean.transfer.TransferBatchesRequest;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.pinet.PinetApplication;
import com.pinet.keruyun.openapi.dto.KryOrderDetailDTO;
import com.pinet.keruyun.openapi.dto.PerformanceCallDTO;
import com.pinet.keruyun.openapi.param.DetailDishParam;
import com.pinet.keruyun.openapi.service.IKryApiService;
import com.pinet.keruyun.openapi.type.AuthType;
import com.pinet.keruyun.openapi.vo.DetailDishVO;
import com.pinet.keruyun.openapi.vo.KryResult;
import com.pinet.keruyun.openapi.vo.OrderDetailVO;
import com.pinet.rest.entity.CustomerMember;
import com.pinet.rest.service.ICustomerCouponService;
import com.pinet.rest.service.ICustomerMemberService;
import com.pinet.rest.service.IOrdersService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
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

    @Resource
    private ICustomerCouponService customerCouponService;

    @Resource
    private WxPayService miniPayService;

    @Test
    public void test() {
        CustomerMember customerMember =  customerMemberService.getByCustomerId(12011L);
        System.out.println(JSONObject.toJSONString(customerMember));
    }

    @Test
    public void test2(){
        String token = kryApiService.getToken(AuthType.SHOP, 13290197L);
        KryOrderDetailDTO kryOrderDetailDTO = new KryOrderDetailDTO();
        kryOrderDetailDTO.setOrderId("20231220061316000116975405200184");
        OrderDetailVO orderDetail = kryApiService.getOrderDetail(13290197L, token, kryOrderDetailDTO);

//        DetailDishParam param = new DetailDishParam();
//        param.setDishIds(Arrays.asList("1065757360555"));
//        KryResult<List<DetailDishVO>> listKryResult = kryApiService.listQueryDetailDish(13290197L, token, param);
        System.err.println(JSON.toJSONString(orderDetail));
    }


    @Test
    public void test3(){
        customerCouponService.grantNewCustomerCoupon(12588L);
    }


    /**
     * 叫号
     */
    @Test
    public void performanceCall(){
        PerformanceCallDTO dto = new PerformanceCallDTO();
        dto.setOrderSource("OPEN_PLATFORM");
        dto.setOrderId("20231219061316000109039670780184");

        ordersService.performanceCall(dto);
    }

    /**
     * 商家转账到零钱
     * @throws WxPayException
     */
    @Test
    public void transferBatches() throws WxPayException {
        TransferBatchesRequest request = new TransferBatchesRequest();
        request.setAppid("");
        request.setOutBatchNo("");
        request.setBatchName("");
        request.setBatchRemark("商家提现");
        request.setTotalAmount(1);
        request.setTotalNum(1);

        List<TransferBatchesRequest.TransferDetail> transferDetails = new ArrayList<>();
        TransferBatchesRequest.TransferDetail transferDetail = new TransferBatchesRequest.TransferDetail();
        transferDetail.setOutDetailNo("");
        transferDetail.setTransferAmount(1);
        transferDetail.setTransferRemark("");
        transferDetail.setUserName("");
        transferDetail.setOpenid("");
        transferDetails.add(transferDetail);
        request.setTransferDetailList(transferDetails);
        miniPayService.getTransferService().transferBatches(request);
    }



    /**
     * 菜品详情
     */
    @Test
    public void dishDetail(){

        String token = kryApiService.getToken(AuthType.SHOP, 19844001L);
        DetailDishParam param = new DetailDishParam();
        param.setDishIds(Arrays.asList("1058678830555"));
        KryResult<List<DetailDishVO>> listKryResult = kryApiService.listQueryDetailDish(19844001L, token, param);
        System.out.println(JSON.toJSONString(listKryResult));
    }
}
