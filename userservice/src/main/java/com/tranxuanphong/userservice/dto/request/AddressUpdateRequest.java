package com.tranxuanphong.userservice.dto.request;

import java.time.LocalDate;

import lombok.Getter;

@Getter
public class AddressUpdateRequest {
  String firstName;
  String lastName;
  LocalDate dob;
  String address;
  String phone;
  Boolean gender;
}
