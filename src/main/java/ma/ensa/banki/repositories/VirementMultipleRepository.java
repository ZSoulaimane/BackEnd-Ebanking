package ma.ensa.banki.repositories;

import ma.ensa.banki.entities.Compte;
import ma.ensa.banki.entities.VirementMultiple;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VirementMultipleRepository extends JpaRepository<VirementMultiple, Long> {
	List<VirementMultiple> findAllByDebiteur(Compte debiteur);
	List<VirementMultiple> findAllById(Long id);
}
