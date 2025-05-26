package com.tranxuanphong.userservice.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UserUpdatePasswordRequest{
  @NotBlank(message = "Password is required")
  @Size(min = 8, max = 32, message = "Password must be at least 8 characters") 
  String password;

  
}