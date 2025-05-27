package com.tranxuanphong.userservice.controller;

import java.text.ParseException;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nimbusds.jose.JOSEException;
import com.tranxuanphong.userservice.dto.request.AuthenticationRequest;
import com.tranxuanphong.userservice.dto.request.IntrospectRequest;
import com.tranxuanphong.userservice.dto.response.ApiResponse;
import com.tranxuanphong.userservice.dto.response.AuthenticationResponse;
import com.tranxuanphong.userservice.dto.response.IntrospectResponse;
import com.tranxuanphong.userservice.service.AuthenticationService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {

  AuthenticationService authenticationService;

  @PostMapping("/token") 
  ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest 
authenticationRequest) { 
    return ApiResponse.<AuthenticationResponse>builder() 
      .result(authenticationService.authenticate(authenticationRequest)) 
      .code(1000) 
      .build(); 
  }

  @PostMapping("/introspect") 
  ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest introspectRequest) throws ParseException, JOSEException { 
    return ApiResponse.<IntrospectResponse>builder() 
      .result(authenticationService.introspect(introspectRequest)) 
      .code(1000) 
      .build(); 
  }
}
