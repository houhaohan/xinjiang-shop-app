import cn.hutool.core.date.DateUtil;
import com.pinet.PinetApplication;
import com.pinet.common.mq.config.QueueConstants;
import com.pinet.common.mq.util.JmsUtil;
import com.pinet.common.redis.util.RedisUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jms.core.JmsMessagingTemplate;

import javax.annotation.Resource;
import javax.jms.Queue;
import java.util.Date;

/**
 * @program: xinjiang-shop-app
 * @description: activemq test
 * @author: hhh
 * @create: 2022-12-09 14:26
 **/
@SpringBootTest(classes = PinetApplication.class)
public class JmsTest {
    @Resource
    private JmsMessagingTemplate jmsMessagingTemplate;

    @Resource
    private JmsUtil jmsUtil;

    @Test
    public void sendMsg(){
        jmsUtil.sendMsgQueue(QueueConstants.QING_SHI_ORDER_PAY_NAME,"test1111");
    }


    @Test
    public void sendTimeMsg(){
        String date = DateUtil.format(new Date(),"yyyy-MM-dd HH:mm:ss");
        System.out.println(date+"发送消息");
        jmsUtil.delaySend(QueueConstants.QING_SHI_ORDER_PAY_NAME,"测试延迟消息",10*1000L);
    }



}
