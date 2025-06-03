package com.tranxuanphong.fileservice.httpclient;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "productservice")
public interface ProductClient {
  @GetMapping(value = "/api/products/check/variant/id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  boolean doesVariantExistById(@PathVariable String id);

  @GetMapping(value = "/api/products/check/variant/id/{variantId}/seller/id/{sellerId}", produces = MediaType.APPLICATION_JSON_VALUE)
  boolean doesVariantExistBySellerId(@PathVariable String variantId, @PathVariable String sellerId);

  @GetMapping(value = "/api/products/get/product/images/id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  List<String> getProductImageMetadata(@PathVariable String id);

  @PostMapping(value = "/api/products/set/product/images/id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  void setProductImageMetadata(@PathVariable String id, @RequestBody List<String> fileNames);

}
