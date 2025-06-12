package com.tranxuanphong.orderservice.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.tranxuanphong.orderservice.dto.request.OrderCreateRequest;
import com.tranxuanphong.orderservice.dto.request.ReviewCheckRequest;
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
import com.tranxuanphong.orderservice.repository.httpclient.PeterClient;
import com.tranxuanphong.orderservice.repository.httpclient.ProductClient;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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

    try {
      userClient.updateUserPeterVoucher(request.getPeterVoucher(), customerId);
    } catch (Exception e) {
      System.out.println("erro: " + e.getMessage());
    }

    return orderMapper.toOrderResponse(orderRepository.save(order));
  }

  public boolean doesAddressExist(String addressId){
    return orderRepository.existsByAddressId(addressId);
  }

  @PreAuthorize("hasRole('ROLE_USER')")
  public Page<OrderResponseFE> getAllOrderByUser(int page, int size) {
    String customerEmail = SecurityContextHolder.getContext().getAuthentication().getName(); 
    String customerId = userClient.userId(customerEmail);


    Page<Order> listOrders = orderRepository.findByUserIdOrderByUpdatedAtDesc(customerId, PageRequest.of(page, size));

    Page<OrderResponseFE> pageOrderResponseFE = listOrders.map(order -> {
      Set<ItemResponse> items = order.getOrderItems().stream().map(item -> {
        ProductResponse productResponse =  productClient.getProductByVariantId(item.getVariantId());
        boolean alreadyReview = false;

        try {
          ReviewCheckRequest request = ReviewCheckRequest.builder()
          .userId(customerId)
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

    List<Order> list = orderRepository.findByUserIdOrderByUpdatedAtDesc(customerId);

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
          .userId(customerId)
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




}
