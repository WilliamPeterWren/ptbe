package com.tranxuanphong.userservice.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UserUpdatePasswordRequest{

  String password;

  String username;
  
}