package com.tranxuanphong.peterservice.service;


import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;


// import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.tranxuanphong.peterservice.dto.request.PeterCategoryCreateRequest;
import com.tranxuanphong.peterservice.dto.request.PeterCategoryUpdateRequest;
import com.tranxuanphong.peterservice.dto.response.PeterCategoryResponse;
import com.tranxuanphong.peterservice.entity.PeterCategory;
import com.tranxuanphong.peterservice.exception.AppException;
import com.tranxuanphong.peterservice.exception.ErrorCode;
import com.tranxuanphong.peterservice.mapper.PeterCategoryMapper;
import com.tranxuanphong.peterservice.repository.mongo.PeterCategoryRepository;
import com.tranxuanphong.peterservice.utils.GenerateSlug;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
public class PeterCategoryService {
  PeterCategoryRepository peterCategoryRepository;
  GenerateSlug generateSlug;
  PeterCategoryMapper peterCategoryMapper;

  @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_STAFF')")
  public PeterCategoryResponse create(PeterCategoryCreateRequest request){
    if(peterCategoryRepository.existsByName(request.getName())){
      throw new AppException(ErrorCode.CATEGORY_EXISTS);
    }

    if(request.getParentId() != null && !peterCategoryRepository.existsById(request.getParentId())){
      throw new AppException(ErrorCode.CATEGORY_NOT_EXISTS);
    }

    String slug = generateSlug.generateSlug(request.getName());


    PeterCategory peterCategory = peterCategoryMapper.toPeterCategory(request);
    peterCategory.setSlug(slug);


    peterCategoryRepository.save(peterCategory);

    // return PeterCategoryResponse.builder()
    // .id(peterCategory.getId())
    // .name(peterCategory.getName())
    // .parentId(peterCategory.getParentId())
    // .slug(slug)
    // .images(peterCategory.getImages())
    // .build();

    return peterCategoryMapper.toPeterCategoryResponse(peterCategory);
  }

  public List<PeterCategoryResponse> getAll(){
    List<PeterCategory> list = peterCategoryRepository.findAll();
    return peterCategoryMapper.toListPeterCategoryResponse(list);
  }

  public PeterCategoryResponse getOne(String id){
    return peterCategoryMapper.toPeterCategoryResponse(peterCategoryRepository.findById(id).orElseThrow(()->new AppException(ErrorCode.CATEGORY_NOT_EXISTS)));
  }
  
  @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_STAFF')")
  public PeterCategoryResponse update(String id, PeterCategoryUpdateRequest request){
    
    if(peterCategoryRepository.existsByName(request.getName())){
      throw new AppException(ErrorCode.CATEGORY_EXISTS);
    }

    if(request.getParentId() != null && !peterCategoryRepository.existsById(request.getParentId())){
      throw new AppException(ErrorCode.CATEGORY_NOT_EXISTS);
    }

    if(request.getParentId().equals(id)){
      throw new AppException(ErrorCode.CATEGORYID_INVALID);
    }

    PeterCategory peterCategory = peterCategoryRepository.findById(id).orElseThrow(()->new AppException(ErrorCode.CATEGORY_NOT_EXISTS));

    peterCategoryMapper.updatePeterCategory(peterCategory, request);

    String slug = generateSlug.generateSlug(request.getName());
    peterCategory.setSlug(slug);

    return peterCategoryMapper.toPeterCategoryResponse(peterCategory);
  }


  public boolean existsById(String id){
    return peterCategoryRepository.existsById(id);
  }
}
