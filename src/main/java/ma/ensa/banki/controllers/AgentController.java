package ma.ensa.banki.controllers;

import ma.ensa.banki.entities.Agent;
import ma.ensa.banki.exceptions.AlreadyExistsException;
import ma.ensa.banki.exceptions.NotFoundException;
import ma.ensa.banki.services.AgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {"https://ebanking-front-admin.vercel.app", "https://ebanking-front-agent.vercel.app", "https://ebanking-front-client.vercel.app"})
@RestController
public class AgentController {


	AgentService service;

	@Autowired
	public AgentController(AgentService service) {

		this.service = service;
	}

	//GET
	@GetMapping("/agents")
	@ResponseStatus(HttpStatus.OK)
	public List<Agent> getAgents(@RequestParam(name = "id", required = false) Long id) throws NotFoundException {
		return service.getAgents(id);
	}


	@GetMapping("/agent/username/{username}")
	@ResponseStatus(HttpStatus.OK)
	public Agent getByUsername(@PathVariable(name = "username") String username) {
		return service.getByUsername(username);
	}


	//POST

	@PostMapping("/agents")
	@ResponseStatus(HttpStatus.CREATED)
	public void addAgent(@RequestBody Agent agent) throws AlreadyExistsException {
		service.addAgent(agent);
	}


	//PUT

	@PutMapping("/agent/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void updateAgent(@PathVariable Long id, @RequestBody(required = false) Agent agent) throws NotFoundException, AlreadyExistsException {
		service.updateAgent(id, agent);
	}


	//DELETE

	@DeleteMapping("/agent/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void deleteAgent(@PathVariable Long id) throws NotFoundException {
		service.removeAgent(id);
	}


}

