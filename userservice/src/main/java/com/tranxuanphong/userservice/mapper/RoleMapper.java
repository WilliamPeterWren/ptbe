package com.tranxuanphong.userservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.tranxuanphong.userservice.dto.request.RoleRequest;
import com.tranxuanphong.userservice.dto.response.RoleResponse;
import com.tranxuanphong.userservice.entity.Role;

@Mapper(componentModel = "spring")
public interface RoleMapper {
  @Mapping(target = "permissionIds", ignore = true) 
  Role toRole(RoleRequest request); 
  RoleResponse toRoleResponse(Role role);
}
