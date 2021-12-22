package ma.ensa.springbatch.config;

import ma.ensa.springbatch.beans.model.Transaction;
import ma.ensa.springbatch.repositories.TransactionRepository;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionWriter implements ItemWriter<Transaction> {
    @Autowired
    private TransactionRepository transactionRepository;
    @Override
    public void write(List<? extends Transaction> items) throws Exception {
        System.out.println("Saving Transactions");
        transactionRepository.saveAll(items);

    }
}
