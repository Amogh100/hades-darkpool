package application.core.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import application.core.models.Trader;
import org.springframework.stereotype.Repository;

@Repository
public interface TraderRepository extends JpaRepository<Trader, Long> {
    Trader findByUsername(String username);
}