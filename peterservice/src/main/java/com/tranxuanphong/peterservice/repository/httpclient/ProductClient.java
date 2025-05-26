package com.tranxuanphong.peterservice.repository.httpclient;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;

import com.tranxuanphong.peterservice.config.FeignConfig;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "productservice", configuration = FeignConfig.class)
public interface ProductClient {
  @GetMapping(value = "/api/products/check/product/id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  boolean existsByProductId(@PathVariable String id);

  @GetMapping(value ="/api/products/product/id/{productId}/seller/id/{sellerId}", produces = MediaType.APPLICATION_JSON_VALUE)
  boolean productBySeller(@PathVariable String productId, @PathVariable String sellerId);
}
