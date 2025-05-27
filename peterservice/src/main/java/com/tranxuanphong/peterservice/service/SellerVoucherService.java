package com.tranxuanphong.peterservice.service;


import com.tranxuanphong.peterservice.entity.SellerVoucher;
import com.tranxuanphong.peterservice.repository.mongo.SellerVoucherRepository;
import com.tranxuanphong.peterservice.service.SellerVoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SellerVoucherService {

    @Autowired
    private SellerVoucherRepository repository;

    public List<SellerVoucher> getAll() {
        return repository.findAll();
    }

    public Optional<SellerVoucher> getById(String id) {
        return repository.findById(id);
    }

    public List<SellerVoucher> getBySellerId(String sellerId) {
        return repository.findBySellerId(sellerId);
    }

    @PreAuthorize("hasRole('ROLE_SELLER')")
    public SellerVoucher create(SellerVoucher voucher) {
        return repository.save(voucher);
    }

    @PreAuthorize("hasRole('ROLE_SELLER')")
    public SellerVoucher update(String id, SellerVoucher voucher) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("SellerVoucher not found");
        }
        voucher.setId(id);
        return repository.save(voucher);
    }

    @PreAuthorize("hasRole('ROLE_SELLER')")
    public void delete(String id) {
        repository.deleteById(id);
    }
}

