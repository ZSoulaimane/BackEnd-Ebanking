package ma.ensa.banki.services;

import ma.ensa.banki.entities.Admin;
import ma.ensa.banki.exceptions.AlreadyExistsException;
import ma.ensa.banki.exceptions.NotFoundException;
import ma.ensa.banki.repositories.AdminRepository;
import ma.ensa.banki.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdminService {
	@Autowired
	AdminRepository adminRepo;
	@Autowired
	UserRepository userRepo;
	Logger logger = LoggerFactory.getLogger(AdminService.class.getName());


	public Admin getByUsername(String username) {
		return adminRepo.findByUsername(username)
				.orElseThrow(() -> new NotFoundException("Aucun administrateur avec le username " + username + " trouvé"));
	}


	public List<Admin> getAdmins(Long id) {
		List<Admin> admins = new ArrayList<>();
		if (id != null) {
			admins.add(adminRepo.findById(id).orElseThrow(() -> new NotFoundException("Aucun administrateur avec l'id " + id + " trouvé")));
		} else {
			admins = adminRepo.findAll();
		}
		if (admins.isEmpty()) {
			throw new NotFoundException("Aucun administrateur trouvé");
		}
		return admins;
	}


	public void addAdmin(Admin admin) {
		if (userRepo.findByUsername(admin.getUsername()).isPresent()) {
			throw new AlreadyExistsException("Veuillez choisir un autre Username");
		}
		if (userRepo.findByCin(admin.getCin()).isPresent()) {
			throw new AlreadyExistsException("Un administrateur avec le CIN " + admin.getCin() + " existe déjà");
		}
		String password = admin.getPassword();
		admin.setPassword(new BCryptPasswordEncoder().encode(password));
		admin.setRole("Admin");
		adminRepo.save(admin);
		Admin user = getByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		logger.debug("L'administrateur " + user.getNom() + " " + user.getPrenom() + " ayant le Username " + user.getUsername() + " a créé l'administrateur avec le username " + admin.getUsername());
	}

	public void updateAdmin(Long id, Admin newAdmin) {
		Admin oldAdmin = adminRepo.findById(id).orElseThrow(() -> new NotFoundException("Aucun administrateur avec l'id " + id + " trouvé"));
		//verifier l'unicité du nouveau username
		if (userRepo.findByUsername(newAdmin.getUsername()).isPresent()) {
			throw new AlreadyExistsException("Veuillez choisir un autre Username");
		}
		//verifier l'unicité du nouveau CIN
		if (userRepo.findByUsername(newAdmin.getCin()).isPresent()) {
			throw new AlreadyExistsException("Un administrateur avec le CIN " + newAdmin.getCin() + " existe déjà");
		}

		oldAdmin.setNom(newAdmin.getNom());
		oldAdmin.setPrenom(newAdmin.getPrenom());
		oldAdmin.setCin(newAdmin.getCin());
		oldAdmin.setTelephone(newAdmin.getTelephone());
		oldAdmin.setAdresse(newAdmin.getAdresse());
		oldAdmin.setUsername(newAdmin.getUsername());
		oldAdmin.setEmail(newAdmin.getEmail());
		String newPassword = newAdmin.getPassword();
		oldAdmin.setPassword(new BCryptPasswordEncoder().encode(newPassword));

		adminRepo.save(oldAdmin);
		Admin user = getByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		logger.debug("L'administrateur " + user.getNom() + " " + user.getPrenom() + " ayant le Username " + user.getUsername() + " a modifié l'administrateur avec le username " + oldAdmin.getUsername());
	}


	public void removeAdmin(Long id) {
		//vérifier l'existence de l'administrateur
		Admin admin = adminRepo.findById(id).orElseThrow(() -> new NotFoundException("Aucun administrateur avec l'id " + id + " n'est trouvé"));
		adminRepo.delete(admin);
		Admin user = getByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		logger.debug("L'administrateur " + user.getNom() + " " + user.getPrenom() + " ayant le Username " + user.getUsername() + " a supprimé l'administrateur avec le username " + admin.getUsername());
	}
}
