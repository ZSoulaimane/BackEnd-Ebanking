package ma.ensa.banki.services;

import ma.ensa.banki.entities.Admin;
import ma.ensa.banki.entities.Agence;
import ma.ensa.banki.entities.Agent;
import ma.ensa.banki.entities.Client;
import ma.ensa.banki.exceptions.AlreadyExistsException;
import ma.ensa.banki.exceptions.NotFoundException;
import ma.ensa.banki.repositories.AgenceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AgenceService {
	@Autowired
	AgenceRepository agenceRepo;
	@Autowired
	AdminService adminService;
	Logger logger = LoggerFactory.getLogger(AgenceService.class.getName());

	public List<Agence> getAgences(Long id) throws NotFoundException {
		List<Agence> agences = new ArrayList<>();
		if (id != null) {
			agences.add(agenceRepo.findById(id).orElseThrow(() -> new NotFoundException("Aucune agence avec l'id " + id + " trouvée")));
		} else {
			agences = agenceRepo.findAll();
		}
		if (agences.isEmpty()) {
			throw new NotFoundException("Aucune agence trouvée");
		}
		return agences;
	}


	public List<Agent> getAgents(Long id) throws NotFoundException {
		Agence agence = agenceRepo.findById(id).orElseThrow(() -> new NotFoundException("Aucune agence avec l'id " + id + " trouvée"));
		if (agence.getAgents().isEmpty()) {
			throw new NotFoundException("Cet agence ne contient aucun agent.");
		}
		return agence.getAgents();
	}


	public List<Client> getClients(Long id) throws NotFoundException {
		Agence agence = agenceRepo.findById(id).orElseThrow(() -> new NotFoundException("Aucune agence avec l'id " + id + " trouvée"));
		if (agence.getClients().isEmpty()) {
			throw new NotFoundException("Cet agence ne contient aucun client.");
		}
		return agence.getClients();
	}


	public void addAgence(Agence agence) throws AlreadyExistsException, NotFoundException {
		if (agenceRepo.findByNom(agence.getNom()).isPresent()) {
			throw new AlreadyExistsException("Une agence avec le Nom " + agence.getNom() + " existe déjà");
		}
		Admin admin = adminService.getByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		agence.setCreationAdmin(admin);
		agenceRepo.save(agence);
		logger.debug("L'administrateur " + admin.getNom() + " " + admin.getPrenom() + " ayant le Username " + admin.getUsername() + " a créé l'agence " + agence.getNom());
	}

	public void updateAgence(Long id, Agence newAgence) throws NotFoundException, AlreadyExistsException {
		Agence oldAgence = agenceRepo.findById(id).orElseThrow(() -> new NotFoundException("Aucune agence avec l'id " + id + " trouvé"));
		//verifier l'unicité du nouveau nom
		if (agenceRepo.findByNom(newAgence.getNom()).isPresent()) {
			throw new AlreadyExistsException("Une agence avec le Nom " + newAgence.getNom() + " existe déjà");
		}
		if (agenceRepo.findByTelephone(newAgence.getTelephone()).isPresent()) {
			throw new AlreadyExistsException("Une agence avec le Telephone " + newAgence.getTelephone() + " existe déjà");
		}
		if (agenceRepo.findByEmail(newAgence.getEmail()).isPresent()) {
			throw new AlreadyExistsException("Une agence avec l'email " + newAgence.getEmail() + " existe déjà");
		}

		oldAgence.setNom(newAgence.getNom());
		oldAgence.setTelephone(newAgence.getTelephone());
		oldAgence.setAdresse(newAgence.getAdresse());
		oldAgence.setEmail(newAgence.getEmail());
		agenceRepo.save(oldAgence);
		Admin admin = adminService.getByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		logger.debug("L'administrateur " + admin.getNom() + " " + admin.getPrenom() + " ayant le Username " + admin.getUsername() + " a modifié l'agence " + oldAgence.getNom());
	}

	public void removeAgence(Long id) throws NotFoundException {
		//vérifier l'existence de l'agence
		Agence agence = agenceRepo.findById(id).orElseThrow(() -> new NotFoundException("Aucune agence avec l'id " + id + " n'est trouvé"));
		agenceRepo.delete(agence);
		Admin admin = adminService.getByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		logger.debug("L'administrateur " + admin.getNom() + " " + admin.getPrenom() + " ayant le Username " + admin.getUsername() + " a supprimé l'agence " + agence.getNom());
	}
}
