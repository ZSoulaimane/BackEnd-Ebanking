package ma.ensa.banki.controllers;

import ma.ensa.banki.entities.Client;
import ma.ensa.banki.entities.Compte;
import ma.ensa.banki.exceptions.AlreadyExistsException;
import ma.ensa.banki.exceptions.NotFoundException;
import ma.ensa.banki.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@CrossOrigin(origins = {"https://ebanking-front-admin.vercel.app", "https://ebanking-front-agent.vercel.app", "https://ebanking-front-client.vercel.app"})
@RestController
public class ClientController {
	ClientService service;

	@Autowired
	public ClientController(ClientService service) {
		this.service = service;
	}

	//GET
	@GetMapping("/clients")
	@ResponseStatus(HttpStatus.OK)
	public List<Client> getClients(@RequestParam(name = "id", required = false) Long id) throws NotFoundException {
		return service.getClients(id);
	}


	@GetMapping("/client/username/{username}")
	@ResponseStatus(HttpStatus.OK)
	public Client getByUsername(@PathVariable(name = "username") String username) {
		return service.getByUsername(username);
	}


	@GetMapping("/client/{id}/comptes")
	@ResponseStatus(HttpStatus.OK)
	public List<Compte> getComptes(@PathVariable(name = "id") Long id) throws NotFoundException {
		return service.getComptes(id);
	}

	@GetMapping("/client/{id}/beneficiares")
	@ResponseStatus(HttpStatus.OK)
	public Set<Compte> getBeneficiares(@PathVariable(name = "id") Long id) {
		return service.getBeneficiares(id);
	}

	//POST

	@PostMapping("/clients")
	@ResponseStatus(HttpStatus.CREATED)
	public void addClient(@RequestBody Client client) throws AlreadyExistsException {
		service.addClient(client);
	}

	@PostMapping("/client/beneficiares")
	@ResponseStatus(HttpStatus.CREATED)
	public void addBeneficiaire(@RequestBody String num_compte_benif) throws AlreadyExistsException {
		System.out.println(num_compte_benif);
		service.addBeneficiare(num_compte_benif);
	}

	//PUT

	@PutMapping("/client/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void updateClient(@PathVariable(name = "id") Long id, @RequestBody(required = false) Client client) throws NotFoundException, AlreadyExistsException {
		service.updateClient(id, client);
	}


	//DELETE

	@DeleteMapping("/client/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void deleteClient(@PathVariable(name = "id") Long id) throws NotFoundException {
		service.removeClient(id);
	}

	@DeleteMapping("/client/beneficiares/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void deleteBeneficiaire(@PathVariable(name = "id") Long id) throws NotFoundException {
		service.removeBeneficiaire(id);
	}



}

