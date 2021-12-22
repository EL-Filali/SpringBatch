package ma.ensa.springbatch.mapper;

import ma.ensa.springbatch.beans.dto.TransactionDTO;
import ma.ensa.springbatch.beans.model.Compte;
import ma.ensa.springbatch.beans.model.Transaction;
import org.springframework.stereotype.Component;

@Component
public class ModelMapper {

    public Transaction getTransactionFromTransactionDTO(TransactionDTO transactionDTO){
        Transaction transaction=new Transaction();
        transaction.setIdTransaction(transactionDTO.getIdTransaction());
        transaction.setDateTransaction(transactionDTO.getDateTransaction());
        transaction.setMontant(transactionDTO.getMontant());
        Compte compte=new Compte();
        compte.setIdCompte(transactionDTO.getIdCompte());
        compte.setMontant(compte.getMontant()-transactionDTO.getMontant());
        transaction.setCompte(compte);
        return  transaction;
    }
}
