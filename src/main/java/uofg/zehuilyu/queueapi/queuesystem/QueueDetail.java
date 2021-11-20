package uofg.zehuilyu.queueapi.queuesystem;

import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Date;
@Data
public class QueueDetail {
    private String queryName;  // 队列名
    private Integer queueNo;  // 排队号码
    private String queryTime;  // 开始排队时间
    private String queryUserId;  // 排队用户ID

    public QueueDetail() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.queryTime = sdf.format(new Date());
    }
}
