package application.core.repositories;

import models.entities.Trade;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TradeRepository extends CrudRepository<Trade,Long> {

    @Query("SELECT t FROM Trade t WHERE t.trader1Id = ?1 OR t.trader2Id = ?1")
    List<Trade> findByTraderId(long traderId);

}
