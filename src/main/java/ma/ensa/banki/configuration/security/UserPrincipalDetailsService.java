package ma.ensa.banki.configuration.security;

import ma.ensa.banki.entities.Utilisateur;
import ma.ensa.banki.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserPrincipalDetailsService implements UserDetailsService {

	@Autowired
	UserRepository rep;


	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Utilisateur utilisateur = rep.findByUsername(username).get();
		return new UserPrincipal(utilisateur);
	}

}
