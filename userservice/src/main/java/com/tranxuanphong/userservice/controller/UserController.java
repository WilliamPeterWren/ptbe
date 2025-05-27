package com.tranxuanphong.userservice.controller;


import org.springframework.web.bind.annotation.RestController;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tranxuanphong.userservice.dto.request.LoginRequest;
import com.tranxuanphong.userservice.dto.request.RegisterRequest;
import com.tranxuanphong.userservice.dto.request.UserUpdatePasswordRequest;
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
  public ApiResponse<UserResponse> updatePassword(@RequestBody UserUpdatePasswordRequest request){
    return ApiResponse.<UserResponse>builder()
    .result(userService.updatePassword(request))
    .build();
  }

  @GetMapping
  public ApiResponse<UserResponse> getUser() {
    return ApiResponse.<UserResponse>builder()
    .result(userService.getUser())
    .build();
  }

  @GetMapping("/get-all")
  public ApiResponse<List<UserResponse>> getAll(){
    return ApiResponse.<List<UserResponse>>builder()
    .result(userService.getAll())
    .build();
  }

  @PostMapping("/login")
  public ApiResponse<LoginResponse> login(@RequestBody @Valid LoginRequest request){
    return ApiResponse.<LoginResponse>builder()
    .result(userService.login(request))
    .build();
  }

  @PutMapping("/update-role/{roleId}")
  public ApiResponse<UserResponse> updateRole(@PathVariable String roleId) {
    return ApiResponse.<UserResponse>builder()
    .result(userService.updateRole(roleId))
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
    System.out.println("email: " + email);
    return userService.getUserId(email);
  }



}
