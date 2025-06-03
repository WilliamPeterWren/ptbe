package com.tranxuanphong.peterservice.service;


import com.tranxuanphong.peterservice.entity.Shipping;
import com.tranxuanphong.peterservice.repository.mongo.ShippingRepository;
import com.tranxuanphong.peterservice.utils.GenerateSlug;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ShippingService {

    private final ShippingRepository shippingRepository;
    private final GenerateSlug generateSlug;



    public List<Shipping> getAllShippings() {
        return shippingRepository.findAll();
    }

    public Optional<Shipping> getShippingById(String id) {
        return shippingRepository.findById(id);
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

    public Optional<Shipping> updateShipping(String id, Shipping updatedShipping) {
        return shippingRepository.findById(id).map(existing -> {
            existing.setName(updatedShipping.getName());
            existing.setValue(updatedShipping.getValue());
            existing.setSlug(updatedShipping.getSlug());
            existing.setAvailable(updatedShipping.isAvailable());
            existing.setUpdatedAt(java.time.Instant.now());
            return shippingRepository.save(existing);
        });
    }

    public void deleteShipping(String id) {
        shippingRepository.deleteById(id);
    }
}

