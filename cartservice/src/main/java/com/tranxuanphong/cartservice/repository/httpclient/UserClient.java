package com.tranxuanphong.cartservice.repository.httpclient;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "userservice")
public interface UserClient {
  @GetMapping(value = "/api/users/check/id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  boolean doesUserExistById(@PathVariable String id);

  @GetMapping
  (value = "/api/users/check/email/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
  boolean doesUserExistByEmail(@PathVariable String email);

  @GetMapping(value = "/api/users/get/userid/email/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
  String userId(@PathVariable String email);


  @GetMapping(value = "/api/users/get/username/id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  String username(@PathVariable String id);



}
