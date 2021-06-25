package ma.ensa.banki.services;

import com.itextpdf.text.DocumentException;
import ma.ensa.banki.entities.*;
import ma.ensa.banki.exceptions.AlreadyExistsException;
import ma.ensa.banki.exceptions.NotFoundException;
import ma.ensa.banki.repositories.CompteRepository;
import ma.ensa.banki.repositories.OperateurRepository;
import ma.ensa.banki.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OperateurService {
	@Autowired
	OperateurRepository operateurRepo;
	@Autowired
	UserRepository userRepo;
	@Autowired
	AgentService agentService;
	@Autowired
	AgenceService agenceService;
	@Autowired
	DeviseService deviseService;
	@Autowired
	CompteRepository compteRepo;
	@Autowired
	CompteService compteService;

	Logger logger = LoggerFactory.getLogger(OperateurService.class.getName());

	public List<Operateur> getAllOperateurs() throws NotFoundException {
		List<Operateur> operateurs = operateurs = operateurRepo.findAll();
		if (operateurs.isEmpty()) throw new NotFoundException("Aucun opérateur trouvé");
		return operateurs;
	}

	public Operateur getOperateur(Long id) {
		Operateur operateur = operateurRepo.findById(id).orElseThrow(() -> new NotFoundException("Aucun opérateur avec id " + id));
		return operateur;
	}


	public void addOperateur(Operateur operateur) throws AlreadyExistsException, DocumentException, FileNotFoundException {
		if (operateurRepo.findByNom(operateur.getNom()).isPresent()) {
			throw new AlreadyExistsException("Veuillez choisir un autre Nom");
		}
		Agent agent = agentService.getByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		operateur.setCreationAgent(agent);

		Compte compte = new Compte();
		compte.setCreationAgent(agent);
		Devise devise = deviseService.getDevisebyCode("MAD");
		compte.setDevise(devise);
		compte.setType("Compte operateur");
		compte.setSolde(0);
		compte.setCreationDate(LocalDateTime.now());
		compte.setNumero(compteService.generateNumCompte());
		compteRepo.save(compte);
		operateur.setCompte(compte);
		operateurRepo.save(operateur);
		logger.debug("L'agent " + agent.getNom() + " " + agent.getPrenom() + " ayant le Username " + agent.getUsername() + " a créé l'opérateur avec le username " + operateur.getNom());
	}

	public void updateOperateur(Long id, Operateur newOperateur) throws NotFoundException, AlreadyExistsException {
		Operateur oldOperateur = operateurRepo.findById(id).orElseThrow(() -> new NotFoundException("Aucun opérateur avec l'id " + id + " trouvé"));
		//verifier l'unicité du nouveau nom
		if (operateurRepo.countByNom(newOperateur.getNom()) > 1) {
			throw new AlreadyExistsException("Veuillez choisir un autre nom");
		}
		oldOperateur.setNom(newOperateur.getNom());
		oldOperateur.setEmail(newOperateur.getEmail());
		oldOperateur.setLogo(newOperateur.getLogo());
		operateurRepo.save(oldOperateur);
		Agent agent = agentService.getByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		logger.debug("L'agent " + agent.getNom() + " " + agent.getPrenom() + " ayant le Username " + agent.getUsername() + " a modifié l'opérateur avec le nom " + oldOperateur.getNom());
	}

	public void removeOperateur(Long id) throws NotFoundException {
		//vérifier l'existence du operateur
		Operateur operateur = operateurRepo.findById(id).orElseThrow(() -> new NotFoundException("Aucun opérateur avec l'id " + id + " n'est trouvé"));
		operateurRepo.delete(operateur);
		Agent agent = agentService.getByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		logger.debug("L'agent " + agent.getNom() + " " + agent.getPrenom() + " ayant le Username " + agent.getUsername() + " a supprimé l'opérateur avec le nom " + operateur.getNom());
	}


	public List<Operation> getOperations(Long id) {
		Operateur operateur = operateurRepo.findById(id).orElseThrow(() -> new NotFoundException("Aucun opérateur avec id "+ id));
		Compte compte = operateur.getCompte();
		List<Operation> operations = compte.getOperations();
		if (operations.isEmpty()){
			throw new NotFoundException("L'operateur " + operateur.getNom()+ " n'a aucune operation");
		}
		return operations;
	}
}
