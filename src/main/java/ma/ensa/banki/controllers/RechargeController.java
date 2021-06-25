package ma.ensa.banki.controllers;

import ma.ensa.banki.entities.Recharge;
import ma.ensa.banki.exceptions.AlreadyExistsException;
import ma.ensa.banki.exceptions.NotFoundException;
import ma.ensa.banki.services.RechargeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {"https://ebanking-front-admin.vercel.app", "https://ebanking-front-agent.vercel.app", "https://ebanking-front-client.vercel.app"})
@RestController
public class RechargeController {


	RechargeService service;

	@Autowired
	public RechargeController(RechargeService service) {

		this.service = service;
	}

	//GET
	@GetMapping("/recharges")
	@ResponseStatus(HttpStatus.OK)
	public List<Recharge> getRecharges(@RequestParam(name = "id", required = false) Long id) throws NotFoundException {
		return service.getRecharges(id);
	}


	//POST

	@PostMapping("/recharges")
	@ResponseStatus(HttpStatus.CREATED)
	public void addRecharge(@RequestBody Recharge recharge) throws Exception, AlreadyExistsException {
		service.addRecharge(recharge);
	}


}

