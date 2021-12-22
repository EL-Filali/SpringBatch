package ma.ensa.springbatch.beans.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
public class Transaction {
    @Id
    @Column(name = "id", nullable = false)
    private Long idTransaction;
    private Date dateTransaction;
    private Date dateDebit;
    private Float montant;
    @ManyToOne(cascade={CascadeType.ALL})
    private Compte compte;

    @PrePersist
    private void  prePersist(){
        dateDebit=new Date();
    }

}
