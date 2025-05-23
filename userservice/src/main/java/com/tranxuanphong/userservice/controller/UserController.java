package com.tranxuanphong.userservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.tranxuanphong.userservice.dto.request.ApiResponse;
import com.tranxuanphong.userservice.dto.request.LoginRequest;
import com.tranxuanphong.userservice.dto.request.RegisterRequest;
import com.tranxuanphong.userservice.dto.request.UserUpdateRequest;
import com.tranxuanphong.userservice.dto.response.LoginResponse;
import com.tranxuanphong.userservice.dto.response.UserResponse;
import com.tranxuanphong.userservice.service.UserService;

import jakarta.validation.Valid;


@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
  private static final Logger log = LoggerFactory.getLogger(UserController.class);
  UserService userService;

  @PostMapping("/register")
  public ApiResponse<UserResponse> register(@RequestBody @Valid RegisterRequest request) {
    return ApiResponse.<UserResponse>builder()
    .result(userService.register(request))
    .build();
  }
  
  @PutMapping("/{email}")
  public ApiResponse<UserResponse> update(@PathVariable @Valid String email, @RequestBody UserUpdateRequest request){
    return ApiResponse.<UserResponse>builder()
    .result(userService.update(email, request))
    .build();
  }

  @GetMapping("/{email}")
  public ApiResponse<UserResponse> getUser(@PathVariable String email) {
    return ApiResponse.<UserResponse>builder()
    .result(userService.getUser(email))
    .build();
  }

  @GetMapping("/get-all")
  public ApiResponse<List<UserResponse>> getAll(){
    return ApiResponse.<List<UserResponse>>builder()
    .result(userService.getAll())
    .build();
  }

  @GetMapping("/myInfo") 
  public ApiResponse<UserResponse> getMyInfo(){ 
    return ApiResponse.<UserResponse>builder() 
    .result(userService.getMyInfo()) 
    .build(); 
  }

  @PostMapping("/login")
  public ApiResponse<LoginResponse> login(@RequestBody @Valid LoginRequest request){
    return ApiResponse.<LoginResponse>builder()
    .result(userService.login(request))
    .build();
  }


  // Product
  @GetMapping("/check-id/{id}")
  public boolean checkId(@PathVariable String id) {
    // log.info("id: " +id);
    return userService.checkId(id);
  }


  @GetMapping("/get-userid/{email}")
  public String getUserId(@PathVariable String email){
    System.out.println("email: " + email);
    return userService.getUserId(email);
  }



}
