package application.core.repositories;

import application.core.models.Order;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderRepository extends CrudRepository<Order,Long>{

    @Query("SELECT o FROM Order o WHERE o.traderId = ?1")
    List<Order> findByTraderId(long traderId);
}
