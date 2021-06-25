package ma.ensa.banki.services;

import ma.ensa.banki.entities.Client;
import ma.ensa.banki.entities.Compte;
import ma.ensa.banki.entities.Operateur;
import ma.ensa.banki.entities.Recharge;
import ma.ensa.banki.exceptions.NotFoundException;
import ma.ensa.banki.repositories.RechargeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class RechargeService {

	@Autowired
	RechargeRepository rep;

	@Autowired
	CompteService compteService;

	@Autowired
	ClientService clientService;

	@Autowired
	OperateurService operateurService;

	Logger logger = LoggerFactory.getLogger(RechargeService.class.getName());


	public List<Recharge> getRecharges(Long id) throws NotFoundException {

		List<Recharge> recharges = new ArrayList<>();

		if (id != null)
			recharges.add(rep.findById(id).orElseThrow(() -> new NotFoundException("Aucune recharge avec l'id " + id + " trouvé")));

		else
			recharges = rep.findAll();

		if (recharges.isEmpty()) throw new NotFoundException("Aucune recharge trouvé");
		return recharges;
	}


	public void addRecharge(Recharge recharge) throws Exception {

		Compte compteClient = compteService.getComptes(recharge.getCompte().getId()).get(0);

		Client client = clientService.getByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		Client proprietaire = clientService.getClients(compteClient.getProprietaire().getId()).get(0);

		if (client != proprietaire) throw new Exception("Ce compte ne vous appartient pas !");


		Operateur operateur = operateurService.getOperateur(recharge.getOperateur().getId());
//		Compte compteOperateur = compteService.getComptes(operateur.getComptes().get(0).getId()).get(0);

		if (compteClient.getSolde() < recharge.getSommeEnv())
			throw new Exception("Vous n'avez pas de solde suffisant ! ");

		recharge.setDate(LocalDateTime.now());

		rep.save(recharge);

		compteClient.setSolde(compteClient.getSolde() - recharge.getSommeEnv());

//		compteOperateur.setSolde(compteOperateur.getSolde() + recharge.getSommeRecu());

		compteService.rep.save(compteClient);
//		compteService.rep.save(compteOperateur);
//		
//		Twilio.init("","");
//		Message message = Message.creator(
//		    new PhoneNumber("+212642330266"),
//		    new PhoneNumber("+212636196050"),
//		    "Vous avez effectué une recharge")
//		.create();


		logger.debug("Le client " + client.getNom() + " " + client.getPrenom() + " ayant le Username " + client.getUsername()
				+ " a effectué une recharge de " + recharge.getSommeEnv() + compteClient.getDevise().getCode() + "vers le numero "
				+ recharge.getTelephone() + " de l'opérateur " + operateur.getNom() + " à la date " + recharge.getDate());

	}


}
