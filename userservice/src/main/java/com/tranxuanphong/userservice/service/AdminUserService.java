package com.tranxuanphong.userservice.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.tranxuanphong.userservice.dto.response.UserResponse;
import com.tranxuanphong.userservice.entity.Role;
import com.tranxuanphong.userservice.entity.User;
import com.tranxuanphong.userservice.exception.AppException;
import com.tranxuanphong.userservice.exception.ErrorCode;
import com.tranxuanphong.userservice.mapper.UserMapper;
import com.tranxuanphong.userservice.repository.mongo.RoleRepository;
import com.tranxuanphong.userservice.repository.mongo.UserRepository;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
// import lombok.extern.slf4j.Slf4j;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
public class AdminUserService {
  UserRepository userRepository;
  RoleRepository roleRepository;

  UserMapper userMapper;

  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public void adminUpdateAllUser(){
    List<User> list = userRepository.findAll();

    // Map<String, Integer> peterVoucher = new HashMap<>() {{
    //   put("684010440414931d729faa42", 10);
    //   put("6840106e0414931d729faa43", 15);
    //   put("6840107b0414931d729faa44", 10);
    //   put("684010860414931d729faa45", 12);
    //   put("6840108f0414931d729faa46", 14);
    // }};

    Map<String, Integer> shippingVoucher = new HashMap<>() {{
      put("6840060e0414931d729faa3d", 10);
      put("684006180414931d729faa3e", 15);
      put("684006250414931d729faa3f", 10);
      put("6840062a0414931d729faa40", 12);
      put("6840063b0414931d729faa41", 14);
      put("6849529d033a5f57bad6e04c", 14);
    }};

    for(User user : list){
      // user.setFollower(null);
      // user.setFollowing(null);
      // user.setRating(null);
      // user.setAvatar("");
//      PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
//      user.setPassword(passwordEncoder.encode("12345678"));


      user.setShippingVoucher(shippingVoucher);
      

      userRepository.save(user);
    }
  }

  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public List<UserResponse> getAll(){
    var authentication = SecurityContextHolder.getContext().getAuthentication(); 
    System.out.println("getUsers authentication: " +authentication + ", username: " + authentication.getName()); 
    // authentication.getAuthorities().forEach(grantedAuthority -> log.info("grantedAuthority: {}", grantedAuthority.getAuthority())); 
    
    List<User> listUser = userRepository.findAll();
    return userMapper.toListUserResponse(listUser);
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

  public void updateShippingVoucher(String shippingVoucherId, int count){
    List<User> list = userRepository.findAll();

    for(User user :list){
      Map<String, Integer> shippingVoucherList = user.getShippingVoucher();
      shippingVoucherList.put(shippingVoucherId, count);

      user.setShippingVoucher(shippingVoucherList);

      userRepository.save(user);
    }

  }

  public void updatePeterVoucher(String peterVoucherId, int count){
    List<User> list = userRepository.findAll();

    for(User user :list){
      Map<String, Integer> peterVoucherList = user.getPeterVoucher();
      peterVoucherList.put(peterVoucherId, count);
      user.setPeterVoucher(peterVoucherList);

      userRepository.save(user);
    }

  }

}
