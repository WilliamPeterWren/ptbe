package com.tranxuanphong.userservice.service;

import java.lang.module.ModuleDescriptor.Builder;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.tranxuanphong.userservice.dto.request.EmailRequest;
import com.tranxuanphong.userservice.dto.request.LoginRequest;
import com.tranxuanphong.userservice.dto.request.RegisterRequest;
import com.tranxuanphong.userservice.dto.request.UserUpdateRequest;
import com.tranxuanphong.userservice.dto.response.ApiResponse;
import com.tranxuanphong.userservice.dto.response.CartResponse;
import com.tranxuanphong.userservice.dto.response.LoginResponse;
import com.tranxuanphong.userservice.dto.response.UserResponse;
import com.tranxuanphong.userservice.entity.Role;
import com.tranxuanphong.userservice.entity.User;
import com.tranxuanphong.userservice.exception.AppException;
import com.tranxuanphong.userservice.exception.ErrorCode;
import com.tranxuanphong.userservice.mapper.UserMapper;
import com.tranxuanphong.userservice.repository.httpclient.CartClient;
import com.tranxuanphong.userservice.repository.httpclient.EmailClient;
import com.tranxuanphong.userservice.repository.mongo.RoleRepository;
import com.tranxuanphong.userservice.repository.mongo.UserRepository;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
// import lombok.extern.slf4j.Slf4j;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
// @Slf4j
public class UserService {

  UserRepository userRepository;

  UserMapper userMapper;
  PasswordEncoder passwordEncoder;
  RoleRepository roleRepository;
  AuthenticationService authenticationService;

  CartClient cartClient;
  EmailClient emailClient;

  public UserResponse register(RegisterRequest request){

    if(userRepository.existsByEmail(request.getEmail())){
      throw new AppException(ErrorCode.EMAIL_EXISTED);
    }

    User user = userMapper.toUser(request);
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
    user.setPassword(passwordEncoder.encode(request.getPassword()));

    Role userRole = roleRepository.findById("ROLE_USER")
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
                
    Set<Role> roles = new HashSet<>();
    roles.add(userRole);
    user.setRoles(roles);
    user.setUsername(request.getEmail());

    user.setIsVerify(false);

    System.out.println("yes 00");
    // create cart 
    userRepository.save(user);

    Set<String> roless = roles.stream()
    .map(Role::getName)
    .collect(Collectors.toSet());

    try {
      String accessToken = authenticationService.generateAccessToken(user.getEmail(), roless, user.getId());
      String authorization = "Bearer " + accessToken;
      ApiResponse<CartResponse> cartResponse = cartClient.create(authorization);
      
    } catch (Exception e) {
      System.out.println("cart service error: " + e.getMessage());
    }

    // add shipping voucher 

    // add peter voucher

    System.out.println("yes 01");
    // send email
    String to = request.getEmail();
    String subject = "Active your account";
    String htmlBody = "<span>Nhấn vào đây để kích hoạt tài khoản</span> "
    + "<span> <a href='http://localhost:8889/api/users/verify/id/" 
    + user.getId() 
    + "'>Đây</a> </span>";

    EmailRequest req = EmailRequest.builder()
      .to(to)
      .subject(subject)
      .htmlBody(htmlBody)
      .build();

    try {
      
      emailClient.sendEmail(req);
    } catch (Exception e) {
      System.out.println("error: " +e.getMessage());
    }

    System.out.println("yes 02");

    return userMapper.toUserResponse(userRepository.save(user));
  }

  public void verifyUserById(String userId){
    User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));
    user.setIsVerify(true);
    userRepository.save(user);
  }

  public UserResponse update(UserUpdateRequest request){
    String email = SecurityContextHolder.getContext().getAuthentication().getName(); 
    User user = userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.EMAIL_NOT_EXISTED)); 
    System.out.println("email: " + email);

    if(request.getPassword() != null)
      user.setPassword(passwordEncoder.encode(request.getPassword()));

    if(request.getUsername() != null && !userRepository.existsByUsername(request.getUsername())){
      user.setUsername(request.getUsername());
    }
    System.out.println("avatar: " + request.getAvatar());
    if(request.getAvatar() != null){
      user.setAvatar(request.getAvatar());
    }


    return userMapper.toUserResponse(userRepository.save(user));
  }

  public UserResponse getUser(){
    String email = SecurityContextHolder.getContext().getAuthentication().getName(); 
    return userMapper.toUserResponse(userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.EMAIL_NOT_EXISTED)));
  }

  public LoginResponse login(LoginRequest request) {
    // System.out.println("email: " + request.getEmail()  + " password: " + request.getPassword());

    User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new AppException(ErrorCode.EMAIL_NOT_EXISTED));

    if(user.getIsVerify() == false){
      throw new AppException(ErrorCode.EMAIL_NOT_VERIFY);
    }

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
    boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());

    if (!authenticated) {
        throw new AppException(ErrorCode.UNAUTHENTICATED);
    }

    Set<String> roles = user.getRoles().stream()
            .map(Role::getName)
            .collect(Collectors.toSet());
    
    String accessToken = authenticationService.generateAccessToken(user.getEmail(), roles, user.getId());

    String refreshToken = authenticationService.generateRefreshToken(user.getEmail(), user.getId());

    return LoginResponse.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .id(user.getId())
            .email(user.getEmail())
            .username(user.getUsername())
            .roles(user.getRoles())
            .addressId(user.getAddressId())
            .avatar(user.getAvatar())
            .peterVoucher(user.getPeterVoucher())
            .shippingVoucher(user.getShippingVoucher())
            .build();
  }

  // product
  public boolean checkId(String id){
    return userRepository.existsById(id);
  }

  public boolean checkByEmail(String email){
    return userRepository.existsByEmail(email);
  }

  public String getUserId(String email){
    User user = userRepository.findByEmail(email).orElseThrow(()-> new AppException(ErrorCode.UNAUTHENTICATED));
    return user.getId();
  }

  public String getUsernameByEmail(String email){
    User user = userRepository.findByEmail(email).orElseThrow(()-> new AppException(ErrorCode.UNAUTHENTICATED));
    return user.getUsername();
  }

  public String getUsernameById(String id){
    User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

    return user.getUsername();
  }

  @PreAuthorize("hasRole('ROLE_USER')")
  public UserResponse updateRatingBySellerId(int star, String sellerId){
    User user = userRepository.findById(sellerId).orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));
  
    Map<Integer, Long> rating = user.getRating();
    rating.put(star, rating.getOrDefault(star, 0L) + 1);
    user.setRating(rating);

    return userMapper.toUserResponse(userRepository.save(user));
  }

  public String userAvatar(String id){
    User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));
    return user.getAvatar();
  }

  public void updateUserPeterVoucher(String userId, String peterVoucherId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

    Map<String, Integer> userPeterVoucher = user.getPeterVoucher();

    if (userPeterVoucher == null || !userPeterVoucher.containsKey(peterVoucherId)) {
        throw new AppException(ErrorCode.VOUCHER_NOT_FOUND); 
    }

    Integer currentCount = userPeterVoucher.get(peterVoucherId);
    if (currentCount == null || currentCount <= 0) {
        throw new AppException(ErrorCode.VOUCHER_ALREADY_USED); 
    }

    userPeterVoucher.put(peterVoucherId, currentCount - 1);

    user.setPeterVoucher(userPeterVoucher);
    userRepository.save(user);
  }


}
