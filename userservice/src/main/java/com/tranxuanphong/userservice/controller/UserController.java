package com.tranxuanphong.userservice.controller;


import org.springframework.web.bind.annotation.RestController;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tranxuanphong.userservice.dto.request.LoginRequest;
import com.tranxuanphong.userservice.dto.request.RegisterRequest;
import com.tranxuanphong.userservice.dto.request.UserUpdateRequest;
import com.tranxuanphong.userservice.dto.response.ApiResponse;
import com.tranxuanphong.userservice.dto.response.LoginResponse;
import com.tranxuanphong.userservice.dto.response.UserResponse;
import com.tranxuanphong.userservice.service.UserService;

import jakarta.validation.Valid;



@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
  UserService userService;

  @PostMapping("/register")
  public ApiResponse<UserResponse> register(@RequestBody @Valid RegisterRequest request) {
    return ApiResponse.<UserResponse>builder()
    .result(userService.register(request))
    .build();
  }
  
  @PutMapping
  public ApiResponse<UserResponse> update(@RequestBody UserUpdateRequest request){
    return ApiResponse.<UserResponse>builder()
    .result(userService.update(request))
    .build();
  }

  @GetMapping
  public ApiResponse<UserResponse> getUser() {
    return ApiResponse.<UserResponse>builder()
    .result(userService.getUser())
    .build();
  }



  @PostMapping("/login")
  public ApiResponse<LoginResponse> login(@RequestBody @Valid LoginRequest request){
    return ApiResponse.<LoginResponse>builder()
    .result(userService.login(request))
    .build();
  }


  // Product
  @GetMapping("/check/id/{id}")
  public boolean checkById(@PathVariable String id) {
    return userService.checkId(id);
  }

  @GetMapping("/check/email/{email}")
  public boolean checkByEmail(@PathVariable String email) {
    return userService.checkByEmail(email);
  }


  @GetMapping("/get/userid/email/{email}")
  public String getUserId(@PathVariable String email){
    return userService.getUserId(email);
  }

  @GetMapping("/get/username/email/{email}")
  public String getUsernameByEmail(@PathVariable String email){
    return userService.getUsernameByEmail(email);
  }

  @GetMapping("/get/username/id/{id}")
  public String getUsernameById(@PathVariable String id){
    return userService.getUsernameById(id);
  }


  @PostMapping("/update/rating/star/{star}/seller/id/{sellerId}")
  public UserResponse updateRatingBySellerId(@PathVariable int star, @PathVariable String sellerId){
    return userService.updateRatingBySellerId(star, sellerId);
  }

  @GetMapping("/get/avatar/user/id/{id}")
  public String userAvatar(@PathVariable String id) {
    return userService.userAvatar(id);
  }

  @GetMapping("/verify/id/{id}")
  public void verifyUserById(@PathVariable String id) {     
      userService.verifyUserById(id);
  }
  

  @PostMapping("/update/petervoucher/id/{petervoucherid}/user/id/{userid}")
  public void updateUserPeterVoucher(@PathVariable String petervoucherid, @PathVariable String userid) {
    userService.updateUserPeterVoucher(userid, petervoucherid);
  }
  

}
