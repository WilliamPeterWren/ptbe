package com.tranxuanphong.productservice.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tranxuanphong.productservice.dto.request.CreateInfoRequest;
import com.tranxuanphong.productservice.dto.request.UpdateInfoRequest;
import com.tranxuanphong.productservice.dto.response.ApiResponse;
import com.tranxuanphong.productservice.dto.response.InfoResponse;
import com.tranxuanphong.productservice.service.InfoService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RequestMapping("/api/infos")
public class InfoController {
  InfoService infoService;


  @PostMapping
  public ApiResponse<InfoResponse> create(@RequestBody CreateInfoRequest request) {
    return ApiResponse.<InfoResponse>builder()
      .result(infoService.create(request))
      .build();
  }

  @GetMapping("/get-all")
  public ApiResponse<List<InfoResponse>> getAll() {
    return ApiResponse.<List<InfoResponse>>builder()
    .result(infoService.getAll())
    .build();
  }

  @GetMapping("/by-product/{productId}")
  public ApiResponse<List<InfoResponse>> getByProduct(@PathVariable String productId){
    return ApiResponse.<List<InfoResponse>>builder()
    .result(infoService.getByProductId(productId))
    .build();
  }

  @GetMapping("/{id}")
  public ApiResponse<InfoResponse> getOne(@PathVariable String id) {
    return ApiResponse.<InfoResponse>builder()
    .result(infoService.getOne(id))
    .build();
  }

  @PutMapping("/{id}")
  public ApiResponse<InfoResponse> upadte(@PathVariable String id, @RequestBody UpdateInfoRequest request) {
    return ApiResponse.<InfoResponse>builder()
    .result(infoService.update(id, request))
    .build();
  }
  
  @DeleteMapping("/{id}")
  public ApiResponse<String> delete(@PathVariable String id){
    infoService.delete(id);

    return ApiResponse.<String>builder()
    .result("Success delete info")
    .build();
  }
}
