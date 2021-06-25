package ma.ensa.banki.controllers;

import ma.ensa.banki.entities.Devise;
import ma.ensa.banki.exceptions.AlreadyExistsException;
import ma.ensa.banki.exceptions.NotFoundException;
import ma.ensa.banki.services.DeviseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {"https://ebanking-front-admin.vercel.app", "https://ebanking-front-agent.vercel.app", "https://ebanking-front-client.vercel.app"})
@RestController
public class DeviseController {
	DeviseService service;
	@Autowired
	public DeviseController(DeviseService service) {
		this.service = service;
	}

	//GET
	@GetMapping("/devise/{id}")
	@ResponseStatus(HttpStatus.OK)
	public Devise getCurrency(@PathVariable(name = "id") Long id) throws NotFoundException {
		return service.getDevise(id);
	}

	@GetMapping("/devise/all")
	@ResponseStatus(HttpStatus.OK)
	public List<Devise> getCurrencies() throws NotFoundException {
		return service.getAllDevise();
	}

	@GetMapping("/devise/convert/{code_src}/{code_dest}")
	public double getRate(@PathVariable(name = "code_src") String codeSrc, @PathVariable(name = "code_dest") String codeDest) {
		return service.getRate(codeSrc, codeDest);
	}

	//POST
	@PostMapping("/devise")
	@ResponseStatus(HttpStatus.CREATED)
	public void addCurrency(@RequestBody Devise devise) throws AlreadyExistsException {
		service.addDevise(devise);
	}


	//PUT
	@PutMapping("/devise/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void updateCurrency(@PathVariable Long id, @RequestBody Devise devise) throws NotFoundException, AlreadyExistsException {
		service.updateDevise(id, devise);
	}


	//DELETE
	@DeleteMapping("/devise/{code}")
	@ResponseStatus(HttpStatus.OK)
	public void deleteCurrency(@PathVariable String code) throws NotFoundException {
		service.removeDevise(code);
	}


}
