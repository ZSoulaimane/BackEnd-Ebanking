package ma.ensa.banki.services;

import ma.ensa.banki.entities.Admin;
import ma.ensa.banki.entities.Agent;
import ma.ensa.banki.exceptions.AlreadyExistsException;
import ma.ensa.banki.exceptions.NotFoundException;
import ma.ensa.banki.repositories.AgentRepository;
import ma.ensa.banki.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AgentService {
	@Autowired
	UserRepository userRepo;
	@Autowired
	AgentRepository agentRepo;
	@Autowired
	AdminService adminService;
	Logger logger = LoggerFactory.getLogger(AgentService.class.getName());


	public List<Agent> getAgents(Long id) throws NotFoundException {
		List<Agent> agents = new ArrayList<>();
		if (id != null) {
			agents.add(agentRepo.findById(id).orElseThrow(() -> new NotFoundException("Aucun agent avec l'id " + id + " trouvé")));
		} else {
			agents = agentRepo.findAll();
		}
		if (agents.isEmpty()) throw new NotFoundException("Aucun agent trouvé");
		return agents;
	}


	public Agent getByUsername(String username) {
		return agentRepo.findByUsername(username).orElseThrow(() -> new NotFoundException("Aucun agent avec le username " + username + " trouvé"));
	}


	public void addAgent(Agent agent) throws AlreadyExistsException {
		System.out.println(agent);
		if (userRepo.countByUsername(agent.getUsername())>1) {
			throw new AlreadyExistsException("Veuillez choisir un autre Username");
		}
		if (userRepo.countByCin(agent.getCin())>1) {
			throw new AlreadyExistsException("Un agent avec le CIN " + agent.getCin() + " existe déjà");
		}
		String newPassword = agent.getPassword();
		newPassword = new BCryptPasswordEncoder().encode(newPassword);
		agent.setPassword(newPassword);
		agent.setRole("Agent");
		Admin admin = adminService.getByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		agent.setCreationAdmin(admin);

		agentRepo.save(agent);

		logger.debug("L'administrateur " + admin.getNom() + " " + admin.getPrenom() + " ayant le Username " + admin.getUsername() + " a créé l'agent avec le username " + agent.getUsername());

	}

	public void updateAgent(Long id, Agent newAgent) throws NotFoundException, AlreadyExistsException {
		Agent oldAgent = agentRepo.findById(id).orElseThrow(() -> new NotFoundException("Aucun agent avec l'id " + id + " trouvé"));

		//verifier l'unicité du nouveau username
		if (userRepo.countByUsername(newAgent.getUsername())>1) {
			throw new AlreadyExistsException("Veuillez choisir un autre Username");
		}

		oldAgent.setNom(newAgent.getNom());
		oldAgent.setPrenom(newAgent.getPrenom());
		oldAgent.setTelephone(newAgent.getTelephone());
		oldAgent.setAdresse(newAgent.getAdresse());
		oldAgent.setEmail(newAgent.getEmail());
		oldAgent.setUsername(newAgent.getUsername());
		String newPassword = newAgent.getPassword();
		if (newPassword != null && !newPassword.isEmpty()){
			oldAgent.setPassword(new BCryptPasswordEncoder().encode(newPassword));
		}
		oldAgent.setAgence(newAgent.getAgence());
		agentRepo.save(oldAgent);
		Admin admin = adminService.getByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		logger.debug("L'administrateur " + admin.getNom() + " " + admin.getPrenom() + " ayant le Username " + admin.getUsername() + " a modifié l'agent avec le username " + oldAgent.getUsername());
	}

	public void removeAgent(Long id) throws NotFoundException {
		//vérifier l'existence de l'agent
		Agent agent = agentRepo.findById(id).orElseThrow(() -> new NotFoundException("Aucun agent avec l'id " + id + " n'est trouvé"));
		agentRepo.delete(agent);
		Admin admin = adminService.getByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		logger.debug("L'administrateur " + admin.getNom() + " " + admin.getPrenom() + " ayant le Username " + admin.getUsername() + " a supprimé l'agent avec le username " + agent.getUsername());
	}
}
