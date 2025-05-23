package com.tranxuanphong.userservice.dto.request;

import java.util.Set;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class RoleRequest {

  @NotBlank(message = "Name is required")
  @Size(min = 2, max = 32, message = "ROLE_NAME_INVALID") 
  String name; 

  @NotBlank(message = "Description is required")
  @Size(min = 2, max = 32, message = "ROLE_DESCRIPTION_INVALID") 
  String description; 

  @NotNull(message = "Permission IDs cannot be null")
  @NotEmpty(message = "At least one permission ID is required")
  @Size(max = 50, message = "Too many permissions, maximum allowed is 50")
  @Valid
  Set<@NotBlank(message = "Permission ID cannot be blank") 
      @Size(min = 2, max = 32, message = "PERMISSION_NAME_INVALID") String> permissionIds;
}
