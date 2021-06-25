package ma.ensa.banki.repositories;

import ma.ensa.banki.entities.Compte;
import ma.ensa.banki.entities.VirementMultiBen;
import ma.ensa.banki.entities.VirementMultiple;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VirementMultiBenRepository extends JpaRepository<VirementMultiBen, Long> {
	public List<VirementMultiBen> findAllByCreancier(Compte debiteur);
	public List<VirementMultiple> findAllByVirementMultiple(VirementMultiple v);
}
