package com.oms.dto;

import lombok.Data;

@Data
public class CustomerUpdateDto {
    private String firstName;
    private String lastName;
    private String phone;
    private String address;
    private String city;
    private String state;
    private String zipCode;
}
