package com.tranxuanphong.orderservice.service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.tranxuanphong.orderservice.dto.request.OrderCreateRequest;
import com.tranxuanphong.orderservice.dto.request.OrderUpdateRequest;
import com.tranxuanphong.orderservice.dto.response.OrderResponse;
import com.tranxuanphong.orderservice.dto.response.OrderResponseFE;
import com.tranxuanphong.orderservice.dto.response.ProductResponse;
import com.tranxuanphong.orderservice.entity.Order;
import com.tranxuanphong.orderservice.entity.OrderItem;
import com.tranxuanphong.orderservice.entity.Status;
import com.tranxuanphong.orderservice.enums.OrderStatus;
import com.tranxuanphong.orderservice.exception.AppException;
import com.tranxuanphong.orderservice.exception.ErrorCode;
import com.tranxuanphong.orderservice.mapper.OrderMapper;
import com.tranxuanphong.orderservice.model.ItemResponse;
import com.tranxuanphong.orderservice.model.PeterVoucher;
import com.tranxuanphong.orderservice.model.Shipping;
import com.tranxuanphong.orderservice.model.ShippingVoucher;
import com.tranxuanphong.orderservice.repository.httpclient.UserClient;
import com.tranxuanphong.orderservice.repository.mongo.OrderRepository;
import com.tranxuanphong.orderservice.repository.mongo.custom.impl.CustomOrderRepositoryImpl;
import com.tranxuanphong.orderservice.repository.httpclient.PeterClient;
import com.tranxuanphong.orderservice.repository.httpclient.ProductClient;
import org.springframework.security.core.Authentication;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;


@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
public class OrderService {
  OrderRepository orderRepository;

  UserClient userClient;
  ProductClient productClient;
  PeterClient peterClient;

  OrderMapper orderMapper;

  @PreAuthorize("hasRole('ROLE_USER')")
  public OrderResponse create(OrderCreateRequest request){
    String customerEmail = SecurityContextHolder.getContext().getAuthentication().getName(); 
    String customerId = userClient.userId(customerEmail);
    if(!userClient.existsId(request.getSellerId())){
      throw new AppException(ErrorCode.SELLERID_INVALID);
    }

    if(customerId.equals(request.getSellerId())){
      throw new AppException(ErrorCode.CUSTOMER_SELLER_CONFLIC);
    }

    Set<OrderItem> orderItems = request.getOrderItems();
    for(OrderItem orderItem : orderItems){
      if(!productClient.doesVariantExistBySellerId(orderItem.getVariantId(), request.getSellerId())){
        throw new AppException(ErrorCode.VARIANT_NOT_EXISTS);
      }
    }


    Set<Status> setStatus = new HashSet<>();

    Status status = Status.builder()
    .status(OrderStatus.PENDING)
    .build();

    setStatus.add(status);

    Order order = orderMapper.toOrder(request);

    order.setOrderStatus(setStatus);
    order.setUserId(customerId);

    return orderMapper.toOrderResponse(orderRepository.save(order));
  }

  public boolean doesAddressExist(String addressId){
    return orderRepository.existsByAddressId(addressId);
  }

