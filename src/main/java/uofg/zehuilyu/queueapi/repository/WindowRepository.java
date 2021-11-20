package uofg.zehuilyu.queueapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uofg.zehuilyu.queueapi.entity.ServiceWindow;

@Repository
public interface WindowRepository extends JpaRepository<ServiceWindow,Integer> {
}
