package com.tranxuanphong.userservice.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tranxuanphong.userservice.dto.request.PermissionRequest;
import com.tranxuanphong.userservice.dto.response.ApiResponse;
import com.tranxuanphong.userservice.dto.response.PermissionResponse;
import com.tranxuanphong.userservice.service.PermissionService;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;

@RestController 
@RequiredArgsConstructor 
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true) 
@RequestMapping("/api/permissions") 
public class PermissionController {
  PermissionService permissionService;
  
  @PostMapping 
  ApiResponse<PermissionResponse> create(@RequestBody PermissionRequest request){ 
    return ApiResponse.<PermissionResponse>builder() 
      .result(permissionService.create(request)) 
      .code(1000) 
      .message("create permission success") 
      .build(); 
  } 
 
  @GetMapping 
  ApiResponse<List<PermissionResponse>> getAll(){ 
    return ApiResponse.<List<PermissionResponse>>builder() 
      .result(permissionService.getAll()) 
      .code(1000) 
      .message("get all permission success") 
      .build(); 
  } 
 
  @DeleteMapping("/{permissionName}") 
  ApiResponse<Void> delete(@PathVariable("permissionName") String permissionName){ 
    permissionService.delete(permissionName); 
    return ApiResponse.<Void>builder() 
      .code(1000) 
      .message("delete permission success") 
      .build(); 
  }
}
