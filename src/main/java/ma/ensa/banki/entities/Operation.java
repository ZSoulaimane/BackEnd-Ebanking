package ma.ensa.banki.entities;

import lombok.Data;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "OPERATION")
@Getter @Setter @ToString
public class Operation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_OPERATION")
	Long id;

	@JoinColumn(name = "COMPTE_OPERATION")
	@ManyToOne
	Compte compte;

	@Column(name = "DATE_OPERATION")
	@CreationTimestamp
	LocalDateTime date;

	@Column(name = "SOMME_ESPECE_OPERATION")
	double sommeEspece;

	@Column(name = "SOMME_COMPTE_OPERATION")
	double sommeCompte;

	@Column(name = "TYPE_OPERATION")
	String type;

	@ManyToOne
	@JoinColumn(name = "DEVISE_OPERATION")
	Devise devise;

}
