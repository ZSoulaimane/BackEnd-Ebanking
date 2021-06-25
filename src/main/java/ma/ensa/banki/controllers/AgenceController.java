package ma.ensa.banki.controllers;

import ma.ensa.banki.entities.Agence;
import ma.ensa.banki.entities.Agent;
import ma.ensa.banki.entities.Client;
import ma.ensa.banki.exceptions.AlreadyExistsException;
import ma.ensa.banki.exceptions.NotFoundException;
import ma.ensa.banki.services.AgenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {"https://ebanking-front-admin.vercel.app", "https://ebanking-front-agent.vercel.app", "https://ebanking-front-client.vercel.app"})
@RestController
public class AgenceController {


	AgenceService service;

	@Autowired
	public AgenceController(AgenceService service) {
		this.service = service;
	}

	//GET
	@GetMapping("/agences")
	@ResponseStatus(HttpStatus.OK)
	public List<Agence> getAgences(@RequestParam(name = "id", required = false) Long id) throws NotFoundException {
		return service.getAgences(id);
	}


	@GetMapping("/agence/{id}/agents")
	@ResponseStatus(HttpStatus.OK)
	public List<Agent> getAgents(@PathVariable(name = "id") Long id) throws NotFoundException {
		return service.getAgents(id);
	}


	@GetMapping("/agence/{id}/clients")
	@ResponseStatus(HttpStatus.OK)
	public List<Client> getClients(@PathVariable(name = "id") Long id) throws NotFoundException {
		return service.getClients(id);
	}


	//POST

	@PostMapping("/agences")
	@ResponseStatus(HttpStatus.CREATED)
	public void addAgence(@RequestBody Agence agence) throws AlreadyExistsException, NotFoundException {
		service.addAgence(agence);
	}


	//PUT

	@PutMapping("/agence/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void updateAgence(@PathVariable Long id, @RequestBody(required = false) Agence agence) throws NotFoundException, AlreadyExistsException {
		service.updateAgence(id, agence);
	}


	//DELETE

	@DeleteMapping("/agence/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void deleteAgence(@PathVariable Long id) throws NotFoundException {
		service.removeAgence(id);
	}


}

