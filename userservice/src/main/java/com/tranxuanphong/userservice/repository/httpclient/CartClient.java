package com.tranxuanphong.userservice.repository.httpclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import com.tranxuanphong.userservice.dto.response.ApiResponse;
import com.tranxuanphong.userservice.dto.response.CartResponse;

import org.springframework.http.MediaType;

@FeignClient(name = "cartservice")
public interface CartClient {
  @PostMapping(value = "/api/carts", produces = MediaType.APPLICATION_JSON_VALUE)
  public ApiResponse<CartResponse> create(@RequestHeader("Authorization") String authorizationHeader);
}





