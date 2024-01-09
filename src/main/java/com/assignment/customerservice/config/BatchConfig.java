package com.assignment.customerservice.config;

import com.assignment.customerservice.domain.Customer;
import com.assignment.customerservice.dto.CustomerBatchDto;
import com.assignment.customerservice.listener.CustomerJobListener;
import com.assignment.customerservice.repository.CustomerRepository;
import com.assignment.customerservice.service.FileService;
import com.assignment.customerservice.utils.CustomerServiceUtility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
@Slf4j
public class BatchConfig {

    @Autowired
    private FileService fileService;

    @Bean
    public FlatFileItemReader<CustomerBatchDto> reader() {
        FlatFileItemReader<CustomerBatchDto> flatFileItemReader = new FlatFileItemReader<>();
        flatFileItemReader.setResource(new FileSystemResource("D:/Altimterik/BatchJob/BatchJobData.csv"));
        flatFileItemReader.setLinesToSkip(1);

        flatFileItemReader.setLineMapper(new DefaultLineMapper<>() {{
            setLineTokenizer(new DelimitedLineTokenizer() {{
                setDelimiter(DELIMITER_COMMA);
                setNames("password","name","address","pincode","profilephoto");
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<>(){{
                setTargetType(CustomerBatchDto.class);
            }});
        }});

        return flatFileItemReader;
    }

    @Bean
    public ItemProcessor<CustomerBatchDto, Customer> processor() {
        return customerBatchDto -> {
            byte [] profilePhoto = null;
            if(!CustomerServiceUtility.isEmpty(customerBatchDto.getProfilephoto())) {
                try {
                    profilePhoto = fileService.readFileToByteArray(customerBatchDto.getProfilephoto());
                } catch (Exception exception) {
                    log.error("Error While reading the file at path {}", customerBatchDto.getProfilephoto());
                }

            }
            Customer customer = Customer.builder()
                    .password(customerBatchDto.getPassword())
                    .name(customerBatchDto.getName())
                    .address(customerBatchDto.getAddress())
                    .profilephoto(profilePhoto)
                    .build();
            if (!CustomerServiceUtility.isEmpty(customerBatchDto.getPincode())) {
                customer.setPincode(Integer.parseInt(customerBatchDto.getPincode()));
            }

            return customer;
        };
    }

    @Bean
    public ItemWriter<Customer> writer() {
        return customers -> {
            customerRepository.saveAll(customers);
        };
    }

    @Bean
    public JobExecutionListener listener() {
        return new CustomerJobListener();
    }

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private PlatformTransactionManager platformTransactionManager;

    @Autowired
    private CustomerRepository customerRepository;


    //Step Object
    @Bean
    public Step step() {
        return new StepBuilder("Read Customers Data From CSV", jobRepository)
                .<CustomerBatchDto,Customer>chunk(1000, platformTransactionManager)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }

    @Bean
    public Job job(){
        return new JobBuilder("Register Customers", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(listener())
                .start(step())
                .build();
    }
}
