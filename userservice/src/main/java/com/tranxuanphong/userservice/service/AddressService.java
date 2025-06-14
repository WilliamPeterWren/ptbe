package com.tranxuanphong.userservice.service;


import com.tranxuanphong.userservice.dto.request.AddressUpdateRequest;
import com.tranxuanphong.userservice.dto.request.AddressCreateRequest;
import com.tranxuanphong.userservice.dto.response.AddressResponse;
import com.tranxuanphong.userservice.entity.Address;
import com.tranxuanphong.userservice.entity.User;
import com.tranxuanphong.userservice.exception.ErrorCode;
import com.tranxuanphong.userservice.mapper.AddressMapper;
import com.tranxuanphong.userservice.repository.httpclient.OrderClient;
import com.tranxuanphong.userservice.repository.mongo.AddressRepository;
import com.tranxuanphong.userservice.repository.mongo.UserRepository;
import com.tranxuanphong.userservice.exception.AppException;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.access.prepost.PreAuthorize;
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

    OrderClient orderClient;

    
    public AddressResponse create(AddressCreateRequest request){
        String email = SecurityContextHolder.getContext().getAuthentication().getName(); 
        User user = userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.EMAIL_NOT_EXISTED)); 

        if(user.getAddressId() == null){
            Address address = addressMapper.toAddress(request);
            address = addressRepository.save(address);
    
            user.setAddressId(address.getId());
            userRepository.save(user);
    
            return addressMapper.toAddressResponse(addressRepository.save(address));
        }

        if(orderClient.existsByAddressId(user.getAddressId())){
            Address address = addressMapper.toAddress(request);            
            user.setAddressId(address.getId());
            userRepository.save(user);
            return addressMapper.toAddressResponse(addressRepository.save(address));
        }

      
        Address address = addressMapper.toAddress(request);
        return addressMapper.toAddressResponse(addressRepository.save(address));   
    }


    public AddressResponse get(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName(); 
        User user = userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.EMAIL_NOT_EXISTED));  
        String addressId = user.getAddressId();
        Address address = addressRepository.findById(addressId).orElseThrow(()-> new AppException(ErrorCode.ADDRESS_INVALID));
        return addressMapper.toAddressResponse(address);
    }

    public AddressResponse update(String addressId, AddressUpdateRequest request){
        String email = SecurityContextHolder.getContext().getAuthentication().getName(); 
       
        User user = userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.EMAIL_NOT_EXISTED));  
        System.out.println("user name "+user.getUsername() );
        if(!user.getAddressId().equals(addressId)){
            throw new AppException(ErrorCode.USER_ADDRESS_NOT_MATCH);
        }
        System.out.println("check.... + " + addressId );
        if(orderClient.existsByAddressId(addressId)){
            System.out.println("exist in order...");

            Address address = addressMapper.toAddress(request);
            address = addressRepository.save(address);

            user.setAddressId(address.getId());
            userRepository.save(user);

            return addressMapper.toAddressResponse(address);
        }

        System.out.println("not exit in order...");
        Address address = addressRepository.findById(addressId).orElseThrow(()-> new AppException(ErrorCode.ADDRESS_INVALID));
        addressMapper.updateAddress(address, request);

        return addressMapper.toAddressResponse(addressRepository.save(address));
    }

    @PreAuthorize("hasAnyRole('ROLE_SELLER','ROLE_ADMIN','ROLE_STAFF','ROLE_SHIPPER')")
    public String address(String addressId){
        Address address = addressRepository.findById(addressId).orElseThrow(() -> new AppException(ErrorCode.ADDRESS_INVALID));

        return address.getAddress();
    }


    
}
