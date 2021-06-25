package ma.ensa.banki.entities;

import lombok.Data;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "VIREMENT")
@Getter @Setter @ToString
public class Virement {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_VIREMENT")
	Long id;

	@JoinColumn(name = "CREANCIER_VIREMENT")
	@ManyToOne
	Compte creancier;

	@JoinColumn(name = "DEBITEUR_VIREMENT")
	@ManyToOne
	Compte debiteur;

	@Column(name = "DATE_VIREMENT")
	@CreationTimestamp
	LocalDateTime date;

	@Column(name = "SOMME_ENV_VIREMENT")
	double sommeEnv;

	@Column(name = "SOMME_RECU_VIREMENT")
	double sommeRecu;

}
