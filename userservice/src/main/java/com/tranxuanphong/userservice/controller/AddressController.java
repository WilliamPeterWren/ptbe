package com.tranxuanphong.userservice.controller;

import com.tranxuanphong.userservice.dto.request.ApiResponse;
import com.tranxuanphong.userservice.dto.request.AddressCreateRequest;
import com.tranxuanphong.userservice.dto.request.AddressUpdateRequest;
import com.tranxuanphong.userservice.dto.response.AddressResponse;
import com.tranxuanphong.userservice.service.AddressService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RequestMapping("/api/addresses")
public class AddressController {
    AddressService addressService;

   @PostMapping
   public ApiResponse<AddressResponse> create(@RequestBody @Valid AddressCreateRequest request) {
       return ApiResponse.<AddressResponse>builder()
               .result(addressService.create(request))
               .build();
   }

   @GetMapping
   public ApiResponse<AddressResponse> getAddress() {
       return ApiResponse.<AddressResponse>builder()
       .result(addressService.get())
       .build();
   }

   @PutMapping("/{id}")
   public ApiResponse<AddressResponse> update(@PathVariable String id, @RequestBody AddressUpdateRequest request) {
       return ApiResponse.<AddressResponse>builder()
       .result(addressService.update(id, request))
       .build();
   }

}
