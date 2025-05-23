package com.tranxuanphong.userservice.mapper;

import com.tranxuanphong.userservice.dto.request.CreateAddressRequest;
import com.tranxuanphong.userservice.dto.response.AddressResponse;
import com.tranxuanphong.userservice.entity.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface AddressMapper {
    
    Address toAddress(CreateAddressRequest request);

    @Mapping(source = "id", target = "id")
    AddressResponse toAddressResponse(Address address);
}
