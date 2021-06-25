package ma.ensa.banki.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "DEVISE")
@Getter @Setter @ToString
public class Devise {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_DEVISE")
	Long id;

	@Column(name = "CODE_DEVISE", unique = true)
	String code;

	@Column(name = "NOM_DEVISE")
	String nom;

	@JsonIgnore
	@Column(name = "CREATION_DATE_DEVISE")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@CreationTimestamp
	LocalDateTime creationDate;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "CREATION_ADMIN_DEVISE")
	Admin creationAdmin;

	@JsonIgnore
	@Column(name = "MODIFICATION_DATE_DEVISE")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@CreationTimestamp
	LocalDateTime modificationDate;
}
