package ma.ensa.banki.services;

import com.itextpdf.text.pdf.PdfWriter;
import ma.ensa.banki.entities.Client;
import ma.ensa.banki.entities.Compte;
import ma.ensa.banki.entities.Devise;
import ma.ensa.banki.entities.Virement;
import ma.ensa.banki.exceptions.NotFoundException;
import ma.ensa.banki.repositories.VirementRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.PathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class VirementService {

	@Autowired
	VirementRepository rep;

	@Autowired
	CompteService compteService;

	@Autowired
	ClientService clientService;

	@Autowired
	RecuVirementService recuService;

	@Autowired
	DeviseService deviseService;

	Logger logger = LoggerFactory.getLogger(RechargeService.class.getName());


	public List<Virement> getVirements(Long id) throws NotFoundException {

		List<Virement> virements = new ArrayList<>();

		if (id != null) {
			virements.add(rep.findById(id).orElseThrow(() -> new NotFoundException("Aucun virement avec l'id " + id + " trouvé")));
		}
		else {
			virements = rep.findAll();
		}
		if (virements.isEmpty()) throw new NotFoundException("Aucun virement trouvé");
		return virements;
	}


	public void addVirement(Virement virement) throws Exception {

		Compte debiteur = compteService.getComptes(virement.getDebiteur().getId()).get(0);
		Compte creancier = compteService.getComptes(virement.getCreancier().getId()).get(0);

		Client client = clientService.getByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		Client clientDebiteur = clientService.getClients(debiteur.getProprietaire().getId()).get(0);
		if (client != clientDebiteur) throw new Exception("Ce compte ne vous appartient pas !");

		if (debiteur.getSolde() < virement.getSommeEnv()) throw new Exception("Vous n'avez pas de solde suffisant !");

		virement.setDate(LocalDateTime.now());

		rep.save(virement);

		debiteur.setSolde(debiteur.getSolde() - virement.getSommeEnv());
		creancier.setSolde(creancier.getSolde() + virement.getSommeRecu());


		compteService.rep.save(debiteur);
		compteService.rep.save(creancier);

		recuService.CreateRecu(virement);


		Devise devise = deviseService.getDevise(debiteur.getDevise().getId());

		logger.debug("Le client " + client.getNom() + " " + client.getPrenom() + " ayant le Username " + client.getUsername()
				+ " a effectué un virement de " + virement.getSommeEnv() + devise.getCode() + " à la date " + virement.getDate() + " du compte "
				+ debiteur.getNumero() + " vers le compte " + creancier.getNumero());

	}


	public ResponseEntity<InputStreamResource> getRecuVirementPDF(Long id) throws IOException {
		Virement virement = getVirements(id).get(0);
		Compte debiteur = compteService.getComptes(virement.getDebiteur().getId()).get(0);



		String filename = "virement_" + debiteur.getNumero() + "_" + virement.getDate().withNano(0).toString().replace(':', '-') + ".pdf";
		Path path = FileSystems.getDefault().getPath("","src","main","resources","virements",filename).toAbsolutePath();

		PathResource pdfFile = new PathResource(path);


		ResponseEntity<InputStreamResource> response = new ResponseEntity<>(
				new InputStreamResource(pdfFile.getInputStream()), HttpStatus.OK);

		Client client = clientService.getByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		logger.debug("Le client " + client.getNom() + " " + client.getPrenom() + " ayant le Username " + client.getUsername() + " a téléchargé le fichier " + filename + " à la date: " + LocalDateTime.now());


		return response;

	}


}
