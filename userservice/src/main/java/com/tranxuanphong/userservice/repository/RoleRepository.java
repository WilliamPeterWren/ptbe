package com.tranxuanphong.userservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.tranxuanphong.userservice.entity.Role;

public interface RoleRepository extends MongoRepository<Role, String>{
  
}
