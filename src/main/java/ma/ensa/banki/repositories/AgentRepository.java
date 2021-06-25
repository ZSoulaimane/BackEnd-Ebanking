package ma.ensa.banki.repositories;

import ma.ensa.banki.entities.Agent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AgentRepository extends JpaRepository<Agent, Long> {
	Optional<Agent> findByUsername(String username);
	Optional<Agent> findByCin(String username);
}
