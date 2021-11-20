package uofg.zehuilyu.queueapi.queuesystem;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.Set;
@Service
public class QueueService {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private  NumberManager numberManager;


    /**
     * 判断Zset集合中是否有某个元素
     * @return
     */
    public boolean isExistZset(String zsetName,String value) {
        boolean result = false;
        if(redisTemplate.opsForZSet().score(zsetName,value) != null)
            result = true;
        return result;
    }
    /**
     * 根据StoreId获取该店排队的Zset名
     * @param storeId
     * @return
     */
    public String getQueryZsetName(String storeId) {
        return "query" + "-" + "zset" + "-" + storeId;
    }

    /**
     * 根据StoreId获取排队的list名
     * @param storeId
     * @return
     */
    public String getQueryListName(String storeId) {
        return "query" + "-" + "list" + "-" + storeId;
    }
    /**
     * 获取队列当前排队总人数
     * @param queryZsetName
     * @return
     */
    public long curQueryNumber(String queryZsetName) {
        return redisTemplate.opsForZSet().zCard(queryZsetName);
    }

    /**
     * 入队操作
     * @param userId  排队用户Id
     * @return 用户实时排队情况
     */
    public JSONObject push(String storeId, String userId) {
        String queryZsetName = getQueryZsetName(storeId);
        String queryListName = getQueryListName(storeId);

        QueueDetail queryDetail = null;
        QueueUser queryUser = null;
        JSONObject resultJson = new JSONObject();

        if(storeId != null && !storeId.equals("") &&userId != null && !userId.equals("")) {
            if(! isExistZset(queryZsetName,userId)) {
                int score=numberManager.generateNewNumber(Integer.parseInt(userId));
                if(score==-1) {resultJson.put("msg","the user is not exist!");return resultJson;}
                queryDetail = new QueueDetail();
                queryDetail.setQueryName(queryZsetName);
                queryDetail.setQueueNo((int) score);
                queryDetail.setQueryUserId(userId);
                String queryDetailStr =  JSONObject.toJSON(queryDetail).toString();
                redisTemplate.opsForList().rightPush(queryListName,queryDetailStr);
                redisTemplate.opsForZSet().add(queryZsetName,userId,score);
                queryUser = new QueueUser();
                queryUser.setQueueNo((int) score);
                queryUser.setTotalNum(String.valueOf(curQueryNumber(queryZsetName)));
                queryUser.setQueueIndex(String.valueOf(redisTemplate.opsForZSet().rank(queryZsetName,userId) + 1));
                queryUser.setBeforeNum(String.valueOf(redisTemplate.opsForZSet().rank(queryZsetName,userId)));
                resultJson = (JSONObject) JSONObject.toJSON(queryUser);
                return resultJson;
            } else {
                resultJson.put("msg","The customer is already in the queue. Do not queue again.");

                return  resultJson;
            }
        } else {
            resultJson.put("msg","storeId and userId cannot be null!");
            return  resultJson;
        }
    }

    /**
     * 出队-可随机从任意位置出队
     * @param storeId
     * @param userId
     * @return 返回用户的详细排队数据
     */
    public JSONObject exit(String storeId, String userId) {

        JSONObject resultJson = new JSONObject();
        if(storeId != null && !storeId.trim().equals("") && userId != null && !userId.trim().equals("")) {
            String queryZsetName = getQueryZsetName(storeId);
            String queryListName = getQueryListName(storeId);

            if(isExistZset(queryZsetName,userId)) {
                long currIndex = redisTemplate.opsForZSet().rank(queryZsetName,userId);
                String removeValue = (String) redisTemplate.opsForList().index(queryListName,currIndex);
                redisTemplate.opsForZSet().remove(queryZsetName,userId);  // Remove user queuing data from Zset
                redisTemplate.opsForList().remove(queryListName,1,removeValue); // Remove user queuing details from list
                resultJson = JSONObject.parseObject(removeValue);
                return resultJson;
            } else {
                resultJson.put("msg","If the queuing status of the user is not found, please queue up first！");
                return resultJson;
            }
        } else {
            resultJson.put("msg","storeId and userId cannot be null！");
            return resultJson;
        }
    }

    /**
     * 从排队最前端出队
     * @param storeId
     * @return
     */
    public JSONObject pop(String storeId) {

        JSONObject resultJson = new JSONObject();
        if(storeId != null && !storeId.trim().equals("")) {

            String queryZsetName = getQueryZsetName(storeId);
            String queryListName = getQueryListName(storeId);
            Set<String> set=redisTemplate.opsForZSet().range(queryZsetName,0,0);
            String userId=null;

            if(set!=null&&!set.isEmpty()) {
                Iterator<String> iterator = set.iterator();
                while (iterator.hasNext()) {
                    userId = iterator.next();

                }
                String removeValue = (String) redisTemplate.opsForList().index(queryListName,0);
                redisTemplate.opsForZSet().remove(queryZsetName,userId); // 移除Zset的用户排队数据
                redisTemplate.opsForList().remove(queryListName,1,removeValue); // 移除list中的用户排队详情
                resultJson = JSONObject.parseObject(removeValue);

            } else {
                resultJson.put("msg","未查到该用户的排队情况，请先排队！");
            }
            return resultJson;
        } else {
            resultJson.put("msg","storeId can not be null！");
            return resultJson;
        }
    }
    /**
     * 实时排队情况
     * @param storeId
     * @param userId
     * @return
     */
    public JSONObject realTimeQuery(String storeId,String userId) {
        String queryZsetName = getQueryZsetName(storeId);
        QueueUser queryUser = null;
        JSONObject resultJson = new JSONObject();

        if(storeId != null && !storeId.equals("") && userId != null && !userId.equals("")) {

            if(isExistZset(queryZsetName,userId)) {
                queryUser = new QueueUser();
                double score = redisTemplate.opsForZSet().score(queryZsetName,userId);
                queryUser.setTotalNum(String.valueOf(curQueryNumber(queryZsetName)));
                queryUser.setQueueIndex(String.valueOf(redisTemplate.opsForZSet().rank(queryZsetName,userId) + 1));
                queryUser.setBeforeNum(String.valueOf(redisTemplate.opsForZSet().rank(queryZsetName,userId)));
                queryUser.setQueueNo((int) score);
                resultJson = (JSONObject) JSONObject.toJSON(queryUser);
                return resultJson;
            } else {
                resultJson.put("msg","未查到该用户的排队情况，请先排队！");
                return resultJson;
            }
        } else {
            resultJson.put("msg","店铺ID和用户ID不能为空！");
            return  resultJson;
        }
    }
    /**
     * clear the queue of storeId
     * @param storeId
     * @return JSONObject of message
     */
    public JSONObject clear(String storeId){
        JSONObject resultJson = new JSONObject();
        if(storeId != null && !storeId.equals("")) {
            String queryZsetName = getQueryZsetName(storeId);
            String queryListName = getQueryListName(storeId);
            boolean result1 = redisTemplate.delete(queryZsetName);
            boolean result2 = redisTemplate.delete(queryListName);
            if(result1 && result2) {
                    resultJson.put("msg","Queue data cleared!");
                    return resultJson;
                } else {
                    resultJson.put("msg","The queue has been emptied! No need to empty again!");
                    return resultJson;
                }
            } else {
                resultJson.put("msg","storeId cannot be empty!");
                return resultJson;
            }

    }

}
