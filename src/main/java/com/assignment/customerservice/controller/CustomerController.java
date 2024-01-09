package com.assignment.customerservice.controller;

import com.assignment.customerservice.domain.Customer;
import com.assignment.customerservice.service.CustomerService;
import com.assignment.customerservice.utils.CustomerServiceUtility;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/customers")
@Slf4j
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping(produces = "application/json")
    public ResponseEntity<Customer> findCustomerByPinCode(@RequestParam(required = false) Integer pinCode, @RequestParam(required = false) String name) throws JsonProcessingException {
        log.info("Fetch Customer API Invoked with pinCode: {} for name: {}", pinCode, name);
        Customer customer = null;
        try {
            if (!CustomerServiceUtility.isEmpty(pinCode)) {
                customer = customerService.findCustomerByPinCode(pinCode);
            } else if (!CustomerServiceUtility.isEmpty(name)) {
                customer = customerService.findCustomerByName(name);
            }
        } catch (Exception exception) {
            log.error("Exception while fetching Customer for API with error:{}", exception.getMessage());
        }
        log.info("findCustomerByPinCode is Successful");
        return ResponseEntity.ok(customer);
    }


    @GetMapping(value = "/findCustomerId/{customerName}")  // Changed the mapping
    public ResponseEntity<Integer> findCustomerIdByName(@PathVariable String customerName) {
        return ResponseEntity.ok(customerService.findUserIdByName(customerName));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Void> updateCustomer(
            @PathVariable Integer userId,
            @RequestBody Customer updatedCustomer) {
        customerService.updateCustomer(userId, updatedCustomer);
        return ResponseEntity.noContent().build();
    }


    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> handleCreateCustomer(
            @RequestParam(value = "password", required = false) String password,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "address", required = false) String address,
            @RequestParam(value = "pincode", required = true) Integer pinCode,
            @RequestPart("profilePhoto") MultipartFile profilePhoto) {
        try {
            customerService.handleCreateCustomer(password, name, address, pinCode, profilePhoto);
            return ResponseEntity.ok("Customer saved successfully with photo.");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error saving customer with photo.");
        }
    }


    @GetMapping("/{customerName}/download-photo")
    public ResponseEntity<byte[]> downloadProfilePhoto(@PathVariable String customerName) {
        byte[] profilePhoto = customerService.findProfilePhotoByName(customerName);

        if (profilePhoto != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            headers.setContentLength(profilePhoto.length);
            headers.setContentDisposition(
                    ContentDisposition.builder("attachment")
                            .filename("profilePhoto.jpg")
                            .build());

//
//            headers.setContentDispositionFormData("attachment", "profilePhoto.jpg");

            return new ResponseEntity<>(profilePhoto, headers, HttpStatus.OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
