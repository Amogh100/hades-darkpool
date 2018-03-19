package application.core.repositories;

import models.entities.Trader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TraderRepository extends JpaRepository<Trader, Long> {
    Trader findByUsername(String username);
}