package ma.ensa.banki.services;

import ma.ensa.banki.entities.*;
import ma.ensa.banki.exceptions.NotFoundException;
import ma.ensa.banki.repositories.VirementMultiBenRepository;
import ma.ensa.banki.repositories.VirementMultipleRepository;
import ma.ensa.banki.repositories.VirementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class VirementMultipleService {
	@Autowired
	VirementMultipleRepository vrmnMultiRepo;
	@Autowired
	VirementMultiBenRepository vrmnMultiBenRepo;
	@Autowired
	VirementMultiBenRepository repoben;
	@Autowired
	CompteService compteService;
	@Autowired
	ClientService clientService;
	@Autowired
	DeviseService deviseService;
	@Autowired
	VirementRepository virementRepo;

	public void addVirementMultiple(VirementMultiple virementMultiple) {
		vrmnMultiRepo.save(virementMultiple);
		virementMultiple.setDate(LocalDateTime.now());
		Compte debiteur = virementMultiple.getDebiteur();
		for (VirementMultiBen ben : virementMultiple.getCreanciers()) {
			ben.setVirementMultiple(virementMultiple);
			repoben.save(ben);
			Compte creancier = ben.getCreancier();
			Devise src = debiteur.getDevise();
			Devise dest = creancier.getDevise();
			double montantEnvoye = ben.getMontant();
			double montantRecu = deviseService.getRate(src.getCode(), dest.getCode()) * montantEnvoye;
			debiteur.setSolde(debiteur.getSolde() - montantEnvoye);
			creancier.setSolde(creancier.getSolde() + montantRecu);
			compteService.rep.save(creancier);

			Virement vir = new Virement();
			vir.setDate(LocalDateTime.now());
			vir.setDebiteur(debiteur);
			vir.setCreancier(creancier);
			vir.setSommeEnv(montantEnvoye);
			vir.setSommeRecu(montantRecu);
			virementRepo.save(vir);
		}
		compteService.rep.save(debiteur);
	}
}

