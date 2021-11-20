package uofg.zehuilyu.queueapi.entity;

import lombok.Data;


import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;


@Entity
@Table(name = "queue_customer")
@Data
@EntityListeners(AuditingEntityListener.class)

public class Customer {

    private Integer id;
    private Integer customer_level;
    private String name;
    private Date create_time;
    private Date update_time;





    public void setId(Integer id) {
        this.id = id;
    }

    @Id
    public Integer getId() {
        return id;
    }


}
