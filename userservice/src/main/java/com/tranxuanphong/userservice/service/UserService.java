package com.tranxuanphong.userservice.service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tranxuanphong.userservice.dto.request.LoginRequest;
import com.tranxuanphong.userservice.dto.request.RegisterRequest;
import com.tranxuanphong.userservice.dto.request.UserUpdatePasswordRequest;
import com.tranxuanphong.userservice.dto.response.LoginResponse;
import com.tranxuanphong.userservice.dto.response.SellerInfoResponse;
import com.tranxuanphong.userservice.dto.response.UserResponse;
import com.tranxuanphong.userservice.entity.Role;
import com.tranxuanphong.userservice.entity.User;
// import com.tranxuanphong.userservice.enums.Role;
import com.tranxuanphong.userservice.exception.AppException;
import com.tranxuanphong.userservice.exception.ErrorCode;
import com.tranxuanphong.userservice.mapper.UserMapper;
import com.tranxuanphong.userservice.repository.httpclient.ProductClient;
import com.tranxuanphong.userservice.repository.mongo.RoleRepository;
import com.tranxuanphong.userservice.repository.mongo.UserRepository;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
@Slf4j
public class UserService {

  UserRepository userRepository;
  UserMapper userMapper;
  PasswordEncoder passwordEncoder;
  RoleRepository roleRepository;
  AuthenticationService authenticationService;

  ProductClient productClient;

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
    
    return userMapper.toUserResponse(userRepository.save(user));
  }

  public UserResponse updatePassword(UserUpdatePasswordRequest request){
    String email = SecurityContextHolder.getContext().getAuthentication().getName(); 
    User user = userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.EMAIL_NOT_EXISTED)); 
    System.out.println("email: " + email);

    if(request.getPassword() != null)
      user.setPassword(passwordEncoder.encode(request.getPassword()));

    if(request.getUsername() != null && !userRepository.existsByUsername(request.getUsername())){
      user.setUsername(request.getUsername());
    }

    System.out.println("user name: " + request.getUsername());

    return userMapper.toUserResponse(userRepository.save(user));
  }

  public UserResponse getUser(){
    String email = SecurityContextHolder.getContext().getAuthentication().getName(); 
    return userMapper.toUserResponse(userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.EMAIL_NOT_EXISTED)));
  }

  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public List<UserResponse> getAll(){
    var authentication = SecurityContextHolder.getContext().getAuthentication(); 
    log.info("getUsers authentication: {}, username: {}", authentication, authentication.getName()); 
    authentication.getAuthorities().forEach(grantedAuthority -> log.info("grantedAuthority: {}", grantedAuthority.getAuthority())); 
    
    List<User> listUser = userRepository.findAll();
    return userMapper.toListUserResponse(listUser);
  }

  public LoginResponse login(LoginRequest request) {
    User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new AppException(ErrorCode.EMAIL_NOT_EXISTED));

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
            .build();
  }

  public UserResponse updateRole(String roleId){
    String email = SecurityContextHolder.getContext().getAuthentication().getName(); 
    User user = userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.EMAIL_NOT_EXISTED)); 
    Set<Role> roles = user.getRoles();
    Role role = roleRepository.findById(roleId).orElseThrow(()-> new AppException(ErrorCode.ROLE_NOT_FOUND));
    roles.add(role);
    user.setRoles(roles);
    return userMapper.toUserResponse(userRepository.save(user));
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

  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public void adminUpdateAllUser(){
    List<User> list = userRepository.findAll();
    for(User user : list){
      user.setFollower(null);
      user.setFollowing(null);
      user.setRating(null);
      user.setAvatar("");

      userRepository.save(user);
    }
  }

  public SellerInfoResponse getSellerInfo(String sellerId){
    User user = userRepository.findById(sellerId).orElseThrow(()->new AppException(ErrorCode.UNAUTHENTICATED));
  
    int countProduct = 0;

    try {
      countProduct = productClient.countProductBySellerId(sellerId);
    } catch (Exception e) {
      System.out.println("count product error: " + e.getMessage());
    }

    int follower = user.getFollower().size();
    int following = user.getFollowing().size();

    Map<Integer, Long> rating = user.getRating();

    int countRating = 0;

    double star = 0;
    long totalCount = 0;
    long weightedSum = 0;

    for (Map.Entry<Integer, Long> entry : rating.entrySet()) {
      int key = entry.getKey();     
      long value = entry.getValue(); 

      countRating += (int)value;

      weightedSum += key * value;
      totalCount += value;
    }

    if (totalCount > 0) {
      star = (double) weightedSum / totalCount;
    }
  

    SellerInfoResponse sellerInfoResponse = SellerInfoResponse.builder()
    .sellerId(sellerId)
    .sellerUsername(user.getUsername())
    .createdAt(user.getCreatedAt())
    .countProduct(countProduct)
    .follower(follower)
    .following(following)
    .rating(countRating)
    .star(star)
    .avatar(user.getAvatar())
    .build();

    return sellerInfoResponse;
  }

  @PreAuthorize("hasRole('ROLE_USER')")
  public UserResponse updateRatingBySellerId(int star, String sellerId){
    User user = userRepository.findById(sellerId).orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));
  
    Map<Integer, Long> rating = user.getRating();
    rating.put(star, rating.getOrDefault(star, 0L) + 1);
    user.setRating(rating);

    return userMapper.toUserResponse(userRepository.save(user));
  }
}
