package ma.ensa.banki.repositories;

import ma.ensa.banki.entities.Agent;
import ma.ensa.banki.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {

	Optional<Client> findByUsername(String username);

	Optional<Client> findByCin(String username);

	List<Client> findAllByCreationAgent(Agent agent);
}
