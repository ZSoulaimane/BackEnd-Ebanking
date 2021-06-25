package ma.ensa.banki.services;

import ma.ensa.banki.entities.Agence;
import ma.ensa.banki.entities.Agent;
import ma.ensa.banki.entities.Client;
import ma.ensa.banki.entities.Compte;
import ma.ensa.banki.exceptions.AlreadyExistsException;
import ma.ensa.banki.exceptions.NotFoundException;
import ma.ensa.banki.repositories.ClientRepository;
import ma.ensa.banki.repositories.CompteRepository;
import ma.ensa.banki.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class ClientService {

	@Autowired
	ClientRepository clientRep;
	@Autowired
	CompteRepository compteRep;
	@Autowired
	UserRepository userRep;
	@Autowired
	AgentService agentService;
	@Autowired
	AgenceService agenceService;

	Logger logger = LoggerFactory.getLogger(ClientService.class.getName());

	public List<Client> getClients(Long id) throws NotFoundException {

		List<Client> clients = new ArrayList<>();

		if (id != null)
			clients.add(clientRep.findById(id).orElseThrow(() -> new NotFoundException("Aucun client avec l'id " + id + " trouvé")));

		else
			clients = clientRep.findAll();

		if (clients.isEmpty()) throw new NotFoundException("Aucun client trouvé");
		return clients;
	}

	public Client getByUsername(String username) {
		return clientRep.findByUsername(username).orElseThrow(() -> new NotFoundException("Aucun client avec le username " + username + " trouvé"));
	}

	public List<Compte> getComptes(Long id) throws NotFoundException {
		Client client = clientRep.findById(id).orElseThrow(() -> new NotFoundException("Aucun client avec l'id " + id + " trouvé"));
		if (client.getComptes().isEmpty()) throw new NotFoundException("Cet client n'a aucun compte.");
		return client.getComptes();

	}

	public void addClient(Client client) throws AlreadyExistsException {
		if (userRep.findByUsername(client.getUsername()).isPresent()) {
			throw new AlreadyExistsException("Veuillez choisir un autre Username");
		}

		if (clientRep.findByCin(client.getCin()).isPresent()) {
			throw new AlreadyExistsException("Un client avec le CIN " + client.getCin() + " existe déjà");
		}

		String password = client.getPassword();

		client.setPassword(new BCryptPasswordEncoder().encode(client.getPassword()));
		client.setRole("Client");

		Agent agent = agentService.getByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

		client.setCreationAgent(agent);
		Agence agence = agenceService.getAgences(agent.getAgence().getId()).get(0);
		client.setAgence(agence);

		clientRep.save(client);

		logger.debug("L'agent " + agent.getNom() + " " + agent.getPrenom() + " ayant le Username " + agent.getUsername() + " a créé le client avec le username " + client.getUsername());


	}

	public void updateClient(Long id, Client client) throws NotFoundException, AlreadyExistsException {
		Client updated = clientRep.findById(id).orElseThrow(() -> new NotFoundException("Aucun client avec l'id " + id + " trouvé"));

		//verifier l'unicité du nouveau username
		if (userRep.findByUsername(client.getUsername()).isPresent() && !(userRep.findByUsername(client.getUsername()).get() == updated))
			throw new AlreadyExistsException("Veuillez choisir un autre Username");
		//verifier l'unicité du nouveau CIN
		if (clientRep.findByCin(client.getCin()).isPresent() && !(clientRep.findByCin(client.getCin()).get() == updated))
			throw new AlreadyExistsException("Un client avec le CIN " + client.getCin() + " existe déjà");

		if (client.getNom() != null && !client.getNom().isEmpty()) updated.setNom(client.getNom());
		if (client.getPrenom() != null && !client.getPrenom().isEmpty()) updated.setPrenom(client.getPrenom());
		if (client.getCin() != null && !client.getCin().isEmpty()) updated.setCin(client.getCin());
		if (client.getTelephone() != null && !client.getTelephone().isEmpty())
			updated.setTelephone(client.getTelephone());
		if (client.getAdresse() != null && !client.getAdresse().isEmpty()) updated.setAdresse(client.getAdresse());
		if (client.getEmail() != null && !client.getEmail().isEmpty()) updated.setEmail(client.getEmail());
		if (client.getUsername() != null && !client.getUsername().isEmpty()) updated.setUsername(client.getUsername());
		if (client.getPassword() != null && !client.getPassword().isEmpty())
			updated.setPassword(new BCryptPasswordEncoder().encode(client.getPassword()));

		clientRep.save(updated);

		if (client.getPassword() != null && !client.getPassword().isEmpty()) updated.setPassword(client.getPassword());
		else updated.setPassword(null);

		Agent agent = agentService.getByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		logger.debug("L'agent " + agent.getNom() + " " + agent.getPrenom() + " ayant le Username " + agent.getUsername() + " a modifié le client avec le username " + updated.getUsername());


	}

	public void removeClient(Long id) throws NotFoundException {

		//vérifier l'existence du client
		Client client = clientRep.findById(id).orElseThrow(() -> new NotFoundException("Aucun client avec l'id " + id + " n'est trouvé"));
		clientRep.delete(client);

		Agent agent = agentService.getByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		logger.debug("L'agent " + agent.getNom() + " " + agent.getPrenom() + " ayant le Username " + agent.getUsername() + " a supprimé le client avec le username " + client.getUsername());

	}

	public Set<Compte> getBeneficiares(Long id) throws NotFoundException {
		Client client = clientRep.findById(id).orElseThrow(() -> new NotFoundException("Aucun client avec l'id " + id + " trouvé"));
		Set<Compte> beneficiares = client.getBeneficiares();
		if (beneficiares.isEmpty()) throw new NotFoundException("Cet client n'a aucun beneficiares.");
		return beneficiares;
	}

	public void addBeneficiare(String num_compte_benif) {
		if (!compteRep.findByNumero(num_compte_benif).isPresent()) {
			throw new AlreadyExistsException("Aucun compte avec N" + num_compte_benif + " trouvé");
		}

		Compte beneficiare = compteRep.findByNumero(num_compte_benif).get();
		Client client = clientRep.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).get();
		Set<Compte> beneficiares = client.getBeneficiares();
		if (beneficiares.contains(beneficiare)) {
			throw new AlreadyExistsException("Le beneficiare " + beneficiare.getNumero() + " deja existant");
		}
		beneficiares.add(beneficiare);
		clientRep.save(client);
		logger.debug("Le client " + client.getNom() + " " + client.getPrenom() + " ayant le Username " + client.getUsername() + " a ajouté le compte N " + beneficiare.getNumero() + " à sa liste de beneficiares");
	}

	public void removeBeneficiaire(Long id) {
		Client client = clientRep.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).get();
		Set<Compte> beneficiares = client.getBeneficiares();
		Compte beneficiare = compteRep.getById(id);

		if (!beneficiares.contains(beneficiare)) {
			throw new NotFoundException("Beneficiare non trouvé");
		}

		beneficiares.remove(beneficiare);
		clientRep.save(client);
	}
}
