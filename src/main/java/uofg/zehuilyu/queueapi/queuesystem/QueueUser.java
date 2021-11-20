package uofg.zehuilyu.queueapi.queuesystem;

import lombok.Data;

@Data
public class QueueUser {
    private Integer queueNo;// 该用户排号
    private String queueIndex;  // 当前位置
    private String totalNum;  // 总排队人数
    private String beforeNum;  // 排在前面的人数
}
