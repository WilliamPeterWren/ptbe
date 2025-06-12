package com.tranxuanphong.orderservice.service;

import com.tranxuanphong.orderservice.mapper.OrderMapper;
import com.tranxuanphong.orderservice.repository.httpclient.PeterClient;
import com.tranxuanphong.orderservice.repository.httpclient.ProductClient;
import com.tranxuanphong.orderservice.repository.httpclient.UserClient;
import com.tranxuanphong.orderservice.repository.mongo.OrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.tranxuanphong.orderservice.dto.request.OrderUpdateRequest;
import com.tranxuanphong.orderservice.dto.request.ReviewCheckRequest;
import com.tranxuanphong.orderservice.dto.response.OrderResponseFE;
import com.tranxuanphong.orderservice.dto.response.ProductResponse;
import com.tranxuanphong.orderservice.entity.Order;
import com.tranxuanphong.orderservice.entity.Status;
import com.tranxuanphong.orderservice.enums.OrderStatus;
import com.tranxuanphong.orderservice.exception.AppException;
import com.tranxuanphong.orderservice.exception.ErrorCode;
import com.tranxuanphong.orderservice.model.ItemResponse;
import com.tranxuanphong.orderservice.model.PeterVoucher;
import com.tranxuanphong.orderservice.model.Shipping;
import com.tranxuanphong.orderservice.model.ShippingVoucher;
import org.springframework.security.core.Authentication;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
public class SellerOrderService {
  OrderRepository orderRepository;

  UserClient userClient;
  ProductClient productClient;
  PeterClient peterClient;


