package com.assignment.customerservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerBatchDto {
    private String password;
    private String name;
    private String address;
    private String pincode;
    private String profilephoto;
}
