package com.tranxuanphong.userservice.mapper;

import org.mapstruct.Mapper;

import com.tranxuanphong.userservice.dto.request.RoleRequest;
import com.tranxuanphong.userservice.dto.response.RoleResponse;
import com.tranxuanphong.userservice.entity.Role;

@Mapper(componentModel = "spring")
public interface RoleMapper {
  Role toRole(RoleRequest request); 
  RoleResponse toRoleResponse(Role role);
}
