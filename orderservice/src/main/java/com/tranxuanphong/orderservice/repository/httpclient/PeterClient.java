package com.tranxuanphong.orderservice.repository.httpclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;

import com.tranxuanphong.orderservice.model.PeterVoucher;
import com.tranxuanphong.orderservice.model.Shipping;
import com.tranxuanphong.orderservice.model.ShippingVoucher;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
@FeignClient(name = "peterservice")
public interface PeterClient {
  @GetMapping(value = "/api/shippings/id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  Shipping getShippingById(@PathVariable String id);

  @GetMapping(value = "/api/vouchers/id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  PeterVoucher getPeterVoucherById(@PathVariable String id);

  @GetMapping(value = "/api/shipping-vouchers/id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  ShippingVoucher getShippingVoucherById(@PathVariable String id);



}
