package com.assignment.customerservice.batch;

import com.assignment.customerservice.domain.Customer;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

public class CustomerItemWriter implements ItemWriter<Customer> {
    @Override
    public void write(Chunk<? extends Customer> customers) throws Exception {
        for (Customer customer : customers) {
            System.out.println("Writing: " + customer.toString());
        }
    }
}
