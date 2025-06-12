package com.tranxuanphong.userservice.dto.request;


import lombok.Getter;

@Getter
public class UserUpdateRequest{

  String password;

  String username;
  
  String avatar;

}