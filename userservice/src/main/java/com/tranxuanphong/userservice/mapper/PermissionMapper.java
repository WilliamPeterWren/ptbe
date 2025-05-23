package com.tranxuanphong.userservice.mapper;

import org.mapstruct.Mapper;

import com.tranxuanphong.userservice.dto.request.PermissionRequest;
import com.tranxuanphong.userservice.dto.response.PermissionResponse;
import com.tranxuanphong.userservice.entity.Permission;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
  Permission toPermission(PermissionRequest request);
  PermissionResponse toPermissionResponse(Permission permission);
}
