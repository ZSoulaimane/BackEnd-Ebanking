package ma.ensa.banki.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "COMPTE")
@Getter @Setter @ToString
public class Compte {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_COMPTE")
	Long id;

	@Column(name = "NUMERO_COMPTE", unique = true)
	String numero;

	@Column(name = "TYPE_COMPTE")
	String type;

	@Column(name = "SOLDE_COMPTE")
	double solde;

	@ManyToOne
	@JoinColumn(name = "DEVISE_COMPTE")
	Devise devise;

	@JsonIgnore
	@Column(name = "CREATION_DATE_COMPTE")
	@CreationTimestamp
	LocalDateTime creationDate;

	@JoinColumn(name = "PROPRIETAIRE_COMPTE")
	@ManyToOne
	Client proprietaire;

	@JsonIgnore
	@JoinColumn(name = "CREATION_AGENT_COMPTE")
	@ManyToOne
	Agent creationAgent;

	@JsonIgnore
	@Column(name = "VIREMENTS_ENVOYES_COMPTE")
	@OneToMany(mappedBy = "debiteur", cascade = CascadeType.ALL)
	List<Virement> virementsEnvoyes;

	@JsonIgnore
	@Column(name = "VIREMENTS_RECUS_COMPTE")
	@OneToMany(mappedBy = "creancier", cascade = CascadeType.ALL)
	List<Virement> virementsRecus;

	@JsonIgnore
	@Column(name = "RECHARGES_COMPTE")
	@OneToMany(mappedBy = "compte", cascade = CascadeType.ALL)
	List<Recharge> recharges;

	@JsonIgnore
	@Column(name = "OPERATIONS_COMPTE")
	@OneToMany(mappedBy = "compte", cascade = CascadeType.ALL)
	List<Operation> operations;

}
