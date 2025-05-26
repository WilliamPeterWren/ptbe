package com.tranxuanphong.userservice.dto.request;

import com.tranxuanphong.userservice.validator.PhoneConstraint;
import lombok.Getter;
import java.time.LocalDate;

import com.tranxuanphong.userservice.validator.DobConstraint;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Getter
public class AddressCreateRequest {

  @NotBlank(message = "First Name is required")
  @Size(min = 1, max = 32, message = "FIRSTNAME_INVALID")
  String firstName;

  @NotBlank(message = "Last name is required")
  @Size(min = 1, max = 32, message = "LASTNAME_INVALID")
  String lastName;

  @DobConstraint(min = 18, message = "INVALID_DOB")
  LocalDate dob;

  @NotBlank(message = "Address must be required")
  @Size(min=4, message = "ADDRESS_INVALID")
  String address;

  @PhoneConstraint(min = 10, message = "INVALID_PHONE")
  String phone;

}
