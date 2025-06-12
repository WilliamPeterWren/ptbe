package com.tranxuanphong.orderservice.service;

import com.tranxuanphong.orderservice.mapper.OrderMapper;
import com.tranxuanphong.orderservice.repository.httpclient.PeterClient;
import com.tranxuanphong.orderservice.repository.httpclient.ProductClient;
import com.tranxuanphong.orderservice.repository.httpclient.UserClient;
import com.tranxuanphong.orderservice.repository.mongo.OrderRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.tranxuanphong.orderservice.entity.Order;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;


@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
public class AdminOrderService {
    OrderRepository orderRepository;

  public void updateAllOrdersWithDefaultAvailable() {
    List<Order> allOrders = orderRepository.findAll();

    List<String> petervouchers = List.of("684010440414931d729faa42","6840106e0414931d729faa43", "6840107b0414931d729faa44", "684010860414931d729faa45", "6840108f0414931d729faa46");

    List<String> shippings = List.of("683b529e2a9cfc41ae6f134b","683b52b92a9cfc41ae6f134c", "683b52cc2a9cfc41ae6f134d", "683b52df2a9cfc41ae6f134e");

    List<String> shipvouchers = List.of("6840060e0414931d729faa3d", "684006180414931d729faa3e","684006250414931d729faa3f","6840062a0414931d729faa40", "6840063b0414931d729faa41");

    for (Order order : allOrders) {     

      int randomIndex1 = ThreadLocalRandom.current().nextInt(petervouchers.size());
      String randomvoucher = petervouchers.get(randomIndex1);

      int randomIndex2 = ThreadLocalRandom.current().nextInt(shippings.size());
      String randomshipping = shippings.get(randomIndex2);

      int randomIndex3 = ThreadLocalRandom.current().nextInt(shipvouchers.size());
      String randomshippingvoucher = shipvouchers.get(randomIndex3);

      order.setPeterVoucher(randomvoucher);
      order.setShippingId(randomshipping);
      order.setShippingVoucherId(randomshippingvoucher);

      orderRepository.save(order);
    }
  }

  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public List<Order> getAllByAdmin(){
    return orderRepository.findAll();
  }

  public void adminUpdate(){
    List<Order> list = orderRepository.findAll();
    for(Order order : list){
      // if(order.getAddressId() == null || order.getAddressId().length() < 5){
      //   order.setAddressId("6841c021aadbe05cf1f1a13b");
      //   orderRepository.save(order);
      // }

      order.setShipperId("");
      order.setRecieveImage("");
      orderRepository.save(order);
    }
  }
}
