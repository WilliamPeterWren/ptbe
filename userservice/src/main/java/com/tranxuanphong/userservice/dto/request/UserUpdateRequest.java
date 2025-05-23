package com.tranxuanphong.userservice.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UserUpdateRequest{
  @NotBlank(message = "Password is required")
  @Size(min = 8, max = 32, message = "Password must be at least 8 characters") 
  String password;

//  @NotBlank(message = "First Name is required")
//  @Size(min = 1, max = 32, message = "First name must be at least 8 characters")
//  String firstName;
//
//  @NotBlank(message = "Last name is required")
//  @Size(min = 1, max = 32, message = "Last name must be at least 8 characters")
//  String lastName;
//
//  LocalDate dob;
}