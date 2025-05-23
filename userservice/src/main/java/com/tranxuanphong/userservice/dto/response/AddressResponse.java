package com.tranxuanphong.userservice.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddressResponse {
    String id;

    String firstName;
    String lastName;

    LocalDate dob;

    String address;

    String phone;
}
