package ma.ensa.banki.repositories;

import ma.ensa.banki.entities.Compte;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompteRepository extends JpaRepository<Compte, Long> {

	Optional<Compte> findByNumero(String numero);


}
