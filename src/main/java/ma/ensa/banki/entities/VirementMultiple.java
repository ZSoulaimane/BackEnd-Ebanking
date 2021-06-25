package ma.ensa.banki.entities;

import lombok.Data;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "VIREMENTMULTIPLE")
@Getter @Setter @ToString
public class VirementMultiple {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_VIREMENT_MULTIPLE")
	Long id;

	@JoinColumn(name = "DEBITEUR_VIREMENT_MULTIPLE")
	@ManyToOne
	Compte debiteur;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "virementMultiple")
	List<VirementMultiBen> creanciers;

	@Column(name = "DATE_VIREMENT_MULTIPLE")
	@CreationTimestamp
	LocalDateTime date;

	@Column(name = "NB_BENIFICIARES_VIREMENT_MULTIPLE")
	int nombreBeneficiaires;
}
