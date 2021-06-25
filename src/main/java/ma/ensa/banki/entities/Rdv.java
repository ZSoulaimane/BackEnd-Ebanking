package ma.ensa.banki.entities;

import lombok.Data;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter @Setter @ToString
public class Rdv {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	@ManyToOne
	Client client;
	@ManyToOne
	Agence agence;
	@Column(nullable = false, columnDefinition = "DATE")
	LocalDate dateRdv;
	@Column(nullable = false, columnDefinition = "TIME")
	LocalTime heureRdv;
	@Column
	String service;
}
