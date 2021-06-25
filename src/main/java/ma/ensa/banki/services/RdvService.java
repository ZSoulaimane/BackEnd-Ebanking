package ma.ensa.banki.services;

import ma.ensa.banki.entities.*;
import ma.ensa.banki.exceptions.AlreadyExistsException;
import ma.ensa.banki.exceptions.NotFoundException;
import ma.ensa.banki.repositories.AgentRepository;
import ma.ensa.banki.repositories.ClientRepository;
import ma.ensa.banki.repositories.RdvRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RdvService {
	@Autowired
	RdvRepository rdvRepo;
	@Autowired
	ClientRepository clientRepo;
	@Autowired
	AgentRepository agentRepo;

	public List<Rdv> getAllRdvs() {
		Agent agent = agentRepo.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).get();
		Agence agence = agent.getAgence();

		// Recuperer la liste des RDV de l'agence, ordre ASC en utilisant la date et heure du RDV
		List<Rdv> rdvs = rdvRepo.findAllByAgenceOrderByDateRdvAscHeureRdvAsc(agence);
		if (rdvs.isEmpty()) throw new NotFoundException("Aucun Rdv trouvé");
		return rdvs;
	}

	public void addRdv(Rdv rdv) throws AlreadyExistsException {
		Client client = clientRepo.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).get();
		Agence agence = client.getAgence();
		rdv.setAgence(agence);
		rdvRepo.save(rdv);
	}

	public List<Rdv> getBookedDates() {
		List<Rdv> booked = new ArrayList<Rdv>();
		List<Rdv> rdvs = rdvRepo.findAllByOrderByDateRdvAscHeureRdvAsc();
		for (Rdv rdv : rdvs) {
			Rdv bookedRdv = new Rdv();
			bookedRdv.setDateRdv(rdv.getDateRdv());
			bookedRdv.setHeureRdv(rdv.getHeureRdv());
			booked.add(bookedRdv);
		}
		return booked;
	}

	public List<Rdv> getClientRdvs() throws NotFoundException{
		Client client = clientRepo.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).get();
		List<Rdv> rdvs = rdvRepo.findAllByClient(client);
		if (rdvs.isEmpty()) {
			throw new NotFoundException("Le client " + client.getUsername() + " n'a aucun RDV");
		}
		return rdvs;
	}
}
