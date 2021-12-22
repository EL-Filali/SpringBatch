package ma.ensa.springbatch.repositories;

import ma.ensa.springbatch.beans.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction,Long> {


}