  @PreAuthorize("hasAnyRole('ROLE_SELLER')")
  public OrderResponseFE update(String orderId, OrderUpdateRequest request){
    // String shipperEmail = SecurityContextHolder.getContext().getAuthentication().getName(); 
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String sellerEmail = authentication.getName();
    String sellerId = userClient.userId(sellerEmail);

    Order order = orderRepository.findById(orderId).orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

    if(!order.getSellerId().equals(sellerId)){
      throw new AppException(ErrorCode.ORDER_USER_NOT_FOUND);
    }

    List<OrderStatus> availableStatus = new ArrayList<>(Arrays.asList(OrderStatus.SELLER_CANCELLED, OrderStatus.SELLER_CHECKING, OrderStatus.SELLER_PREPAIRED, OrderStatus.SELLER_PREPAIRING));
    if(!availableStatus.contains(request.getOrderStatus())){
      throw new AppException(ErrorCode.ORDER_STATUS_INVALID);
    }

    Set<Status> status = order.getOrderStatus();
    Status updateStatus = Status.builder()
    .shipperId(sellerId)
    .status(request.getOrderStatus())
    .build();

    for(Status stt : status){
      if(stt.getStatus().equals(request.getOrderStatus())){
        throw new AppException(ErrorCode.ORDER_STATUS_INVALID);
      }
    }

    status.add(updateStatus);
    order.setOrderStatus(status);
    order.setUpdatedAt(Instant.now());
    orderRepository.save(order);

    Set<ItemResponse> items = order.getOrderItems().stream().map(item -> {
      ProductResponse productResponse = productClient.getProductByVariantId(item.getVariantId());

      boolean alreadyReview = false;

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
        .alreadyReview(alreadyReview)
        .build();
    }).collect(Collectors.toSet());

    String sellerUsername = "";
    try {
      sellerUsername = userClient.usernameByUserId(order.getSellerId());
      
    } catch (Exception e) {
      System.out.println("error: " + e.getMessage());
    }
    
    String username = "";
    try {
      
      username = userClient.usernameByUserId(order.getUserId());
    } catch (Exception e) {
      System.out.println("error: " + e.getMessage());        
    }

    Long shippingPrice = 0L;
    String shippingName = "";
    try{
      Shipping shipping = peterClient.getShippingById(order.getShippingId());
      shippingPrice = shipping.getValue();
      shippingName = shipping.getName();
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

    String authToken = null;
    if (authentication != null && authentication.getCredentials() instanceof String) {
      authToken = (String) authentication.getCredentials();
    } else if (authentication != null && authentication.getPrincipal() instanceof org.springframework.security.oauth2.jwt.Jwt) {
      authToken = ((org.springframework.security.oauth2.jwt.Jwt) authentication.getPrincipal()).getTokenValue();
    }

    if (authToken == null) {
        throw new AppException(ErrorCode.UNAUTHENTICATED);
    }
    String address = "";
    String authorizationHeader = "Bearer " + authToken;
    try {
      address = userClient.sellerGetAddress(order.getAddressId(), authorizationHeader);
    } catch (Exception e) {
      System.out.println("err: " + e.getMessage());    
    }

    String recieveImage = "";
    if(request.getRecieveImage() != null){
      recieveImage = request.getRecieveImage();
    }

    return OrderResponseFE.builder()
      .id(order.getId())
      .userId(order.getUserId())
      .username(username)
      .sellerId(order.getSellerId())
      .sellerUsername(sellerUsername)
      .items(items)

      .shipperId(order.getShipperId())
      .recieveImage(recieveImage)

      .shippingPrice(shippingPrice)
      .shippingName(shippingName)
      .shippingVoucherPrice(shippingVoucherPrice)
      .sellerVoucherId(order.getSellerVoucherId())
      .peterVoucher(peterVoucherPrice)
      
      .addressId(address)
      .orderStatus(order.getOrderStatus())
      .paymentType(order.getPaymentType())
      .createdAt(order.getCreatedAt().atZone(java.time.ZoneId.systemDefault()).toInstant())
      .updatedAt(order.getUpdatedAt().atZone(java.time.ZoneId.systemDefault()).toInstant())
      .build();
  
  }

  @PreAuthorize("hasRole('ROLE_SELLER')")
  public Page<OrderResponseFE> getAllOrderBySeller(int page, int size) {
    String sellerEmail = SecurityContextHolder.getContext().getAuthentication().getName(); 
    String sellerId = userClient.userId(sellerEmail);

    Page<Order> listOrders = orderRepository.findBySellerIdOrderByUpdatedAtDesc(sellerId, PageRequest.of(page, size));

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
      String username = userClient.usernameByUserId(order.getUserId());


      Long shippingPrice = 0L;
      String shippingName = "";

      try{
        Shipping shipping = peterClient.getShippingById(order.getShippingId());
        shippingPrice = shipping.getValue();
        shippingName = shipping.getName();
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
        .username(username)
        .sellerId(order.getSellerId())
        .sellerUsername(sellerUsername)
        .items(items)

        .shippingPrice(shippingPrice)
        .shippingName(shippingName)
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


  @PreAuthorize("hasRole('ROLE_SELLER')")
  public Page<OrderResponseFE> getOrderByStatus(OrderStatus orderStatus, int page, int size) {
    String sellerEmail = SecurityContextHolder.getContext().getAuthentication().getName(); 
    String sellerId = userClient.userId(sellerEmail);

    List<Order> list = orderRepository.findBySellerIdOrderByUpdatedAtDesc(sellerId);

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

        boolean alreadyReview = false;

        try {
          ReviewCheckRequest request = ReviewCheckRequest.builder()
          .userId(sellerId)
          .productId(productResponse.getProductId())
          .variantId(item.getVariantId())
          .orderId(order.getId())
          .build();
          alreadyReview = productClient.checkReviewByUserProductVariant(request);
        } catch (Exception e) {
          System.out.println("product service error: " + e.getMessage());
        }

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
          .alreadyReview(alreadyReview)
          .build();
      }).collect(Collectors.toSet());

      String sellerUsername = "";
      try {
        sellerUsername = userClient.usernameByUserId(order.getSellerId());
        
      } catch (Exception e) {
        System.out.println("error: " + e.getMessage());
      }
      String username = "";
      try {
        
        username = userClient.usernameByUserId(order.getUserId());
      } catch (Exception e) {
        System.out.println("error: " + e.getMessage());        
      }

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
        .username(username)
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


  @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_SELLER','ROLE_STAFF')")
  public OrderResponseFE getOrderById(String orderId){
    String sellerEmail = SecurityContextHolder.getContext().getAuthentication().getName(); 
    String sellerId = userClient.userId(sellerEmail);

    Order order = orderRepository.findById(orderId).orElse(null);

    Set<ItemResponse> items = order.getOrderItems().stream().map(item -> {
      ProductResponse productResponse = productClient.getProductByVariantId(item.getVariantId());

      boolean alreadyReview = false;

      try {
        ReviewCheckRequest request = ReviewCheckRequest.builder()
        .userId(sellerId)
        .productId(productResponse.getProductId())
        .variantId(item.getVariantId())
        .orderId(order.getId())
        .build();
        alreadyReview = productClient.checkReviewByUserProductVariant(request);
      } catch (Exception e) {
        System.out.println("product service error: " + e.getMessage());
      }

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
        .alreadyReview(alreadyReview)
        .build();
    }).collect(Collectors.toSet());

    String sellerUsername = "";
    try {
      sellerUsername = userClient.usernameByUserId(order.getSellerId());
      
    } catch (Exception e) {
      System.out.println("error: " + e.getMessage());
    }
    
    String username = "";
    try {
      
      username = userClient.usernameByUserId(order.getUserId());
    } catch (Exception e) {
      System.out.println("error: " + e.getMessage());        
    }

    Long shippingPrice = 0L;
    String shippingName = "";
    try{
      Shipping shipping = peterClient.getShippingById(order.getShippingId());
      shippingPrice = shipping.getValue();
      shippingName = shipping.getName();
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

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
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
    String address = "";
    try {
      address = userClient.sellerGetAddress(order.getAddressId(), authorizationHeader);
    } catch (Exception e) {
      System.out.println("err: " + e.getMessage());    
    }

    return OrderResponseFE.builder()
        .id(order.getId())
        .userId(order.getUserId())
        .username(username)
        .sellerId(order.getSellerId())
        .sellerUsername(sellerUsername)
        .items(items)

        .shipperId(order.getShipperId())
        .recieveImage(order.getRecieveImage())

        .shippingPrice(shippingPrice)
        .shippingName(shippingName)
        .shippingVoucherPrice(shippingVoucherPrice)
        .sellerVoucherId(order.getSellerVoucherId())
        .peterVoucher(peterVoucherPrice)
        
        .addressId(address)
        .orderStatus(order.getOrderStatus())
        .paymentType(order.getPaymentType())
        .createdAt(order.getCreatedAt().atZone(java.time.ZoneId.systemDefault()).toInstant())
        .updatedAt(order.getUpdatedAt().atZone(java.time.ZoneId.systemDefault()).toInstant())
        .build();
  }

}
