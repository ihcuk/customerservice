package com.assignment.customerservice.batch;

import com.assignment.customerservice.domain.Customer;
import com.assignment.customerservice.dto.CustomerBatchDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

@Slf4j
public class CustomerItemProcessor implements ItemProcessor<CustomerBatchDto, Customer> {
    @Override
    public Customer process(CustomerBatchDto customerBatchDto) throws Exception {
        log.info("CustomerBatch DTO Received", customerBatchDto.toString());
        return null;
    }
}
