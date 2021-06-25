package ma.ensa.banki.controllers;

import com.itextpdf.text.DocumentException;
import ma.ensa.banki.entities.Compte;
import ma.ensa.banki.entities.Operateur;
import ma.ensa.banki.entities.Operation;
import ma.ensa.banki.exceptions.AlreadyExistsException;
import ma.ensa.banki.exceptions.NotFoundException;
import ma.ensa.banki.services.OperateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.util.List;

@CrossOrigin(origins = {"https://ebanking-front-admin.vercel.app", "https://ebanking-front-agent.vercel.app", "https://ebanking-front-client.vercel.app"})
@RestController
public class OperateurController {
	OperateurService service;

	@Autowired
	public OperateurController(OperateurService service) {
		this.service = service;
	}

	//GET
	@GetMapping("/operateur/all")
	@ResponseStatus(HttpStatus.OK)
	public List<Operateur> getAllOperateurs() throws NotFoundException {
		return service.getAllOperateurs();
	}

	@GetMapping("/operateur/{id}")
	@ResponseStatus(HttpStatus.OK)
	public Operateur getAllOperateurs(@PathVariable(name = "id") Long id) throws NotFoundException {
		return service.getOperateur(id);
	}

	//POST
	@PostMapping("/operateur")
	@ResponseStatus(HttpStatus.CREATED)
	public void addOperateur(@RequestBody Operateur operateur) throws AlreadyExistsException, DocumentException, FileNotFoundException {
		service.addOperateur(operateur);
	}


	//PUT
	@PutMapping("/operateur/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void updateOperateur(@PathVariable Long id, @RequestBody(required = false) Operateur operateur) throws NotFoundException, AlreadyExistsException {
		service.updateOperateur(id, operateur);
	}


	//DELETE
	@DeleteMapping("/operateur/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void deleteOperateur(@PathVariable Long id) throws NotFoundException {
		service.removeOperateur(id);
	}

	//GET
	@GetMapping("/operateur/{id}/operation")
	@ResponseStatus(HttpStatus.OK)
	public List<Operation> getOperations(@PathVariable(name = "id") Long id) throws NotFoundException {
		return service.getOperations(id);
	}

}

