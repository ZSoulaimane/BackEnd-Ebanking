package ma.ensa.banki.controllers;

import ma.ensa.banki.entities.Rdv;
import ma.ensa.banki.exceptions.AlreadyExistsException;
import ma.ensa.banki.exceptions.NotFoundException;
import ma.ensa.banki.services.RdvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {"https://ebanking-front-admin.vercel.app", "https://ebanking-front-agent.vercel.app", "https://ebanking-front-client.vercel.app"})
@RestController
public class RdvController {
	RdvService service;

	@Autowired
	public RdvController(RdvService service) {
		this.service = service;
	}

	// GET
	@GetMapping("/rdvs")
	@ResponseStatus(HttpStatus.OK)
	public List<Rdv> getAllRdvs() throws NotFoundException {
		return service.getAllRdvs();
	}

	@GetMapping("/client/rdvs")
	@ResponseStatus(HttpStatus.OK)
	public List<Rdv> getClientRdvs() throws NotFoundException {
		return service.getClientRdvs();
	}

	@GetMapping("/rdvs/booked")
	@ResponseStatus(HttpStatus.OK)
	public List<Rdv> getBookedRdvs() throws NotFoundException {
		return service.getBookedDates();
	}


	// POST
	@PostMapping("/rdvs")
	@ResponseStatus(HttpStatus.CREATED)
	public void addRdv(@RequestBody Rdv rdv) throws AlreadyExistsException {
		service.addRdv(rdv);
	}

	// Delete

}
