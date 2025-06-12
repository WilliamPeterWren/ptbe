package com.tranxuanphong.userservice.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.tranxuanphong.userservice.dto.request.RegisterRequest;
import com.tranxuanphong.userservice.dto.request.UserUpdateRequest;
import com.tranxuanphong.userservice.dto.response.UserResponse;
import com.tranxuanphong.userservice.entity.User; 
 
@Mapper(componentModel = "spring") 
public interface UserMapper { 
  User toUser(RegisterRequest request); 
  void updateUser(@MappingTarget User user, UserUpdateRequest request); 
  UserResponse toUserResponse(User user); 
  List<UserResponse> toListUserResponse(List<User> listUser);
}
