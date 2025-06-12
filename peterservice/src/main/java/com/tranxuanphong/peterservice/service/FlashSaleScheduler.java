package com.tranxuanphong.peterservice.service;

import java.time.Instant;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.tranxuanphong.peterservice.entity.FlashSale;
import com.tranxuanphong.peterservice.repository.mongo.FlashSaleRepository;

@Service
public class FlashSaleScheduler {

    @Autowired
    private FlashSaleRepository flashSaleRepository;

    @Scheduled(fixedRate = 60000)
    public void updateExpiredFlashSales() {
        Instant now = Instant.now();
        List<FlashSale> sales = flashSaleRepository.findByAvailableTrueAndExpiredAtBefore(now);

        for (FlashSale sale : sales) {
            sale.setAvailable(false);
        }

        if (!sales.isEmpty()) {
            flashSaleRepository.saveAll(sales);
        }
    }
}

