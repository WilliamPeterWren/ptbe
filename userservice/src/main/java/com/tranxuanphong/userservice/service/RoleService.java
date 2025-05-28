package com.tranxuanphong.userservice.service;

import java.util.HashSet;
import java.util.List;

import org.springframework.stereotype.Service;

import com.tranxuanphong.userservice.dto.request.RoleRequest;
import com.tranxuanphong.userservice.dto.response.RoleResponse;
import com.tranxuanphong.userservice.entity.Role;
import com.tranxuanphong.userservice.mapper.RoleMapper;
import com.tranxuanphong.userservice.repository.mongo.RoleRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleService {
  RoleRepository roleRepository;
  RoleMapper roleMapper;

  
   public RoleResponse create(RoleRequest request){ 
        Role role = roleMapper.toRole(request); 
        return roleMapper.toRoleResponse(roleRepository.save(role)); 
    } 
 
    public List<RoleResponse> getAll(){ 
        return roleRepository.findAll() 
                .stream() 
                .map(roleMapper::toRoleResponse) 
                .toList(); 
    } 
 
    public void delete(String roleName){ 
        roleRepository.deleteById(roleName); 
    }
}
