package com.assignment.customerservice.controller;

import com.assignment.customerservice.domain.Customer;
import com.assignment.customerservice.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    @Test
    public void testFindCustomerByPinCode() throws Exception {

        when(customerService.findCustomerByPinCode(12345)).thenReturn(Customer.builder().build());
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/customers")
                .param("pinCode", String.valueOf(12345))
                .param("name", "abc")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testFindCustomerByName() throws Exception {
        when(customerService.findCustomerByName("abc")).thenReturn(Customer.builder().build());
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/customers")
                .param("name", "abc")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testFindCustomerIdByName() throws Exception {
        when(customerService.findUserIdByName("abc")).thenReturn(1);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/customers/findCustomerId/{customerName}", "abc")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(1));
    }

    @Test
    void testUpdateCustomer() throws Exception {
        Customer customer = Customer.builder().build();
        doNothing().when(customerService).updateCustomer(1, customer);
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/customers/{userId}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(customer)))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDownloadProfilePhoto() throws Exception {
        String customerName = "John Doe";
        byte[] profilePhotoData = new byte[]{};

        when(customerService.findProfilePhotoByName(customerName)).thenReturn(profilePhotoData);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/customers/{customerName}/download-photo", customerName)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_JPEG))
                .andExpect(content().bytes(profilePhotoData));

    }

}
