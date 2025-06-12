package com.tranxuanphong.productservice.repository.httpclient;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "peterservice")
public interface PeterClient {
  @GetMapping(value = "/api/petercategories/exists-by-id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  boolean existsInPeterCategoryId(@PathVariable String id);

  @GetMapping(value = "/api/flashsales/get/discount/flashsale/id/{flashsaleid}/product/id/{productid}", produces = MediaType.APPLICATION_JSON_VALUE)
  Long getDiscountByFlashSaleIdAndProductId(@PathVariable String flashsaleid, @PathVariable String productid);

  @GetMapping(value = "/api/flashsales/get/flashsale/id/latest", produces = MediaType.APPLICATION_JSON_VALUE)
  String getLastestFlashSalesId();

}
