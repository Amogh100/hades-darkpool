package application.core.repositories;

import models.entities.Order;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository class in spring to query/insert data from/to Postgres
 */
@Repository
public interface OrderRepository extends CrudRepository<Order,Long>{

    @Query("SELECT o FROM Order o WHERE o.traderId = ?1")
    List<Order> findByTraderId(long traderId);

    @Query("SELECT o FROM Order o WHERE o.traderId = ?1 AND o.filled = false")
    List<Order> findOpenOrders(long traderId);
}
