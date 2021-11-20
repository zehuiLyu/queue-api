package uofg.zehuilyu.queueapi.entity;

import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "service_window")
@Data
@EntityListeners(AuditingEntityListener.class)
public class ServiceWindow {

    @Id
    private Integer id;
    private String name;
    private Integer status;
    private Date create_time;
    private Date update_time;

}
