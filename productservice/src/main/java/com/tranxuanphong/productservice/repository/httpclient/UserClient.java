package com.tranxuanphong.productservice.repository.httpclient;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import com.tranxuanphong.productservice.dto.response.UserResponse;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "userservice")
public interface UserClient {
  @GetMapping(value = "/api/users/check/id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  boolean existsById(@PathVariable String id);

  @GetMapping(value = "/api/users/check/email/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
  boolean existsByEmail(@PathVariable String email);

  @GetMapping(value = "/api/users/get/userid/email/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
  String userId(@PathVariable String email);

  @GetMapping(value = "/api/users/get/username/email/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
  String usernameByEmail(@PathVariable String email);

  @GetMapping(value = "/api/users/get/username/id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  String usernameByUserId(@PathVariable String id);

  @PostMapping("/api/users/update/rating/star/{star}/seller/id/{sellerId}")
  UserResponse updateRatingBySellerId(@PathVariable int star, @PathVariable String sellerId, @RequestHeader("Authorization") String authorizationHeader);

  @GetMapping("/api/users/get/avatar/user/id/{id}")
  public String userAvatar(@PathVariable String id);
}
