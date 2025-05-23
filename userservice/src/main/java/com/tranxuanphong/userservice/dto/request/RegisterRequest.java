package com.tranxuanphong.userservice.dto.request;


import lombok.Getter;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Getter
public class RegisterRequest {

  @NotBlank(message = "Email is required")
  @Email(message = "EMAIL_INVALID")
  String email;

  @NotBlank(message = "Password is required")
  @Size(min = 8, max = 32, message = "PASSWORD_INVALID") 
  String password;

}
