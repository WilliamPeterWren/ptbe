package com.tranxuanphong.productservice.service;

import org.springframework.stereotype.Service;

import com.tranxuanphong.productservice.dto.request.CreateInfoRequest;
import com.tranxuanphong.productservice.dto.request.UpdateInfoRequest;
import com.tranxuanphong.productservice.dto.response.InfoResponse;
import com.tranxuanphong.productservice.entity.Info;
import com.tranxuanphong.productservice.exception.AppException;
import com.tranxuanphong.productservice.exception.ErrorCode;
import com.tranxuanphong.productservice.mapper.InfoMapper;
import com.tranxuanphong.productservice.repository.InfoRepository;
import com.tranxuanphong.productservice.repository.ProductRepository;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
@Slf4j
public class InfoService {
  InfoRepository infoRepository;
  ProductRepository productRepository;
  InfoMapper infoMapper;


  public InfoResponse create(CreateInfoRequest request){

    if(!productRepository.existsById(request.getProductId())){
      throw new AppException(ErrorCode.PRODUCT_NOT_EXISTS);
    }
       
    List<Info> list = infoRepository.findByProductId(request.getProductId());
    boolean hasName = list.stream().anyMatch(info -> info.getName().equals(request.getName()));
    if(hasName){
      throw new AppException(ErrorCode.INFO_EXISTS);
    }

    Info info = infoMapper.toInfo(request);

    return infoMapper.toInfoResponse(infoRepository.save(info));
  }

  public List<InfoResponse> getAll(){
    return infoMapper.toListInfoResponse(infoRepository.findAll());
  }

  public List<InfoResponse> getByProductId(String productId){
    return infoMapper.toListInfoResponse(infoRepository.findByProductId(productId));
  }

  public InfoResponse getOne(String id){
    Info info = infoRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.INFO_NOT_EXISTS));
    return infoMapper.toInfoResponse(info);
  }

  public InfoResponse update(String id, UpdateInfoRequest request){
    
    if(!productRepository.existsById(request.getProductId())){
      throw new AppException(ErrorCode.PRODUCT_NOT_EXISTS);
    }

    Info info = infoRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.INFO_NOT_EXISTS));
    
    List<Info> list = infoRepository.findByProductId(request.getProductId());
    boolean hasName = list.stream().anyMatch(i -> i.getName().equals(request.getName()));
    if(hasName){
      throw new AppException(ErrorCode.INFO_EXISTS);
    }

    // infoMapper.updateInfo(info, request);

    info.setName(request.getName());
    info.setDetail(request.getDetail());
    
    infoRepository.save(info);

    return infoMapper.toInfoResponse(info);
  }

  public void delete(String id){
    infoRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.INFO_NOT_EXISTS));

    infoRepository.deleteById(id);
  }
}
