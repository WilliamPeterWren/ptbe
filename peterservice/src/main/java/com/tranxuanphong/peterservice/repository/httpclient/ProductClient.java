package com.tranxuanphong.peterservice.repository.httpclient;


import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.tranxuanphong.peterservice.config.FeignConfig;
import com.tranxuanphong.peterservice.dto.response.FlashSaleProductResponse;
import com.tranxuanphong.peterservice.dto.response.ProductResponse;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "productservice", configuration = FeignConfig.class)
public interface ProductClient {
  @GetMapping(value = "/api/products/check/product/id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  boolean existsByProductId(@PathVariable String id);

  @GetMapping(value = "/api/products/check/product/{productId}/seller/{sellerId}", produces = MediaType.APPLICATION_JSON_VALUE)
  boolean doesProductExistBySellerId(@PathVariable String productId, @PathVariable String sellerId);

  @GetMapping(value = "/api/products/get/products/by/ids", produces = MediaType.APPLICATION_JSON_VALUE)
  List<FlashSaleProductResponse> getListProductByIds(@RequestBody List<String> ids);

  @GetMapping(value = "/api/products/get/seller/id/product/id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public String getSellerIdByProductId(@PathVariable String id);

  @GetMapping(value = "/api/products/product/id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ProductResponse getProductResponseByProductId(@PathVariable String id);

}
