package ma.ensa.banki.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.*;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Getter @Setter @ToString
public abstract class Utilisateur {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	String nom;
	String prenom;
	String cin;
	String adresse;
	String telephone;
	String email;
	@Column(unique = true, nullable = false)
	String username;
	@Column(nullable = false)
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	String password;
	@Column(nullable = false)
	String role;
	@Column(nullable = false)
	String locked = "N";
}
