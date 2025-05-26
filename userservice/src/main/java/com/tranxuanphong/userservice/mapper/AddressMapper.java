package com.tranxuanphong.userservice.mapper;

import com.tranxuanphong.userservice.dto.request.AddressUpdateRequest;
import com.tranxuanphong.userservice.dto.request.AddressCreateRequest;
import com.tranxuanphong.userservice.dto.request.UserUpdatePasswordRequest;
import com.tranxuanphong.userservice.dto.response.AddressResponse;
import com.tranxuanphong.userservice.entity.Address;
import com.tranxuanphong.userservice.entity.User;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;


@Mapper(componentModel = "spring")
public interface AddressMapper {
    
    Address toAddress(AddressCreateRequest request);
    Address toAddress(AddressUpdateRequest request);

    @Mapping(source = "id", target = "id")
    AddressResponse toAddressResponse(Address address);
    void updateAddress(@MappingTarget Address address, AddressUpdateRequest request); 
    void updateAddress(@MappingTarget Address address, AddressCreateRequest request); 

}
