package com.tranxuanphong.userservice.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tranxuanphong.userservice.dto.request.PermissionRequest;
import com.tranxuanphong.userservice.dto.response.PermissionResponse;
import com.tranxuanphong.userservice.entity.Permission;
import com.tranxuanphong.userservice.mapper.PermissionMapper;
import com.tranxuanphong.userservice.repository.mongo.PermissionRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionService {
  PermissionRepository permissionRepository;
  PermissionMapper permissionMapper;

  public PermissionResponse create(PermissionRequest request){ 
    Permission permission = permissionMapper.toPermission(request); 
    return permissionMapper.toPermissionResponse(permissionRepository.save(permission)); 
  } 

  public List<PermissionResponse> getAll(){ 
    var permissions = permissionRepository.findAll(); 
    return permissions.stream().map(permissionMapper::toPermissionResponse).toList(); 
  } 

  public void delete(String permissionName){ 
    permissionRepository.deleteById(permissionName); 
  }
}
