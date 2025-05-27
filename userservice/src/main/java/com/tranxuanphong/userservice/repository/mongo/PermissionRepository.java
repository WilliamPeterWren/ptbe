package com.tranxuanphong.userservice.repository.mongo;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.tranxuanphong.userservice.entity.Permission;

@Repository
public interface PermissionRepository extends MongoRepository<Permission, String> {
}
