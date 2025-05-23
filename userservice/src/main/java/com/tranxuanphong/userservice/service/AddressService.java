package com.tranxuanphong.userservice.service;


import com.tranxuanphong.userservice.dto.request.CreateAddressRequest;
import com.tranxuanphong.userservice.dto.response.AddressResponse;
import com.tranxuanphong.userservice.entity.Address;
import com.tranxuanphong.userservice.entity.User;
import com.tranxuanphong.userservice.exception.ErrorCode;
import com.tranxuanphong.userservice.mapper.AddressMapper;
import com.tranxuanphong.userservice.repository.AddressRepository;
import com.tranxuanphong.userservice.repository.UserRepository;
import com.tranxuanphong.userservice.exception.AppException;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
@Slf4j
public class AddressService {
    AddressRepository addressRepository;
    AddressMapper addressMapper;
    UserRepository userRepository;

    public AddressResponse create(CreateAddressRequest request){
        String email = SecurityContextHolder.getContext().getAuthentication().getName(); 
        User user = userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.EMAIL_NOT_EXISTED)); 

        
        Address address = addressMapper.toAddress(request);
        address = addressRepository.save(address);

        user.setAddress(address);
        userRepository.save(user);

        return addressMapper.toAddressResponse(addressRepository.save(address));
    }


    public AddressResponse get(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName(); 
        User user = userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.EMAIL_NOT_EXISTED));  

        Address address = user.getAddress();

        return addressMapper.toAddressResponse(address);
    }
}
