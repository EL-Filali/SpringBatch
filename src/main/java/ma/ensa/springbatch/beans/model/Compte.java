package ma.ensa.springbatch.beans.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;

@Entity
@Data
public class Compte {
    @Id
    private Long idCompte;
    private Float montant;

    public Compte() {
        montant=0f;
    }
}
