package com.tranxuanphong.userservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;

@Data 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder 
@FieldDefaults(level = AccessLevel.PRIVATE) 
public class PermissionResponse {
  String name; 
  String description;
}
