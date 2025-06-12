package com.tranxuanphong.cartservice.repository.httpclient;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;


import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "peterservice")
public interface PeterClient {
  @GetMapping(value = "/api/products/check/variant/id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  boolean doesVariantExistById(@PathVariable String id);

  @GetMapping(value = "/api/flashsales/get/discount/flashsale/id/{flashsaleid}/product/id/{productid}", produces = MediaType.APPLICATION_JSON_VALUE)
  Long getDiscountByFlashSaleIdAndProductId(@PathVariable String flashsaleid, @PathVariable String productid);

  @GetMapping(value = "/api/flashsales/get/flashsale/id/latest", produces = MediaType.APPLICATION_JSON_VALUE)
  public String getLastestFlashSalesId();

}
