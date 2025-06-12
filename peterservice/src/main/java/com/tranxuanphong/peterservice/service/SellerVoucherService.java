package com.tranxuanphong.peterservice.service;


import com.tranxuanphong.peterservice.entity.SellerVoucher;
import com.tranxuanphong.peterservice.repository.mongo.SellerVoucherRepository;
import com.tranxuanphong.peterservice.service.SellerVoucherService;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SellerVoucherService {

    SellerVoucherRepository sellerVoucherRepository;

    public List<SellerVoucher> getAll() {
        return sellerVoucherRepository.findAll();
    }

    public Optional<SellerVoucher> getById(String id) {
        return sellerVoucherRepository.findById(id);
    }

    public List<SellerVoucher> getBySellerId(String sellerId) {
        return sellerVoucherRepository.findBySellerId(sellerId);
    }

    @PreAuthorize("hasRole('ROLE_SELLER')")
    public SellerVoucher create(SellerVoucher voucher) {
        return sellerVoucherRepository.save(voucher);
    }

    @PreAuthorize("hasRole('ROLE_SELLER')")
    public SellerVoucher update(String id, SellerVoucher voucher) {
        if (!sellerVoucherRepository.existsById(id)) {
            throw new RuntimeException("SellerVoucher not found");
        }
        voucher.setId(id);
        return sellerVoucherRepository.save(voucher);
    }

    @PreAuthorize("hasRole('ROLE_SELLER')")
    public void delete(String id) {
        sellerVoucherRepository.deleteById(id);
    }
}

