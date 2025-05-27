package com.tranxuanphong.userservice.controller;

import lombok.AccessLevel; 
import lombok.RequiredArgsConstructor; 
import lombok.experimental.FieldDefaults; 
import org.springframework.web.bind.annotation.*;

import com.tranxuanphong.userservice.dto.request.RoleRequest;
import com.tranxuanphong.userservice.dto.response.ApiResponse;
import com.tranxuanphong.userservice.dto.response.RoleResponse;
import com.tranxuanphong.userservice.service.RoleService;

import java.util.List; 
 
@RestController 
@RequestMapping("/api/roles") 
@RequiredArgsConstructor 
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true) 
public class RoleController {
  RoleService roleService; 
 
    @PostMapping 
    ApiResponse<RoleResponse> create(@RequestBody RoleRequest request){ 
      System.out.println(request);
        return ApiResponse.<RoleResponse>builder() 
                .result(roleService.create(request)) 
                .code(1000) 
                .message("create success") 
                .build(); 
    } 
 
    @GetMapping 
    ApiResponse<List<RoleResponse>> getAll(){ 
        return ApiResponse.<List<RoleResponse>>builder() 
                .result(roleService.getAll()) 
                .code(1000) 
                .message("get all success") 
                .build(); 
    } 
 
    @DeleteMapping("/{roleName}") 
    ApiResponse<Void> delete(@PathVariable("roleName") String roleName){ 
        roleService.delete(roleName); 
        return ApiResponse.<Void>builder() 
                .message("delete success") 
                .code(200) 
                .build(); 
    }
}
