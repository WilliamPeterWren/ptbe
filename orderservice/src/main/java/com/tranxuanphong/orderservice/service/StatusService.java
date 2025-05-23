package com.tranxuanphong.orderservice.service;

import org.springframework.stereotype.Service;

import com.tranxuanphong.orderservice.dto.request.CreateOrderRequest;
import com.tranxuanphong.orderservice.dto.request.StatusCreateRequest;
import com.tranxuanphong.orderservice.dto.request.UpdateOrderRequest;
import com.tranxuanphong.orderservice.dto.response.OrderResponse;
import com.tranxuanphong.orderservice.entity.Order;
import com.tranxuanphong.orderservice.entity.Status;
import com.tranxuanphong.orderservice.exception.AppException;
import com.tranxuanphong.orderservice.exception.ErrorCode;
import com.tranxuanphong.orderservice.mapper.OrderMapper;
import com.tranxuanphong.orderservice.repository.OrderRepository;
import com.tranxuanphong.orderservice.repository.StatusRepository;
import com.tranxuanphong.orderservice.repository.httpclient.UserClient;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.util.List;


@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
@Slf4j
public class StatusService {
  StatusRepository statusRepository;

  public Status create(StatusCreateRequest request){
    Status status = Status.builder()
    .status(request.getStatus())
    .build();

    return statusRepository.save(status);
  }
}
