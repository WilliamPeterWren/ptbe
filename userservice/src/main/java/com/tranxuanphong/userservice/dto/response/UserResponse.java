package com.tranxuanphong.userservice.dto.response;

import java.util.Map;
import java.util.Set;

import com.tranxuanphong.userservice.entity.Role;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    String id;
    String email;
    String username;
    Set<Role> roles;
    String addressId;
    String avatar;
    Map<String, Integer> peterVoucher;
    Map<String, Integer> shippingVoucher;
}
