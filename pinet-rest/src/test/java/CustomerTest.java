import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pinet.PinetApplication;
import com.pinet.rest.entity.CustomerBalance;
import com.pinet.rest.entity.CustomerScore;
import com.pinet.rest.entity.Orders;
import com.pinet.rest.entity.VipShopBalance;
import com.pinet.rest.service.ICustomerBalanceService;
import com.pinet.rest.service.ICustomerScoreService;
import com.pinet.rest.service.IOrdersService;
import com.pinet.rest.service.IVipShopBalanceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @description:
 * @author: chengshuanghui
 * @date: 2024-06-19 14:35
 */
@SpringBootTest(classes = PinetApplication.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CustomerTest {
    @Autowired
    private ICustomerBalanceService customerBalanceService;
    @Autowired
    private ICustomerScoreService customerScoreService;
    @Autowired
    private IVipShopBalanceService vipShopBalanceService;
    @Autowired
    private IOrdersService ordersService;

    @Test
    public void syncBalance(){
        QueryWrapper<CustomerBalance> queryWrapper = new QueryWrapper<>();
        queryWrapper.inSql("customer_id","select DISTINCT(customer_id) from `qingshi`.orders where del_flag = 0");
        List<CustomerBalance> list = customerBalanceService.list(queryWrapper);
        for(CustomerBalance customerBalance : list){
            List<VipShopBalance> vipShopBalances = vipShopBalanceService.getByCustomerId(customerBalance.getCustomerId());
            VipShopBalance vipShopBalance ;
            if(!CollectionUtils.isEmpty(vipShopBalances)){
//                vipShopBalance = vipShopBalances.get(0);
//                vipShopBalance.setAmount(customerBalance.getBalance());
//                vipShopBalanceService.updateById(vipShopBalance);
            }else {
                QueryWrapper<Orders> queryWrapper2 = new QueryWrapper<>();
                queryWrapper2.eq("customer_id",customerBalance.getCustomerId());
                queryWrapper2.orderByDesc("id");
                queryWrapper2.last("limit 1");
                Orders orders = ordersService.getOne(queryWrapper2);
                if(orders == null){
                    continue;
                }
                vipShopBalance = new VipShopBalance();
                vipShopBalance.setShopId(orders.getShopId());
                vipShopBalance.setCustomerId(customerBalance.getCustomerId());
                vipShopBalance.setAmount(customerBalance.getBalance());
                vipShopBalanceService.save(vipShopBalance);
            }
        }

    }

    @Test
    public void syncScore(){
        List<CustomerBalance> list = customerBalanceService.list();
        for(CustomerBalance customerBalance : list){
            CustomerScore customerScore = customerScoreService.getByCustomerId(customerBalance.getCustomerId());
            if(customerScore != null){
                customerScore.setScore(customerBalance.getScore() +customerScore.getScore());
                customerScoreService.updateById(customerScore);
            }else {

                customerScore = new CustomerScore();
                customerScore.setCustomerId(customerBalance.getCustomerId());
                customerScore.setScore(customerBalance.getScore().doubleValue());
                customerScoreService.save(customerScore);
            }
        }

    }
}
