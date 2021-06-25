package ma.ensa.banki.repositories;

import ma.ensa.banki.entities.Agence;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface AgenceRepository extends JpaRepository<Agence, Long> {

	Optional<Agence> findByNom(String username);

	Optional<Agence> findByTelephone(String telephone);

	Optional<Agence> findByEmail(String email);
}
