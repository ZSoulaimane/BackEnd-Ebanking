package ma.ensa.banki.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "VIREMENT_Multi_Ben ")
@Getter
@Setter
@ToString
public class VirementMultiBen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_VIREMENTMulti")
    Long id;

    @JoinColumn(name = "CREANCIER_VIREMENT",nullable = true)
    @ManyToOne
    Compte creancier;

    @JoinColumn(name = "Montant")
    Double montant;

    @ManyToOne
    @JoinColumn(name= "VirementMultipleId")
    VirementMultiple virementMultiple;

}
