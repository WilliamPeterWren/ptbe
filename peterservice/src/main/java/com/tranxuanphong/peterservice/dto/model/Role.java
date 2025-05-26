package com.tranxuanphong.peterservice.dto.model;

import java.util.Set;





public class Role {
  String name; 
  String description; 
  Set<Permission> permissionIds;
}
