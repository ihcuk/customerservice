package com.assignment.customerservice.service;

import com.assignment.customerservice.domain.Customer;
import com.assignment.customerservice.exception.CustomerNotFoundException;
import com.assignment.customerservice.repository.CustomerRepository;
import com.assignment.customerservice.utils.CustomerServiceUtility;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public Customer findCustomerByPinCode(Integer pinCode) throws JsonProcessingException {
        return customerRepository.findByPincode(pinCode);

    }

    public Customer findCustomerByName(String name) {
        return customerRepository.findByName(name);
    }

    public Integer findUserIdByName(String name) {
        return customerRepository.findUseridByName(name);
    }

    @Async
    public void updateCustomer(Integer userId, Customer updatedCustomer) {
        Customer existingCustomer = customerRepository.findById(userId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with userId: " + userId));

        if (!CustomerServiceUtility.isEmpty(updatedCustomer.getPassword())) {
            existingCustomer.setPassword(updatedCustomer.getPassword());
        }
        if (!CustomerServiceUtility.isEmpty(updatedCustomer.getName())) {
            existingCustomer.setName(updatedCustomer.getName());
        }
        if (!CustomerServiceUtility.isEmpty(updatedCustomer.getAddress())) {
            existingCustomer.setAddress(updatedCustomer.getAddress());
        }
        if (!CustomerServiceUtility.isEmpty(updatedCustomer.getPincode())) {
            existingCustomer.setPincode(updatedCustomer.getPincode());
        }
        if (!CustomerServiceUtility.isEmpty(updatedCustomer.getProfilephoto())) {
            existingCustomer.setProfilephoto(updatedCustomer.getProfilephoto());
        }
        customerRepository.save(existingCustomer);
    }

    public byte[] findProfilePhotoByName(String customerName) {
        return customerRepository.findProfilephotoByName(customerName);
    }

    @Async
    public void handleCreateCustomer(String password, String name, String address, Integer pinCode,
                                     MultipartFile profilePhoto) throws IOException {
        saveAllCustomerData(List.of(createCustomerData(password, name, address, pinCode, profilePhoto)));
    }

    private void saveAllCustomerData(List<Customer> customerList) {
        customerRepository.saveAll(customerList);
    }

    private Customer createCustomerData(String password, String name, String address, Integer pinCode,
                                       MultipartFile profilePhoto) throws IOException {
        Customer customer =  Customer.builder()
                .password(password)
                .name(name)
                .address(address)
                .pincode(pinCode)
                .build();

        if(!CustomerServiceUtility.isEmpty(profilePhoto)) {
            customer.setProfilephoto(profilePhoto.getBytes());
        }

        return customer;
    }
}
