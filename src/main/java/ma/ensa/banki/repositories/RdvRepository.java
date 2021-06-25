package ma.ensa.banki.repositories;

import ma.ensa.banki.entities.Agence;
import ma.ensa.banki.entities.Client;
import ma.ensa.banki.entities.Rdv;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RdvRepository extends JpaRepository<Rdv, Long> {
	List<Rdv> findAllByOrderByDateRdvAscHeureRdvAsc();
	Optional<Rdv> findById(Long id);
	List<Rdv> findAllByClient(Client client);
	List<Rdv> findAllByAgenceOrderByDateRdvAscHeureRdvAsc(Agence agence);
}
