package com.tranxuanphong.productservice.repository.httpclient;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "apigateway")
public interface UserClient {
  @GetMapping(value = "/api/users/check-id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  boolean existsId(@PathVariable String id);
}
