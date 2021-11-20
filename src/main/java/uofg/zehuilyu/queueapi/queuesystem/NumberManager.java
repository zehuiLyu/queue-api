package uofg.zehuilyu.queueapi.queuesystem;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import uofg.zehuilyu.queueapi.controller.CustomerController;
import uofg.zehuilyu.queueapi.entity.Customer;


@Service
@Scope(value = ConfigurableBeanFactory. SCOPE_SINGLETON)
public class NumberManager{
    @Autowired
    CustomerController customerController;



    int lastSupremeNumber=100000;
    int lastGoldNumber=200000;
    int lastCommonNumber=300000;


    public synchronized Integer generateNewNumber(int id) {
        Customer customer= JSONObject.toJavaObject(customerController.getUsersById(id).getBody(),Customer.class);

        if (customer!=null) {
            int level=customer.getCustomer_level();
            int lastNumber=-1;
            if(level==1){
                lastNumber=lastSupremeNumber++;
            }
            else if(level==2){
                lastNumber=lastGoldNumber++;

            }
            else if(level==3){
                lastNumber=lastCommonNumber++;
            }

            return lastNumber;
        }else {
            return -1;
        }
    }

}
