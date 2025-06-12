package com.tranxuanphong.userservice.service;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.tranxuanphong.userservice.entity.Address;
import com.tranxuanphong.userservice.entity.User;
import com.tranxuanphong.userservice.exception.AppException;
import com.tranxuanphong.userservice.exception.ErrorCode;
import com.tranxuanphong.userservice.repository.mongo.AddressRepository;
import com.tranxuanphong.userservice.repository.mongo.UserRepository;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor

public class ShipperUserService {
  UserRepository userRepository;
  AddressRepository addressRepository;

  @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_STAFF','ROLE_SHIPPER')")
  public String getPhoneById (String id){
    User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));
    
    Address address = addressRepository.findById(user.getAddressId()).orElseThrow(() -> new AppException(ErrorCode.ADDRESS_INVALID));
    return address.getPhone();
  }
}
