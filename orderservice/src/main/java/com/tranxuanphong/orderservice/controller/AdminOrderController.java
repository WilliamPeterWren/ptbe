package com.tranxuanphong.orderservice.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tranxuanphong.orderservice.entity.Order;
import com.tranxuanphong.orderservice.service.AdminOrderService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PutMapping;


@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RequestMapping("/api/orders/admin")
public class AdminOrderController {
  AdminOrderService adminOrderService;
  
    // @PutMapping("/admin/update")
  // public void putMethodName() {
  //     adminOrderService.updateAllOrdersWithDefaultAvailable();    
  // }

  @GetMapping("/getall")
  public List<Order> getOrderAdmin() {
    return adminOrderService.getAllByAdmin();    
  }

  @PostMapping("/update")
  public void adminUpdate() {
    adminOrderService.adminUpdate();
  }
}
