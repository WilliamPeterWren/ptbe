package com.tranxuanphong.orderservice.repository.httpclient;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "userservice")
public interface UserClient {
  @GetMapping(value = "/api/users/check/id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  boolean existsId(@PathVariable String id);
  
  @GetMapping(value = "/api/users/get/userid/email/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
  String userId(@PathVariable String email);

  @GetMapping(value = "/api/users/get/username/email/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
  String usernameByEmail(@PathVariable String email);

  @GetMapping(value = "/api/users/get/username/id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  String usernameByUserId(@PathVariable String id);

  @GetMapping(value =  "/api/addresses/get/seller/order/address/id/{id}")
  String sellerGetAddress(@PathVariable String id, @RequestHeader("Authorization") String authorizationHeader);

  @GetMapping(value = "/api/users/shipper/get/phone/id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  String getPhoneById(@PathVariable String id, @RequestHeader("Authorization") String authorizationHeader);

  @PostMapping("/api/users/update/petervoucher/id/{petervoucherid}/user/id/{userid}")
  void updateUserPeterVoucher(@PathVariable String petervoucherid, @PathVariable String userid);

}
