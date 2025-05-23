package com.tranxuanphong.userservice.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tranxuanphong.userservice.dto.request.LoginRequest;
import com.tranxuanphong.userservice.dto.request.RegisterRequest;
import com.tranxuanphong.userservice.dto.request.UserUpdateRequest;
import com.tranxuanphong.userservice.dto.response.LoginResponse;
import com.tranxuanphong.userservice.dto.response.UserResponse;
import com.tranxuanphong.userservice.entity.Role;
import com.tranxuanphong.userservice.entity.User;
// import com.tranxuanphong.userservice.enums.Role;
import com.tranxuanphong.userservice.exception.AppException;
import com.tranxuanphong.userservice.exception.ErrorCode;
import com.tranxuanphong.userservice.mapper.UserMapper;
import com.tranxuanphong.userservice.repository.RoleRepository;
import com.tranxuanphong.userservice.repository.UserRepository;

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
    
    return userMapper.toUserResponse(userRepository.save(user));
  }

  public UserResponse update(String email, UserUpdateRequest request){
    User user = userRepository.findByEmail(email).orElseThrow(()-> new AppException(ErrorCode.EMAIL_EXISTED));
    userMapper.updateUser(user, request);
    user.setPassword(passwordEncoder.encode(request.getPassword()));


    userRepository.save(user);
    return userMapper.toUserResponse(user);
  }

  @PostAuthorize("returnObject.username == authentication.name")
  public UserResponse getUser(String email){
    return userMapper.toUserResponse(userRepository.findByEmail(email).orElseThrow(()-> new AppException(ErrorCode.EMAIL_INVALID)));
  }

  @PreAuthorize("hasRole('ROLE_ADMIN')")
  // @PreAuthorize("hasAuthority('APPROVE_POST')")
  public List<UserResponse> getAll(){
    var authentication = SecurityContextHolder.getContext().getAuthentication(); 
    log.info("getUsers authentication: {}, username: {}", authentication, authentication.getName()); 
    authentication.getAuthorities().forEach(grantedAuthority -> log.info("grantedAuthority: {}", grantedAuthority.getAuthority())); 
    
    List<User> listUser = userRepository.findAll();
    return userMapper.toListUserResponse(listUser);
  }

  public UserResponse getMyInfo(){ 
    String email = SecurityContextHolder.getContext().getAuthentication().getName(); 
    User user = userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.EMAIL_NOT_EXISTED)); 
    
    
    return userMapper.toUserResponse(user); 
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
            .build();
    }

  // product
  public boolean checkId(String id){

    log.info("id: " + id);
//    return CheckIdResponse.builder()
//    .check(userRepository.existsById(id))
//    .build();

    return userRepository.existsById(id);
  }

  public String getUserId(String email){
    System.out.println("email 2: " + email);
    User user = userRepository.findByEmail(email).orElseThrow(()-> new AppException(ErrorCode.UNAUTHENTICATED));
    System.out.println("user: " + user.getId());

    return user.getId();
  }
}
