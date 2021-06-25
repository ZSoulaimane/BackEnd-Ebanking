package ma.ensa.banki.repositories;

import ma.ensa.banki.entities.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<Utilisateur, Long> {
	Optional<Utilisateur> findByUsername(String username);
	Optional<Utilisateur> findByEmail(String email);
	Optional<Utilisateur> findByCin(String cin);
	Long countByUsername(String username);
	Long countByCin(String cin);

}
