package com.tranxuanphong.orderservice.repository.httpclient;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.tranxuanphong.orderservice.dto.request.ReviewCheckRequest;
import com.tranxuanphong.orderservice.dto.response.ProductResponse;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "productservice")
public interface ProductClient {
  @GetMapping(value = "/api/products/check/variant/id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  boolean doesVariantExistById(@PathVariable String id);

  @GetMapping(value = "/api/products/check/variant/id/{variantId}/seller/id/{sellerId}", produces = MediaType.APPLICATION_JSON_VALUE)
  boolean doesVariantExistBySellerId(@PathVariable String variantId, @PathVariable String sellerId);

 
  @GetMapping(value = "/api/products/get/product/images/id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  List<String> getProductImageMetadata(@PathVariable String variantId, @PathVariable String sellerId);

  @GetMapping(value = "/api/products/get/cartproduct/variant/id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  ProductResponse getProductByVariantId(@PathVariable String id);

  @PutMapping(value = "/api/products/shipper/id/{id}/sold/{sold}", produces = MediaType.APPLICATION_JSON_VALUE)
  ProductResponse updateProductSold(@PathVariable String id, @PathVariable Long sold, @RequestHeader("Authorization") String authorizationHeader);

  @PostMapping("/api/reviews/check/reviews/user/product/variant")
  public boolean checkReviewByUserProductVariant(@RequestBody ReviewCheckRequest request);
}
