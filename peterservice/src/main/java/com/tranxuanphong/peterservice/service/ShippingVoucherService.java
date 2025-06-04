package com.tranxuanphong.peterservice.service;

import com.tranxuanphong.peterservice.entity.ShippingVoucher;
import com.tranxuanphong.peterservice.exception.AppException;
import com.tranxuanphong.peterservice.exception.ErrorCode;
import com.tranxuanphong.peterservice.repository.mongo.ShippingVoucherRepository;
import com.tranxuanphong.peterservice.service.ShippingVoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ShippingVoucherService {

    @Autowired
    private ShippingVoucherRepository repository;

    public List<ShippingVoucher> getAll() {
        return repository.findAll();
    }

    public Optional<ShippingVoucher> getById(String id) {
        return repository.findById(id);
    }

    
    public ShippingVoucher getByIdd(String id) {
        return repository.findById(id).orElseThrow(() -> new AppException(ErrorCode.INVALID_KEY));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_STAFF')")
    public ShippingVoucher create(ShippingVoucher voucher) {
        return repository.save(voucher);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_STAFF')")
    public ShippingVoucher update(String id, ShippingVoucher voucher) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("ShippingVoucher not found");
        }
        voucher.setId(id);
        return repository.save(voucher);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_STAFF')")
    public void delete(String id) {
        repository.deleteById(id);
    }
}
