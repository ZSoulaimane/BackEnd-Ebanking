package ma.ensa.banki.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = {"https://ebanking-front-admin.vercel.app", "https://ebanking-front-agent.vercel.app", "https://ebanking-front-client.vercel.app"})
@RestController
public class VersionController {


	@Autowired
	BuildProperties buildProperties;

	@GetMapping("/version")
	public String buildInfos() {
		return buildProperties.getVersion();
	}
}
