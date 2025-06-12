package com.tranxuanphong.peterservice.dto.model;

import java.util.Objects;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data; 
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;


@NoArgsConstructor
@AllArgsConstructor 
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Data 
public class Info {

    String id;
    String name;
    String detail;

}