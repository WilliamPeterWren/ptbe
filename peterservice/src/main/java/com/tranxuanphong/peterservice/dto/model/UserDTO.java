package com.tranxuanphong.peterservice.dto.model;

import java.time.LocalDate;
import java.util.Set;

import org.springframework.boot.autoconfigure.amqp.RabbitConnectionDetails.Address;

import lombok.Getter;


@Getter
public class UserDTO {
  String id;

  String email;
  String password;

  Address address = null;

  Set<Role> roles ;

  Boolean isActive ;

  Boolean isVerify;

  LocalDate createdAt ;

  LocalDate updatedAt ;
}
