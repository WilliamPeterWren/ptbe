package com.tranxuanphong.userservice.repository.httpclient;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "orderservice")
public interface OrderClient {
  @GetMapping(value = "/api/orders/exists-by-address/{addressId}", produces = MediaType.APPLICATION_JSON_VALUE)
  boolean existsByAddressId(@PathVariable String id);

}
