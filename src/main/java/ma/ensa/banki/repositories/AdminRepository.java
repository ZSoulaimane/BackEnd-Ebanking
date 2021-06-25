package ma.ensa.banki.repositories;

import ma.ensa.banki.entities.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {
	Optional<Admin> findByUsername(String username);

	Optional<Admin> findByCin(String cin);
}
