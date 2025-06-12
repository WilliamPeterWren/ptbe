package com.tranxuanphong.userservice.controller;

import org.springframework.web.bind.annotation.RestController;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tranxuanphong.userservice.dto.response.ApiResponse;
import com.tranxuanphong.userservice.dto.response.UserResponse;
import com.tranxuanphong.userservice.service.AdminUserService;


@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RequestMapping("/api/users/admin")
public class AdminUserController {
  AdminUserService adminUserService;
  @GetMapping("/admin/update/all")
  public void adminUpdateAllUser(){
    adminUserService.adminUpdateAllUser();
  }

  @GetMapping("/get-all")
  public ApiResponse<List<UserResponse>> getAll(){
    return ApiResponse.<List<UserResponse>>builder()
    .result(adminUserService.getAll())
    .build();
  }

  @PutMapping("/update-role/{roleId}")
  public ApiResponse<UserResponse> updateRole(@PathVariable String roleId) {
    return ApiResponse.<UserResponse>builder()
    .result(adminUserService.updateRole(roleId))
    .build();
  }

  @PutMapping("/update/shippingvoucher/id/{shippingvoucherid}/count/{count}")
  public void updateShippingVoucher(@PathVariable String shippingvoucherid, @PathVariable int count) {
    adminUserService.updateShippingVoucher(shippingvoucherid, count);  
  }

  @PutMapping("/update/petervoucher/id/{petervoucherid}/count/{count}")
  public void updatePeterVoucher(@PathVariable String petervoucherid, @PathVariable int count) {
    adminUserService.updateShippingVoucher(petervoucherid, count);  
  }

}
