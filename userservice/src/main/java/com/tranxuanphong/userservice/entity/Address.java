package com.tranxuanphong.userservice.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class Address {
    @Id
    String id;

    String firstName;
    String lastName;

    LocalDate dob;
    Boolean gender;

    String address;

    @Indexed(unique = true)
    String phone;
}
