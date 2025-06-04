package com.tranxuanphong.userservice.repository.httpclient;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "productservice")
public interface ProductClient {
  @GetMapping(value = "/api/products/count/seller/{sellerId}", produces = MediaType.APPLICATION_JSON_VALUE)
  int countProductBySellerId(@PathVariable String sellerId);

}
