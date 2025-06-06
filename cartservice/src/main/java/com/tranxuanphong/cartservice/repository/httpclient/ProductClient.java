package com.tranxuanphong.cartservice.repository.httpclient;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;

import com.tranxuanphong.cartservice.dto.model.ItemResponse;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "productservice")
public interface ProductClient {
  @GetMapping(value = "/api/products/check/variant/id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  boolean doesVariantExistById(@PathVariable String id);


  // @GetMapping(value = " /api/products/get/cartproduct/variant/id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  // CartProductResponse getProductByVariantId(@PathVariable String id);

  @GetMapping(value = "/api/products/get/cartproduct/variant/id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  ItemResponse getProductByVariantId(@PathVariable String id);

}
