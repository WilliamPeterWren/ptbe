package com.tranxuanphong.peterservice.service;


import com.tranxuanphong.peterservice.entity.Shipping;
import com.tranxuanphong.peterservice.exception.AppException;
import com.tranxuanphong.peterservice.exception.ErrorCode;
import com.tranxuanphong.peterservice.repository.mongo.ShippingRepository;
import com.tranxuanphong.peterservice.utils.GenerateSlug;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ShippingService {

    ShippingRepository shippingRepository;
    GenerateSlug generateSlug;


    public List<Shipping> getAllShippings() {
        return shippingRepository.findByAvailableTrue();
    }

    public List<Shipping> adminGetAllShippings() {
        return shippingRepository.findAll();
    }

    public Optional<Shipping> getShippingById(String id) {
        return shippingRepository.findById(id);
    }

    public Shipping getShippingByIdd(String id) {
        return shippingRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.INVALID_KEY));
    }

    public Shipping createShipping(Shipping shipping) {
        
        String slug = generateSlug.generateSlug(shipping.getName());

        Shipping temp = Shipping.builder()
        .name(shipping.getName())
        .value(shipping.getValue())
        .slug(slug)
        .build();
        
        return shippingRepository.save(temp);
    }

    public Shipping updateShipping(String id, Shipping updatedShipping) {
        Shipping shipping = shippingRepository.findById(id).orElseThrow(()->new AppException(ErrorCode.SHIPPING_INVALID));
        System.out.println("herrrrrrrrrrrrrrrrrr");
        // log.info("id: " + id);
        boolean checkUpdate = false;
        if(updatedShipping.getName() !=null ){
            shipping.setName(updatedShipping.getName());
            String slug = generateSlug.generateSlug(shipping.getName());
            shipping.setSlug(slug);
            checkUpdate = true;
        }

        if(updatedShipping.getValue()!=null && updatedShipping.getValue() > 5000L){
            shipping.setValue(updatedShipping.getValue());
            checkUpdate = true;
        }
        System.out.println("avai: " + updatedShipping.getAvailable());
        if(updatedShipping.getAvailable() != null){
            
            shipping.setAvailable(updatedShipping.getAvailable());
            checkUpdate = true;
        }

        if(checkUpdate){
            shipping.setUpdatedAt(Instant.now());
        }
        
        return shippingRepository.save(shipping);
        
    }

    public void deleteShipping(String id) {
        Shipping shipping = shippingRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.SHIPPING_INVALID));

        shipping.setAvailable(false);
        shipping.setUpdatedAt(Instant.now());
        shippingRepository.save(shipping);
    }
}

