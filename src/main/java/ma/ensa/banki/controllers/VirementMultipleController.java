package ma.ensa.banki.controllers;

import ma.ensa.banki.entities.VirementMultiple;
import ma.ensa.banki.services.VirementMultipleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ma.ensa.banki.exceptions.NotFoundException;
import java.util.List;

@CrossOrigin(origins = {"https://ebanking-front-admin.vercel.app", "https://ebanking-front-agent.vercel.app", "https://ebanking-front-client.vercel.app"})
@RestController
public class VirementMultipleController {
    VirementMultipleService service;

    @Autowired
    public VirementMultipleController(VirementMultipleService service){
        this.service = service;
    }

    //POST
    @PostMapping("/virementsMultiples")
    @ResponseStatus(HttpStatus.CREATED)
    public void addVirement(@RequestBody VirementMultiple virementMultiple) {
        service.addVirementMultiple(virementMultiple);
    }
}
