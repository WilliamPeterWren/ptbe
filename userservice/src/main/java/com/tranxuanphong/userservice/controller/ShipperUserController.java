package com.tranxuanphong.userservice.controller;

import org.springframework.web.bind.annotation.RestController;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;



import com.tranxuanphong.userservice.service.ShipperUserService;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RequestMapping("/api/users/shipper")
public class ShipperUserController {
  ShipperUserService shipperUserService;
  
  @GetMapping("/get/phone/id/{id}")
  public String getPhoneById(@PathVariable String id) {
      return shipperUserService.getPhoneById(id);
  }

}
