package com.tranxuanphong.peterservice.repository.httpclient;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "userservice")
public interface UserClient {
  @GetMapping(value = "/api/users/check/id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  boolean existsId(@PathVariable String id); 

  @GetMapping(value = "/api/users/get/userid/email/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
  String userId(@PathVariable String email);

  @PutMapping("/api/users/admin/update/shippingvoucher/id/{shippingvoucherid}/count/{count}")
  void updateShippingVoucher(@PathVariable String shippingvoucherid, @PathVariable int count, @RequestHeader("Authorization") String authorizationHeader);

  @PutMapping("/api/users/admin/update/petervoucher/id/{petervoucherid}/count/{count}")
  void updatePeterVoucher(@PathVariable String petervoucherid, @PathVariable int count, @RequestHeader("Authorization") String authorizationHeader);

}
