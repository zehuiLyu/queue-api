package uofg.zehuilyu.queueapi;

import com.alibaba.fastjson.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import uofg.zehuilyu.queueapi.controller.CustomerController;
import uofg.zehuilyu.queueapi.controller.WindowController;
import uofg.zehuilyu.queueapi.entity.Customer;
import uofg.zehuilyu.queueapi.entity.ServiceWindow;
import uofg.zehuilyu.queueapi.queuesystem.QueueService;

import java.util.Iterator;
import java.util.Set;

@SpringBootTest
@RunWith(SpringRunner.class)
class QueueApiApplicationTests {
    @Autowired
    CustomerController customerController;
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    QueueService queueService;
    @Autowired
    WindowController windowController;


    @Test
    void contextLoads() {
        Customer customer1 = JSONObject.toJavaObject(customerController.getUsersById(100001).getBody(),Customer.class);;
        System.out.println(customer1.toString());

    }
    @Test
    void windowTest(){
        ServiceWindow window=JSONObject.toJavaObject(windowController.getWindowById(1).getBody(), ServiceWindow.class);
        System.out.println(window.toString());
    }
    @Test
    void redisTest(){

        queueService.push("queueTest","100002");
        queueService.push("queueTest","100001");
        queueService.push("queueTest","100009");
        queueService.push("queueTest","100008");
        queueService.push("queueTest","100004");
        queueService.push("queueTest","100011");
        queueService.push("queueTest","100012");
        queueService.push("queueTest","100013");
        queueService.push("queueTest","100005");
        Set<String> testSet=redisTemplate.opsForZSet().range(queueService.getQueryZsetName("queueTest"),0,-1);
        Iterator<String> iterator = testSet.iterator();
        while (iterator.hasNext()) {
            Customer usersById = JSONObject.toJavaObject(customerController.getUsersById(Integer.parseInt(iterator.next())).getBody(),Customer.class);
            System.out.println("userid is"+usersById.getId()+" level is "+usersById.getCustomer_level());
        }
        System.out.println("========================================================");
        queueService.exit("queueTest","100011");
        queueService.pop("queueTest");
        Set<String> testSet2=redisTemplate.opsForZSet().range(queueService.getQueryZsetName("queueTest"),0,-1);
        Iterator<String> iterator2 = testSet2.iterator();
        while (iterator2.hasNext()) {
            Customer usersById = JSONObject.toJavaObject(customerController.getUsersById(Integer.parseInt(iterator2.next())).getBody(),Customer.class);
            System.out.println("userid is"+usersById.getId()+" level is "+usersById.getCustomer_level());
        }







    }
//    @Autowired


}
