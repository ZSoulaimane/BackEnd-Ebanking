package ma.ensa.banki.controllers;

import ma.ensa.banki.entities.Operation;
import ma.ensa.banki.exceptions.AlreadyExistsException;
import ma.ensa.banki.exceptions.NotFoundException;
import ma.ensa.banki.services.OperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@CrossOrigin(origins = {"https://ebanking-front-admin.vercel.app", "https://ebanking-front-agent.vercel.app", "https://ebanking-front-client.vercel.app"})
@RestController
public class OperationController {


	OperationService service;

	@Autowired
	public OperationController(OperationService service) {

		this.service = service;
	}

	//GET
	@GetMapping("/operations")
	@ResponseStatus(HttpStatus.OK)
	public List<Operation> getOperations(@RequestParam(name = "id", required = false) Long id) throws NotFoundException {
		return service.getOperations(id);
	}


	@GetMapping(value = "/operationPDF/{id}", produces = "application/pdf")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<InputStreamResource> getRecuOperationPDF(@PathVariable(name = "id") Long id) throws IOException {
		return service.getRecuOperationPDF(id);
	}


	//POST

	@PostMapping("/operations")
	@ResponseStatus(HttpStatus.CREATED)
	public void addOperation(@RequestBody Operation operation) throws Exception, AlreadyExistsException {
		service.addOperation(operation);
	}


}

