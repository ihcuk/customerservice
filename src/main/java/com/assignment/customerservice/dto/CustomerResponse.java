package com.assignment.customerservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerResponse {
    private Integer userid;
    private String password;
    private String name;
    private String address;
    private Integer pincode;
    private byte[] profilephoto;

}
