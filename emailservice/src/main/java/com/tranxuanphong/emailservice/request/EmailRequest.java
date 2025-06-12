package com.tranxuanphong.emailservice.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EmailRequest {
    private String to;
    private String subject;
    private String htmlBody;

}
