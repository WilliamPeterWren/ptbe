package com.tranxuanphong.cartservice.repository.httpclient;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "apigateway")
public interface UserClient {
  @GetMapping(value = "/api/users/check-id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  boolean existsId(@PathVariable String id);

  @GetMapping(value = "/api/users/get-userid/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
  // String getUserid(@PathVariable String email);
  ResponseEntity<Map<String, String>> getUserid(@PathVariable String email);
}
