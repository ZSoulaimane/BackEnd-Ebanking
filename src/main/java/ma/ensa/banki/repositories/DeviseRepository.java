package ma.ensa.banki.repositories;

import ma.ensa.banki.entities.Devise;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeviseRepository extends JpaRepository<Devise, Long> {
	Optional<Devise> findByCode(String code);
	Optional<Devise> findById(Long id);
	Long countByCode(String code);
}
