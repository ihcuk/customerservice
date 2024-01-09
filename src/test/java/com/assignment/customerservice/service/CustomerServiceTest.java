package com.assignment.customerservice.service;

import com.assignment.customerservice.domain.Customer;
import com.assignment.customerservice.repository.CustomerRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindCustomerByPinCode() throws JsonProcessingException {
        Integer pinCode = 123;
        Customer mockCustomer = new Customer();
        when(customerRepository.findByPincode(pinCode)).thenReturn(mockCustomer);
        Customer result = customerService.findCustomerByPinCode(pinCode);
        verify(customerRepository, times(1)).findByPincode(pinCode);
        assertEquals(mockCustomer, result);
    }

    @Test
    public void testFindCustomerByName() {
        String customerName = "abc";
        Customer mockCustomer = new Customer();
        when(customerRepository.findByName(customerName)).thenReturn(mockCustomer);
        Customer result = customerService.findCustomerByName(customerName);
        verify(customerRepository, times(1)).findByName(customerName);
        assertEquals(mockCustomer, result);
    }

    @Test
    public void testFindUserIdByName() {
        String customerName = "abc";
        when(customerRepository.findUseridByName(customerName)).thenReturn(1);
        Integer result = customerService.findUserIdByName(customerName);
        verify(customerRepository, times(1)).findUseridByName(customerName);
        assertEquals(1, result);
    }

    @Test
    public void testUpdateCustomer() {
        Customer inputCustomer = Customer.builder()
                .name("a")
                .password("a")
                .pincode(1)
                .profilephoto("Some".getBytes(StandardCharsets.UTF_8))
                .build();
        Customer expectedCustomer = Customer.builder().build();
        when(customerRepository.findById(1)).thenReturn(java.util.Optional.ofNullable(expectedCustomer));
        customerService.updateCustomer(1, inputCustomer);
        verify(customerRepository, times(1)).findById(1);
    }

    @Test
    public void testFindProfilePhotoByName() throws IOException {
        when(customerRepository.findProfilephotoByName("a")).thenReturn("some".getBytes(StandardCharsets.UTF_8));
        customerService.findProfilePhotoByName("a");
        verify(customerRepository, times(1)).findProfilephotoByName("a");
    }

    @Test
    public void testHandleCreateCustomer() throws IOException {
        String password = "password";
        String name = "John Doe";
        String address = "123 Main St";
        Integer pinCode = 456;
        MockMultipartFile profilePhoto = new MockMultipartFile("profilePhoto", "photo.jpg", "image/jpeg", "SomeData".getBytes());
        customerService.handleCreateCustomer(password, name, address, pinCode, profilePhoto);
    }

}
