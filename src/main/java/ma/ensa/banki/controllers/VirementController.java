package ma.ensa.banki.controllers;

import ma.ensa.banki.entities.Virement;
import ma.ensa.banki.exceptions.AlreadyExistsException;
import ma.ensa.banki.exceptions.NotFoundException;
import ma.ensa.banki.services.VirementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@CrossOrigin(origins = {"https://ebanking-front-admin.vercel.app", "https://ebanking-front-agent.vercel.app", "https://ebanking-front-client.vercel.app"})
@RestController
public class VirementController {


	VirementService service;

	@Autowired
	public VirementController(VirementService service) {

		this.service = service;
	}

	//GET
	@GetMapping("/virements")
	@ResponseStatus(HttpStatus.OK)
	public List<Virement> getVirements(@RequestParam(name = "id", required = false) Long id) throws NotFoundException {
		return service.getVirements(id);
	}


	@GetMapping(value = "/virementPDF/{id}", produces = "application/pdf")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<InputStreamResource> getRecuVirementPDF(@PathVariable(name = "id") Long id) throws IOException {
		return service.getRecuVirementPDF(id);
	}


	//POST

	@PostMapping("/virements")
	@ResponseStatus(HttpStatus.CREATED)
	public void addVirement(@RequestBody Virement virement) throws Exception, AlreadyExistsException {
		service.addVirement(virement);
	}


}

