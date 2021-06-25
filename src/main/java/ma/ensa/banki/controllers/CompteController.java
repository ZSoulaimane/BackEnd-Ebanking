package ma.ensa.banki.controllers;

import com.itextpdf.text.DocumentException;
import ma.ensa.banki.entities.Compte;
import ma.ensa.banki.entities.Operation;
import ma.ensa.banki.entities.Recharge;
import ma.ensa.banki.entities.Virement;
import ma.ensa.banki.exceptions.AlreadyExistsException;
import ma.ensa.banki.exceptions.NotFoundException;
import ma.ensa.banki.services.CompteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

@CrossOrigin(origins = {"https://ebanking-front-admin.vercel.app", "https://ebanking-front-agent.vercel.app", "https://ebanking-front-client.vercel.app"})
@RestController
public class CompteController {


	CompteService service;

	@Autowired
	public CompteController(CompteService service) {

		this.service = service;
	}

	//GET
	@GetMapping("/comptes")
	@ResponseStatus(HttpStatus.OK)
	public List<Compte> getComptes(@RequestParam(name = "id", required = false) Long id) throws NotFoundException {
		return service.getComptes(id);
	}


	@GetMapping("/compte/{numero}")
	@ResponseStatus(HttpStatus.OK)
	public Compte getCompteByNumero(@PathVariable(name = "numero") String numero) {
		return service.getCompteByNumero(numero);
	}


	@GetMapping("/compte/{id}/virements")
	@ResponseStatus(HttpStatus.OK)
	public List<Virement> getVirements(@PathVariable(name = "id") Long id) throws NotFoundException {
		return service.getVirements(id);
	}


	@GetMapping("/compte/{id}/virementsEnvoyes")
	@ResponseStatus(HttpStatus.OK)
	public List<Virement> getVirementsEnvoyes(@PathVariable(name = "id") Long id) throws NotFoundException {
		return service.getVirementsEnvoyes(id);
	}


	@GetMapping("/compte/{id}/virementsRecus")
	@ResponseStatus(HttpStatus.OK)
	public List<Virement> getVirementsRecus(@PathVariable(name = "id") Long id) throws NotFoundException {
		return service.getVirementsRecus(id);
	}


	@GetMapping("/compte/{id}/recharges")
	@ResponseStatus(HttpStatus.OK)
	public List<Recharge> getRecharges(@PathVariable(name = "id") Long id) throws NotFoundException {
		return service.getRecharges(id);
	}


	@GetMapping("/compte/{id}/operations")
	@ResponseStatus(HttpStatus.OK)
	public List<Operation> getOperations(@PathVariable(name = "id") Long id) throws NotFoundException {
		return service.getOperations(id);
	}


	@GetMapping(value = "/contratPDF/{id}", produces = "application/pdf")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<InputStreamResource> getContratPDF(@PathVariable(name = "id") Long id) throws IOException {
		return service.getContratPDF(id);
	}


	//POST

	@PostMapping("/comptes")
	@ResponseStatus(HttpStatus.CREATED)
	public void addCompte(@RequestBody Compte compte) throws AlreadyExistsException, DocumentException, FileNotFoundException {
		service.addCompte(compte);
	}


	//PUT

	@PutMapping("/compte/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void updateCompte(@PathVariable Long id, @RequestBody(required = false) Compte compte) throws NotFoundException, AlreadyExistsException {
		service.updateCompte(id, compte);
	}


	//DELETE

	@DeleteMapping("/compte/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void deleteCompte(@PathVariable Long id) throws NotFoundException {
		service.removeCompte(id);
	}


}

