package ma.ensa.springbatch.config;

import ma.ensa.springbatch.beans.dto.TransactionDTO;
import ma.ensa.springbatch.beans.model.Transaction;
import ma.ensa.springbatch.mapper.ModelMapper;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionProcessor implements ItemProcessor<TransactionDTO,Transaction> {

    @Autowired
    ModelMapper mapper;
    @Override
    public Transaction process(TransactionDTO item) throws Exception {
        return mapper.getTransactionFromTransactionDTO(item);
    }
}
