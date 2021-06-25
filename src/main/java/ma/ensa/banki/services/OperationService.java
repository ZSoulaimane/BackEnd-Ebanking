package ma.ensa.banki.services;

import ma.ensa.banki.entities.*;
import ma.ensa.banki.exceptions.NotFoundException;
import ma.ensa.banki.repositories.OperationRepository;
import ma.ensa.banki.repositories.VirementRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.PathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OperationService {


	@Autowired
	OperationRepository rep;

	@Autowired
	CompteService compteService;

	@Autowired
	AgentService agentService;

	@Autowired
	DeviseService deviseService;

	@Autowired
	RecuOperationService recuService;

	@Autowired
	RecuVirementService recuVirementService;

	@Autowired
	VirementRepository virementRepo;



	Logger logger = LoggerFactory.getLogger(RechargeService.class.getName());


	public List<Operation> getOperations(Long id) throws NotFoundException {

		List<Operation> operations = new ArrayList<>();

		if (id != null)
			operations.add(rep.findById(id).orElseThrow(() -> new NotFoundException("Aucun operation avec l'id " + id + " trouvé")));

		else
			operations = rep.findAll();

		if (operations.isEmpty()) throw new NotFoundException("Aucun operation trouvé");
		return operations;
	}


	public void addOperation(Operation operation) throws Exception {
		Compte compte = compteService.getComptes(operation.getCompte().getId()).get(0);
		Devise devise = deviseService.getDevise(operation.getDevise().getId());
		Agent agent = agentService.getByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

		if (operation.getType().equals("Retrait")) {
			if (compte.getSolde() < operation.getSommeCompte()) {
				throw new Exception("Vous n'avez pas de solde suffisant ! ");
			}
			operation.setDate(LocalDateTime.now());
			rep.save(operation);
			compte.setSolde(compte.getSolde() - operation.getSommeCompte());
			Virement virement = new Virement();
			virement.setCreancier(null);
			virement.setDebiteur(compte);
			virement.setSommeEnv(operation.getSommeCompte());
			virement.setSommeRecu(0);
			virement.setDate(LocalDateTime.now());
			virementRepo.save(virement);
			// Creation de recu de virement
			virement.setCreancier(compte);
			recuVirementService.CreateRecu(virement);
		}
		if (operation.getType().equals("Versement")) {
			operation.setDate(LocalDateTime.now());
			rep.save(operation);
			compte.setSolde(compte.getSolde() + operation.getSommeCompte());
			Virement virement = new Virement();
			virement.setCreancier(compte);
			virement.setDebiteur(null);
			virement.setSommeRecu(operation.getSommeCompte());
			virement.setSommeEnv(0);
			virement.setDate(LocalDateTime.now());
			virementRepo.save(virement);
			// Creation de recu de virement
			virement.setDebiteur(compte);
			recuVirementService.CreateRecu(virement);
		}
		compteService.rep.save(compte);
		recuService.CreateRecu(operation);

		logger.debug("L'agent " + agent.getNom() + " " + agent.getPrenom() + " ayant le Username " + agent.getUsername()
				+ " a effectué un " + operation.getType() + " de " + operation.getSommeEspece() + devise.getCode() + " à la date " + operation.getDate() + " en faveur du compte "
				+ compte.getNumero());
	}


	public ResponseEntity<InputStreamResource> getRecuOperationPDF(Long id) throws IOException {
		Operation operation = getOperations(id).get(0);
		Compte compte = compteService.getComptes(operation.getCompte().getId()).get(0);

		String filename =  operation.getType() + "_" + compte.getNumero() + "_" + operation.getDate().withNano(0).toString().replace(':', '-') + ".pdf";
		Path path = FileSystems.getDefault().getPath("","src","main","resources","operations",filename).toAbsolutePath();

		PathResource pdfFile = new PathResource(path);

		ResponseEntity<InputStreamResource> response = new ResponseEntity<>(
				new InputStreamResource(pdfFile.getInputStream()), HttpStatus.OK);


		Agent agent = agentService.getByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		logger.debug("L'agent " + agent.getNom() + " " + agent.getPrenom() + " ayant le Username " + agent.getUsername() + " a téléchargé le fichier " + filename + " à la date: " + LocalDateTime.now());


		return response;

	}


}
