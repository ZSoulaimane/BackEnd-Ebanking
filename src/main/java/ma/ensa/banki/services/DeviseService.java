package ma.ensa.banki.services;

import com.posadskiy.currencyconverter.CurrencyConverter;
import com.posadskiy.currencyconverter.config.ConfigBuilder;
import ma.ensa.banki.entities.Admin;
import ma.ensa.banki.entities.Devise;
import ma.ensa.banki.exceptions.AlreadyExistsException;
import ma.ensa.banki.exceptions.NotFoundException;
import ma.ensa.banki.repositories.DeviseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@PropertySource("classpath:application.properties")
public class DeviseService {

	@Autowired
	DeviseRepository deviseRepo;

	@Autowired
	AdminService adminService;

	@Autowired
	private Environment environment;

//	String CURRENCY_CONVERTER_API_API_KEY = environment.getProperty("currencyConverterAPIkey");
//	String CURRENCY_LAYER = environment.getProperty("cuurencyLayerAPIkey");
//	String OPEN_EXCHANGE_RATES = environment.getProperty("openExchangeRatesAPIkey");

	CurrencyConverter converter = new CurrencyConverter(
			new ConfigBuilder()
					.currencyConverterApiApiKey("e818f19b6aa01b37ef42")
					.build()
	);


	Logger logger = LoggerFactory.getLogger(DeviseService.class.getName());


	public Devise getDevisebyCode(String code){
		return deviseRepo.findByCode(code).orElseThrow(() -> new NotFoundException("Aucune devise avec le code " + code + "trouvée."));
	}
	public List<Devise> getAllDevise() throws NotFoundException {
		List<Devise> devises = deviseRepo.findAll();
		if (devises.isEmpty()) {
			throw new NotFoundException("Aucun administrateur trouvé");
		}
		return devises;
	}

	public Devise getDevise(Long id) {
		return deviseRepo.findById(id).orElseThrow(() -> new NotFoundException("Aucune devise avec l'id" + id + "trouvée."));
	}

	//Ajouter une devise
	public void addDevise(Devise devise) throws AlreadyExistsException {

		//vérifier l'existence de la devise
		if (deviseRepo.findByCode(devise.getCode()).isPresent())
			throw new AlreadyExistsException("Une devise avec le code " + devise.getCode() + " existe déjà");

		devise.setCreationDate(LocalDateTime.now());
		devise.setModificationDate(LocalDateTime.now());

		deviseRepo.save(devise);
		Admin admin = adminService.getByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		devise.setCreationAdmin(admin);
		logger.debug("L'administrateur " + admin.getNom() + " " + admin.getPrenom() + " ayant le Username " + admin.getUsername() + " a créé la devise " + devise.getCode());

	}


	//modifier une devise
	public void updateDevise(Long id, Devise newDevise) throws NotFoundException, AlreadyExistsException {

		//vérifier l'existence de la devise
		Devise oldDevise = deviseRepo.findById(id).orElseThrow(() -> new NotFoundException("Aucune devise avec l'id " + id + " n'est trouvée"));

		//vérifier si les données saisies respectent l'unicité du code
		if (deviseRepo.countByCode(newDevise.getCode())>1) {
			throw new AlreadyExistsException("Une devise avec le code " + newDevise.getCode() + " existe déjà");
		}

		Admin admin = adminService.getByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

		//modifier les données
		oldDevise.setModificationDate(LocalDateTime.now());
		oldDevise.setNom(newDevise.getNom());
		oldDevise.setCode(newDevise.getCode());

		deviseRepo.save(oldDevise);

		logger.debug("L'administrateur " + admin.getNom() + " " + admin.getPrenom() + " ayant le Username " + admin.getUsername() + " a modifié la devise " + oldDevise.getId());
	}


	//supprimer une devise
	public void removeDevise(String code) throws NotFoundException {

		//vérifier l'existence de la devise
		Devise devise = deviseRepo.findByCode(code).orElseThrow(() -> new NotFoundException("Aucune devise avec le code " + code + " n'est trouvée"));
		deviseRepo.delete(devise);

		Admin admin = adminService.getByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		logger.debug("L'administrateur " + admin.getNom() + " " + admin.getPrenom() + " ayant le Username " + admin.getUsername() + " a supprimé la devise " + code);

	}

	public double getRate(String currSrc, String currDest) {
		double rate = 1;
		try {
			rate = converter.rate(currSrc.toUpperCase(), currDest.toUpperCase());
		} catch(Exception e){
			try{
				this.converter = new CurrencyConverter(
						new ConfigBuilder()
								.currencyLayerApiKey("c227dd8326701239370be32b4f1b39f3")
								.build()
				);
				rate = converter.rate(currSrc.toUpperCase(), currDest.toUpperCase());
			}
			catch(Exception ex){
				this.converter = new CurrencyConverter(
						new ConfigBuilder()
								.openExchangeRatesApiKey("addb418b0c394f43ade3ccfc8d7324a9")
								.build()
				);
				rate = converter.rate(currSrc.toUpperCase(), currDest.toUpperCase());
			}
		}
		return rate;
	}
}