  @PreAuthorize("hasAnyRole('ROLE_SHIPPER','ROLE_SELLER')")
  public OrderResponse update(String orderId, OrderUpdateRequest request){
    // String shipperEmail = SecurityContextHolder.getContext().getAuthentication().getName(); 
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String shipperEmail = authentication.getName();
    String shipperId = userClient.userId(shipperEmail);

    Order order = orderRepository.findById(orderId).orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));


    Set<Status> status = order.getOrderStatus();
    Status updateStatus = Status.builder()
    .shipperId(shipperId)
    .status(request.getOrderStatus())
    .build();

    for(Status stt : status){
      if(stt.getStatus().equals(request.getOrderStatus())){
        throw new AppException(ErrorCode.ORDER_STATUS_INVALID);
      }
    }

    status.add(updateStatus);

    String authToken = null;
    if (authentication != null && authentication.getCredentials() instanceof String) {
      authToken = (String) authentication.getCredentials();
    } else if (authentication != null && authentication.getPrincipal() instanceof org.springframework.security.oauth2.jwt.Jwt) {
      authToken = ((org.springframework.security.oauth2.jwt.Jwt) authentication.getPrincipal()).getTokenValue();
    }

    if (authToken == null) {
        throw new AppException(ErrorCode.UNAUTHENTICATED);
    }
    String authorizationHeader = "Bearer " + authToken;

    //update product.sold if request.getOrderStatus() == DELIVERD only  shipper
    System.out.println("start....");
    if(request.getOrderStatus().equals(OrderStatus.DELIVERD)){
      Set<OrderItem> orderItems = order.getOrderItems();
      for(OrderItem orderItem : orderItems){        
        ProductResponse productResponse = productClient.getProductByVariantId(orderItem.getVariantId());
        System.out.println("product id: " + productResponse.getProductId() + " quanttiY: " + orderItem.getQuantity());
        productClient.updateProductSold(productResponse.getProductId(),orderItem.getQuantity(), authorizationHeader);
      }
    }
    System.out.println("end....");
    order.setOrderStatus(status);
    order.setUpdatedAt(Instant.now());

    return orderMapper.toOrderResponse(orderRepository.save(order));
  }

  @PreAuthorize("hasRole('ROLE_USER')")
  public Page<OrderResponseFE> getAllOrderByUser(int page, int size) {
    String customerEmail = SecurityContextHolder.getContext().getAuthentication().getName(); 
    String customerId = userClient.userId(customerEmail);


    Page<Order> listOrders = orderRepository.findByUserIdOrderByCreatedAtDesc(customerId, PageRequest.of(page, size));

    Page<OrderResponseFE> pageOrderResponseFE = listOrders.map(order -> {
      Set<ItemResponse> items = order.getOrderItems().stream().map(item -> {
        ProductResponse productResponse =  productClient.getProductByVariantId(item.getVariantId());
        
        return ItemResponse.builder()
          .salePrice(item.getSalePrice())
          .discount(item.getDiscount())
          .price(item.getPrice())
          .variantId(item.getVariantId())
          .quantity(item.getQuantity())
          .productId(productResponse.getProductId())
          .variantName(productResponse.getVariantName())     
          .productName(productResponse.getProductName())    
          .image(productResponse.getImage()) 
          .build();
      }).collect(Collectors.toSet());
      String sellerUsername = userClient.usernameByUserId(order.getSellerId());


      Long shippingPrice = 0L;
      try{
        Shipping shipping = peterClient.getShippingById(order.getShippingId());
        shippingPrice = shipping.getValue();
      }catch(Exception e){
        System.out.println("shippping error: " + e.getMessage());
      }

      Long peterVoucherPrice = 0L;
      try {
        PeterVoucher peterVoucher = peterClient.getPeterVoucherById(order.getPeterVoucher());
        peterVoucherPrice = peterVoucher.getValue();
      } catch (Exception e) {
        System.out.println("peter error: " + e.getMessage());

      }

      Long shippingVoucherPrice = 0L;
      try {
        ShippingVoucher shippingVoucher = peterClient.getShippingVoucherById(order.getShippingVoucherId());
        shippingVoucherPrice = shippingVoucher.getPrice();
      } catch (Exception e) {
        System.out.println("shipping voucher error: " + e.getMessage());
      }
      

      return OrderResponseFE.builder()
        .id(order.getId())
        .userId(order.getUserId())
        .sellerId(order.getSellerId())
        .sellerUsername(sellerUsername)
        .items(items)

        .shippingPrice(shippingPrice)
        .shippingVoucherPrice(shippingVoucherPrice)
        .sellerVoucherId(order.getSellerVoucherId())
        .peterVoucher(peterVoucherPrice)

        .addressId(order.getAddressId())
        .orderStatus(order.getOrderStatus())
        .paymentType(order.getPaymentType())        
        .createdAt(order.getCreatedAt().atZone(java.time.ZoneId.systemDefault()).toInstant())
        .updatedAt(order.getUpdatedAt().atZone(java.time.ZoneId.systemDefault()).toInstant())
        .build();
    });

    return pageOrderResponseFE;
  }

  @PreAuthorize("hasRole('ROLE_USER')")
  public Page<OrderResponseFE> getOrderByStatus(OrderStatus orderStatus, int page, int size) {
    String customerEmail = SecurityContextHolder.getContext().getAuthentication().getName(); 
    String customerId = userClient.userId(customerEmail);

    // Page<Order> listOrders = customOrderRepositoryImpl
    // .findOrdersByUserIdAndStatusSortedByStatusCreatedAt(customerId, "SELLER_CHECKING", pageable);
    List<Order> list = orderRepository.findByUserId(customerId);


    Set<OrderStatus> orderStatuses = new HashSet<>();

    if(orderStatus.equals(OrderStatus.SELLER_CHECKING)  || orderStatus.equals(OrderStatus.PENDING)){
      orderStatuses.add(OrderStatus.SELLER_CHECKING);
      orderStatuses.add(OrderStatus.PENDING);
    }

    if(orderStatus.equals(OrderStatus.SELLER_PREPAIRING) || orderStatus.equals(OrderStatus.SELLER_PREPAIRED)){
      orderStatuses.add(OrderStatus.SELLER_PREPAIRING);
      orderStatuses.add(OrderStatus.SELLER_PREPAIRED);

    }

    if(orderStatus.equals(OrderStatus.DISPATCHED) || orderStatus.equals(OrderStatus.DELIVERING) || orderStatus.equals(OrderStatus.SHIPPER_TAKING) || orderStatus.equals(OrderStatus.SHIPPER_TAKEN)){
      orderStatuses.add(OrderStatus.DISPATCHED);
      orderStatuses.add(OrderStatus.DELIVERING);
      orderStatuses.add(OrderStatus.SHIPPER_TAKEN);
      orderStatuses.add(OrderStatus.SHIPPER_TAKING);
    }

    if(orderStatus.equals(OrderStatus.DELIVERD)){
      orderStatuses.add(OrderStatus.DELIVERD);
    }

    if(orderStatus.equals(OrderStatus.RETURN) || orderStatus.equals(OrderStatus.REFUND)){
      orderStatuses.add(OrderStatus.RETURN);
      orderStatuses.add(OrderStatus.REFUND);
    }

    if(orderStatus.equals(OrderStatus.SELLER_CANCELLED) || orderStatus.equals(OrderStatus.CANCELLED)){
      orderStatuses.add(OrderStatus.SELLER_CANCELLED);
      orderStatuses.add(OrderStatus.CANCELLED);
    }

    List<Order> listResponse = new ArrayList<>();
    int count = 0;
    BREAKINTOHERE:
    for(Order order : list){
      Set<Status> statusSet = order.getOrderStatus();
      List<Status> sortedStatusList = statusSet.stream()
        .sorted(Comparator.comparing(Status::getCreatedAt).reversed())
        .toList();

      for(OrderStatus status : orderStatuses){
        if (!sortedStatusList.isEmpty() && sortedStatusList.get(0).getStatus().equals(status)) {
          listResponse.add(order);
          count++;
          if(count >= 10){
            break BREAKINTOHERE;
          }
        }
      }
  
    }


    List<OrderResponseFE> orderResponseFEList = listResponse.stream()
    .map(order -> {
      Set<ItemResponse> items = order.getOrderItems().stream().map(item -> {
        ProductResponse productResponse = productClient.getProductByVariantId(item.getVariantId());

        return ItemResponse.builder()
          .salePrice(item.getSalePrice())
          .discount(item.getDiscount())
          .price(item.getPrice())
          .variantId(item.getVariantId())
          .quantity(item.getQuantity())
          .productId(productResponse.getProductId())
          .variantName(productResponse.getVariantName())
          .productName(productResponse.getProductName())
          .image(productResponse.getImage())
          .build();
      }).collect(Collectors.toSet());

      String sellerUsername = userClient.usernameByUserId(order.getSellerId());

      Long shippingPrice = 0L;
      try{
        Shipping shipping = peterClient.getShippingById(order.getShippingId());
        shippingPrice = shipping.getValue();
      }catch(Exception e){
        System.out.println("shippping error: " + e.getMessage());
      }

      Long peterVoucherPrice = 0L;
      try {
        PeterVoucher peterVoucher = peterClient.getPeterVoucherById(order.getPeterVoucher());
        peterVoucherPrice = peterVoucher.getValue();
      } catch (Exception e) {
        System.out.println("peter error: " + e.getMessage());

      }

      Long shippingVoucherPrice = 0L;
      try {
        ShippingVoucher shippingVoucher = peterClient.getShippingVoucherById(order.getShippingVoucherId());
        shippingVoucherPrice = shippingVoucher.getPrice();
      } catch (Exception e) {
        System.out.println("shipping voucher error: " + e.getMessage());
      }
      return OrderResponseFE.builder()
        .id(order.getId())
        .userId(order.getUserId())
        .sellerId(order.getSellerId())
        .sellerUsername(sellerUsername)
        .items(items)

        .shippingPrice(shippingPrice)
        .shippingVoucherPrice(shippingVoucherPrice)
        .sellerVoucherId(order.getSellerVoucherId())
        .peterVoucher(peterVoucherPrice)
        
        .addressId(order.getAddressId())
        .orderStatus(order.getOrderStatus())
        .paymentType(order.getPaymentType())
        .createdAt(order.getCreatedAt().atZone(java.time.ZoneId.systemDefault()).toInstant())
        .updatedAt(order.getUpdatedAt().atZone(java.time.ZoneId.systemDefault()).toInstant())
        .build();
    })
    .collect(Collectors.toList());

    Pageable pageable = PageRequest.of(page, size); 

    Page<OrderResponseFE> pageOrderResponseFE = new PageImpl<>(orderResponseFEList, pageable, listResponse.size());
    return pageOrderResponseFE;
  }


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

}
