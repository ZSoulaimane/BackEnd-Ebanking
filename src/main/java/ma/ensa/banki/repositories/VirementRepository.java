package ma.ensa.banki.repositories;


import ma.ensa.banki.entities.Virement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VirementRepository extends JpaRepository<Virement, Long> {


}
