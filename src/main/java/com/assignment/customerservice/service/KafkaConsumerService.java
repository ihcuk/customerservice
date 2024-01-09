package com.assignment.customerservice.service;

import com.assignment.customerservice.domain.Customer;
import com.assignment.customerservice.dto.CustomerBatchDto;
import com.assignment.customerservice.repository.CustomerRepository;
import com.assignment.customerservice.utils.CustomerServiceUtility;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.net.URLDecoder;
import java.nio.charset.Charset;

@Service
@Slf4j
public class KafkaConsumerService {

    @Autowired
    private FileService fileService;

    @Autowired
    private CustomerRepository customerRepository;

    @KafkaListener(topics = "customers", groupId = "my-group")
    public void listen(String message) throws JsonProcessingException {
        CustomerBatchDto customerBatchDto = new ObjectMapper().readValue(message, CustomerBatchDto.class);

        byte [] profilePhoto = null;
        if(!CustomerServiceUtility.isEmpty(customerBatchDto.getProfilephoto())) {
            try {
                profilePhoto = fileService.readFileToByteArray(URLDecoder.decode(customerBatchDto.getProfilephoto(), Charset.defaultCharset()));
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
        customerRepository.save(customer);
    }
}
