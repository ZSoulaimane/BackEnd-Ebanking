package ma.ensa.banki.configuration.security;


import com.google.common.collect.ImmutableList;
import ma.ensa.banki.entities.Admin;
import ma.ensa.banki.entities.Devise;
import ma.ensa.banki.exceptions.NotFoundException;
import ma.ensa.banki.services.AdminService;
import ma.ensa.banki.services.DeviseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
public class AppSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	AdminService adminService;
	@Autowired
	DeviseService deviseService;

	UserPrincipalDetailsService service;

	@Autowired
	public AppSecurityConfig(UserPrincipalDetailsService service) {
		this.service = service;
	}

	@PostConstruct
	public void init() {
		try {
			adminService.getAdmins(null);
		} catch (NotFoundException e) {
			Admin admin = new Admin();
			admin.setUsername("admin");
			admin.setPassword("admin");
			admin.setEmail("ensa.backend@gmail.com");
			admin.setRole("Admin");
			admin.setAdresse("123 Rue Med V");
			admin.setCin("P123456");
			admin.setTelephone("0612345678");
			admin.setNom("Admin");
			admin.setPrenom("Principal");
			adminService.addAdmin(admin);
		}

		try {
			deviseService.getAllDevise();
		} catch (NotFoundException e) {
			Devise devise = new Devise();
			devise.setCode("MAD");
			devise.setNom("Dirham Marocain");
			deviseService.addDevise(devise);
		}
	}

	@Bean
	public DaoAuthenticationProvider autProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(service);
		provider.setPasswordEncoder(new BCryptPasswordEncoder());

		return provider;
	}


	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		final CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Collections.singletonList("*"));
		configuration.setAllowedOrigins(Arrays.asList("https://ebanking-front-admin.vercel.app","https://ebanking-front-agent.vercel.app","https://ebanking-front-client.vercel.app"));
		configuration.setAllowedMethods(ImmutableList.of("HEAD",
				"GET", "POST", "PUT", "DELETE", "PATCH"));
		configuration.setAllowedHeaders(ImmutableList.of("accept",
				"accept-encoding",
				"authorization",
				"content-type",
				"dnt",
				"origin",
				"user-agent",
				"x-csrftoken",
				"x-requested-with"));
		// setAllowCredentials(true) is important, otherwise:
		// The value of the 'Access-Control-Allow-Origin' header in the response must not be the wildcard '*' when the request's credentials mode is 'include'.
		configuration.setAllowCredentials(true);
		// setAllowedHeaders is important! Without it, OPTIONS preflight request
		// will fail with 403 Invalid CORS request
		configuration.setAllowedHeaders(ImmutableList.of("Authorization", "Cache-Control", "Content-Type"));
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http
				.cors()
				.and()
				.authorizeRequests()
				//ADMIN
				.antMatchers(HttpMethod.GET, "/admins").hasRole("Admin")        //afficher les admins
				.antMatchers(HttpMethod.GET, "/admin/username/{username}").hasRole("Admin")        //admin par username
				.antMatchers(HttpMethod.POST, "/admins").hasRole("Admin")        //creer les admins
				.antMatchers(HttpMethod.PUT, "/admin/{id}").hasRole("Admin")    //modifier un admin
				.antMatchers(HttpMethod.DELETE, "/admin/{id}").hasRole("Admin")    //supprimer un admin

				//AGENT
				.antMatchers(HttpMethod.POST, "/agents").hasRole("Admin")        //creer agent
				.antMatchers(HttpMethod.PUT, "/agent/{id}").hasRole("Admin")    //modifier agent
				.antMatchers(HttpMethod.DELETE, "/agent/{id}").hasRole("Admin")    //supprimer agent
				.antMatchers(HttpMethod.GET, "/agence/{id}/agents").hasRole("Admin")    //afficher agents
				.antMatchers(HttpMethod.GET, "/agent/username/{username}").hasRole("Agent") //agent par username
				.antMatchers(HttpMethod.GET, "/agents").hasRole("Admin")        //afficher agent

				//AGENCE
				.antMatchers(HttpMethod.GET, "/agences").hasAnyRole("Admin", "Agent")        //afficher agences
				.antMatchers(HttpMethod.POST, "/agences").hasRole("Admin")        //creer agences
				.antMatchers(HttpMethod.PUT, "/agence/{id}").hasRole("Admin")    //modifier agence
				.antMatchers(HttpMethod.DELETE, "/agence/{id}").hasRole("Admin")    //supprimer agence

				//DEVISE
				.antMatchers(HttpMethod.GET, "/devise/{id}").hasAnyRole("Admin", "Agent", "Client")
				.antMatchers(HttpMethod.GET, "/devise/all").hasAnyRole("Admin", "Agent", "Client")        //afficher devises
				.antMatchers(HttpMethod.GET, "/devise/convert/{code_src}/{code_dest}").hasAnyRole("Admin","Agent","Client")
				.antMatchers(HttpMethod.POST, "/devise").hasRole("Admin")        //creer devises
				.antMatchers(HttpMethod.PUT, "/devise/{code}").hasRole("Admin")    //modifier devise
				.antMatchers(HttpMethod.DELETE, "/devise/{code}").hasRole("Admin")    //supprimer devise

				//CLIENT
				.antMatchers(HttpMethod.POST, "/clients").hasRole("Agent")        //creer client
				.antMatchers(HttpMethod.GET, "/clients").hasRole("Agent")        //afficher client
				.antMatchers(HttpMethod.GET, "/client/username/{username}").hasRole("Client")        //client par username
				.antMatchers(HttpMethod.GET, "/agence/{id}/clients").hasRole("Agent")    //afficher clients
				.antMatchers(HttpMethod.PUT, "/client/{id}").hasRole("Agent")    //modifier client
				.antMatchers(HttpMethod.DELETE, "/client/{id}").hasRole("Agent")    //supprimer client
				.antMatchers(HttpMethod.POST, "/client/beneficiares").hasRole("Client") //Ajouter un beneficiares au client
				.antMatchers(HttpMethod.GET, "/client/{id}/beneficiares").hasRole("Client") //Afficher les beneficiares du client
				.antMatchers(HttpMethod.DELETE,"/client/beneficiares/{id}").hasRole("Client")
				//COMPTE
				.antMatchers(HttpMethod.GET, "/client/{id}/comptes").hasAnyRole("Agent", "Client")    //afficher comptes
				.antMatchers(HttpMethod.GET, "/comptes").hasAnyRole("Agent", "Client")    //afficher compte
				.antMatchers(HttpMethod.POST, "/comptes").hasRole("Agent")    //creer compte
				.antMatchers(HttpMethod.PUT, "/compte/{id}").hasRole("Agent")    //modifier compte
				.antMatchers(HttpMethod.DELETE, "/compte/{id}").hasRole("Agent")    //supprimer compte
				.antMatchers(HttpMethod.GET, "/compte/{numero}").hasRole("Client")    //afficher compte
				.antMatchers(HttpMethod.GET, "/contratPDF/{id}").hasRole("Agent")    //Contrat PDF

				//VIREMENT ET RECHARGE
				.antMatchers(HttpMethod.GET, "/compte/{id}/virements").hasAnyRole("Client", "Agent")    //afficher virements
				.antMatchers(HttpMethod.GET, "/compte/{id}/virementsEnvoyes").hasAnyRole("Agent", "Client")    //afficher virement envoyes
				.antMatchers(HttpMethod.GET, "/compte/{id}/virementsRecus").hasAnyRole("Agent", "Client")    //afficher virement recus
				.antMatchers(HttpMethod.GET, "/compte/{id}/recharges").hasAnyRole("Client", "Agent")    //afficher recharges
				.antMatchers(HttpMethod.GET, "/virements").hasRole("Client")    //afficher virement
				.antMatchers(HttpMethod.GET, "/virementPDF/{id}").hasRole("Client")    // Reçu virement PDF
				.antMatchers(HttpMethod.POST, "/virements").hasRole("Client")    //creer virement
				.antMatchers(HttpMethod.GET, "/recharges").hasRole("Client")    //afficher recharge
				.antMatchers(HttpMethod.POST, "/recharges").hasRole("Client")    //creer recharge
				.antMatchers(HttpMethod.POST, "/compte/abonnes").hasRole("Client") // ajouter benificaire
				.antMatchers(HttpMethod.DELETE, "/compte/abonnes/{id}").hasRole("Client")

				//OPERATIONS (RETRAIT ET VERSEMENT)
				.antMatchers(HttpMethod.GET, "/operations").hasAnyRole("Agent", "Client")    //afficher operation
				.antMatchers(HttpMethod.GET, "/compte/{id}/operations").hasAnyRole("Agent", "Client")    //afficher operations
				.antMatchers(HttpMethod.GET, "/operationPDF/{id}").hasRole("Agent")    // Reçu operation PDF
				.antMatchers(HttpMethod.POST, "/operations").hasRole("Agent")    //creer operation

				//RDV
				.antMatchers(HttpMethod.GET, "/rdvs").hasRole("Agent") // Liste des RDV
				.antMatchers(HttpMethod.GET, "/client/rdvs").hasRole("Client") // RDVs du client
				.antMatchers(HttpMethod.GET, "/rdvs/booked").hasRole("Client") // RDVs deja reservés
				.antMatchers(HttpMethod.POST, "/rdvs").hasRole("Client")

				.and()
				.httpBasic()
				.and()
				.csrf().disable()
		;


		super.configure(http);
	}


}
