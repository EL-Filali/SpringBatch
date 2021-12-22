package ma.ensa.springbatch.config;

import ma.ensa.springbatch.beans.dto.TransactionDTO;
import ma.ensa.springbatch.beans.model.Transaction;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.function.Function;

@EnableBatchProcessing
@EnableScheduling
@Configuration
public class BatchConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;
    @Autowired
    PlatformTransactionManager dataSourceTransactionManager;
    @Autowired
    DataSource dataSource;
    @Value("${file.input}")
    private String fileInput;
    @Autowired
    private ItemProcessor processor;
    @Autowired
    private ItemWriter writer;
    @Bean
    public FlatFileItemReader reader() {
        FlatFileItemReader<TransactionDTO> reader = new FlatFileItemReader<>();
        reader.setResource((new FileSystemResource(fileInput)));
        reader.setLineMapper(new DefaultLineMapper<>() {{
            setLineTokenizer(new DelimitedLineTokenizer() {{
                setNames(new String[]{"idTransaction", "idCompte", "montant", "dateTransaction"});
                setDelimiter(",");
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
                setTargetType(TransactionDTO.class);
            }});
        }});
        return reader;
    }
    @Bean
    public ItemWriter<Transaction> writer() {
        return writer;
    }

    @Bean
    public ItemProcessor<TransactionDTO, Transaction> processor() {
        return processor;
    }

    @Bean
    public Step step(){
        return stepBuilderFactory.get("Step 1").<Transaction, TransactionDTO>chunk(2).reader(reader()).processor(processor).writer(writer).build();
    }


    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(20);
        return threadPoolTaskExecutor;
    }

    @Bean(name = "importTransactions")
    public Job importTransactions(JobBuilderFactory jobs) {
        return jobs.get("importTransactions")
                .start(step())
                .build();
    }

    @Bean(name = "transactionJobRepository")
    public JobRepository jobRepository() throws Exception {
        JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
        factory.setDataSource(dataSource);
        factory.setTransactionManager(dataSourceTransactionManager);
        return factory.getObject();
    }

    @Bean(name = "joblancher")
    public JobLauncher jobLauncher() throws Exception {
        SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
        jobLauncher.setJobRepository(jobRepository());
        return jobLauncher;
    }

    @Bean
    public BatchLauncher launchBatch() {
        return new BatchLauncher();
    }

    //@Scheduled(cron = "*,0,0,1,*,*")
    public void scheduleFixedDelayTask() throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
        launchBatch().run();
    }

}