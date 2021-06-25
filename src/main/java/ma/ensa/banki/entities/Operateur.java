package ma.ensa.banki.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import javax.persistence.*;

@Entity
@Table(name = "OPERATEUR")
@Getter @Setter @ToString
public class Operateur {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	String nom;
	String email;
	String logo;
	@OneToOne
	Compte compte;
	@JsonIgnore
	@ManyToOne
	Agent creationAgent;
}
