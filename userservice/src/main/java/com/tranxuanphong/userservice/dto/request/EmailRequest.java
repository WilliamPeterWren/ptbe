package com.tranxuanphong.userservice.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class EmailRequest {
    private String to;
    private String subject;
    private String htmlBody;

}
