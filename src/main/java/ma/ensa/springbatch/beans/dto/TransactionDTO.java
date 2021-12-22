package ma.ensa.springbatch.beans.dto;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;


@Data
public class TransactionDTO {

    private Long idTransaction;
    private Date dateTransaction;
    private Long idCompte;
    private Float montant;


}
