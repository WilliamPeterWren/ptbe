package com.tranxuanphong.userservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PermissionRequest {

  @NotBlank(message = "Permission is required")
  @Size(min = 3, max = 32, message = "PERMISSION_NAME_INVALID")
  String name; 

  @NotBlank(message = "Description is required")
  @Size(min = 3, max = 32, message = "PERMISSION_DESCRIPTION_INVALID")
  String description; 
}
