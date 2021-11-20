package uofg.zehuilyu.queueapi.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;
import uofg.zehuilyu.queueapi.queuesystem.QueueService;

/**
 * The controller class for queues mainly provides methods that show the total number of people in a queue;
 * Customers enqueue, dequeue and exit by self.
 * and display the number of people ahead of you in the queue
 */
@Api(value = "Queue Management", tags = "Queue Management API", description = "Queue Service")
@RestController
@RequestMapping("/api/v1/queue")
public class QueueController {
    @Autowired
    private QueueService queryService;

    /**
     * The number of all customers in the queue
     * @param storeId the id of queue
     * @return A JSONObject of key=String "num" and value= int num.
     */
    @ApiOperation(value = "The number of all customers in the queue", notes = "GetCustomerNumber", response = JSONObject.class, produces = "application/json")
    @GetMapping("/curQueryNum")
    public JSONObject curQueryNum(String storeId){
        String queryZsetName = queryService.getQueryZsetName(storeId);
        long num=queryService.curQueryNumber(queryZsetName);
        JSONObject response=new JSONObject();
        response.put("num",num);
        return response;
    }

    /**
     * If the user exists, the merge succeeds, and a JSONObject of type QueryUSer for that user is returned
     * If storeId and userId are null or empty strings, or the user does not exist, joining the queue fails,
     * and a JSONObject is returned with key as MSG and value as a string describing the failure cause
     * @param storeId the id of queue
     * @param userId id of customer
     * @return JSONObject of QueryUser or the failure cause
     */
    @ApiOperation(value = "Queues the customer into the specified queue",notes = "QueueCustomer",response = JSONObject.class, produces = "application/json" )
    @PatchMapping("/push")
    public JSONObject push(String storeId, String userId) {
        return queryService.push(storeId,userId);
    }

    /**
     * Unqueue the first customer and return that customer's JSONObject
     * @param storeId the id of queue
     * @return On success, the JSONObject of the customer is returned.
     * On failure, the JSONObject is returned with key "MSG" and value as the cause of the failure
     */
    @ApiOperation(value = "Dequeue the customer with rank 1 and return the JSONObject of the customer",
            notes = "PopCustomer",response = JSONObject.class, produces = "application/json" )
    @PatchMapping("/pop")
    public JSONObject pop(String storeId) {
        return  queryService.pop(storeId);
    }

    /**
     * Unqueue the first customer and return that customer's JSONObject
     * @param storeId the id of queue
     * @param userId the id of customer
     * @return On success, the JSONObject of the customer is returned.
     *         On failure, the JSONObject is returned with key "MSG" and value as the cause of the failure
     */
    @ApiOperation(value = "Dequeue the specified customer from its queue, return the JSONObject of the customer",
            notes = "PopCustomer",response = JSONObject.class, produces = "application/json" )
    @PatchMapping("/exit")
    public JSONObject exit(String storeId, String userId){return queryService.exit(storeId, userId);}

    /**
     * Check the status of the customer in the queue
     * @param storeId the id of queue
     * @param userId the id of customer
     * @return On success, the JSONObject of the customer is returned.
     *         On failure, the JSONObject is returned with key "MSG" and value as the cause of the failure
     */
    @ApiOperation(value = "Customer status in the queue", notes = "GetCustomerStatusJson", response = JSONObject.class, produces = "application/json")
    @GetMapping("/realTimeQuery")
    public JSONObject realTimeQuery(String storeId, String userId) {
        return  queryService.realTimeQuery(storeId,userId);
    }

    /**
     *
     * @param storeId the id of queue
     * @return the message, key is "msg"
     */
    @ApiOperation(value = "clear the data in queue", notes = "ClearQueue",response = JSONObject.class,produces = "application/json" )
    @DeleteMapping("/clear")
    public JSONObject clearQueueById(String storeId){
        return queryService.clear(storeId);
    }

//    @RequestMapping(value = "/findByPage")
//    public JSONObject findByPage(Integer page, Integer limit, String storeId) {
//        return queryService.findByPage(page, limit,storeId);
//    }
//
//    @RequestMapping(value = "/emptyQuery")
//    public JSONObject emptyQuery(String storeId) {
//        return queryService.emptyQuery(storeId);
//    }
}
