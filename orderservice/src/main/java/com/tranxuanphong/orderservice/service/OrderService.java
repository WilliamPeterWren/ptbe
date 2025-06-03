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
import com.tranxuanphong.orderservice.model.Variant;
import com.tranxuanphong.orderservice.repository.httpclient.UserClient;
import com.tranxuanphong.orderservice.repository.mongo.OrderRepository;
import com.tranxuanphong.orderservice.repository.mongo.custom.impl.CustomOrderRepositoryImpl;
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
// import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
public class OrderService {
  OrderRepository orderRepository;
  CustomOrderRepositoryImpl customOrderRepositoryImpl;

  UserClient userClient;
  ProductClient productClient;

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
        // If you are using Spring Security's OAuth2 Resource Server with JWTs
        authToken = ((org.springframework.security.oauth2.jwt.Jwt) authentication.getPrincipal()).getTokenValue();
    }

    if (authToken == null) {
        // Handle case where token is not found in security context, though PreAuthorize should prevent this usually
        throw new AppException(ErrorCode.UNAUTHENTICATED); // Or a more specific error
    }
    String authorizationHeader = "Bearer " + authToken;

    //update product.sold if request.getOrderStatus() == DELIVERD
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

    System.out.println("yes 00");

    Page<Order> listOrders = orderRepository.findByUserIdOrderByCreatedAtDesc(customerId, PageRequest.of(page, size));

    System.out.println("yes 01");
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

      return OrderResponseFE.builder()
        .id(order.getId())
        .userId(order.getUserId())
        .sellerId(order.getSellerId())
        .sellerUsername(sellerUsername)
        .items(items)
        .shippingId(order.getShippingId())
        .shippingVoucherId(order.getShippingVoucherId())
        .sellerVoucherId(order.getSellerVoucherId())
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

    System.out.println("user id: " + customerId);

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
    System.out.println("here");
    int count = 0;
    BREAKINTOHERE:
    for(Order order : list){
      System.out.println("in loop");
      Set<Status> statusSet = order.getOrderStatus();
      List<Status> sortedStatusList = statusSet.stream()
        .sorted(Comparator.comparing(Status::getCreatedAt).reversed())
        .toList();

      for(OrderStatus status : orderStatuses){
        System.out.println(status);
        if (!sortedStatusList.isEmpty() && sortedStatusList.get(0).getStatus().equals(status)) {
          listResponse.add(order);
          count++;
          if(count >= 10){
            break BREAKINTOHERE;
          }
        }
      }
  
    }

    System.out.println("out loop");

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

      return OrderResponseFE.builder()
        .id(order.getId())
        .userId(order.getUserId())
        .sellerId(order.getSellerId())
        .sellerUsername(sellerUsername)
        .items(items)
        .shippingId(order.getShippingId())
        .shippingVoucherId(order.getShippingVoucherId())
        .sellerVoucherId(order.getSellerVoucherId())
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


}
