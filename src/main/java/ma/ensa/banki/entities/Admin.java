package ma.ensa.banki.entities;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "ADMIN")
@AttributeOverrides({
		@AttributeOverride(name = "id", column = @Column(name = "ID_ADMIN")),
		@AttributeOverride(name = "nom", column = @Column(name = "NOM_ADMIN")),
		@AttributeOverride(name = "prenom", column = @Column(name = "PRENOM_ADMIN")),
		@AttributeOverride(name = "cin", column = @Column(name = "CIN_ADMIN")),
		@AttributeOverride(name = "adresse", column = @Column(name = "ADRESSE_ADMIN")),
		@AttributeOverride(name = "telephone", column = @Column(name = "TELEPHONE_ADMIN")),
		@AttributeOverride(name = "email", column = @Column(name = "EMAIL_ADMIN")),
		@AttributeOverride(name = "username", column = @Column(name = "USERNAME_ADMIN")),
		@AttributeOverride(name = "password", column = @Column(name = "PASSWORD_ADMIN")),
		@AttributeOverride(name = "role", column = @Column(name = "ROLE_ADMIN"))
})
@Getter
@Setter
@ToString
public class Admin extends Utilisateur {
}
