package com.tranxuanphong.emailservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.tranxuanphong.emailservice.request.EmailRequest;
import com.tranxuanphong.emailservice.service.EmailService;

import jakarta.mail.MessagingException;

@RestController
@RequestMapping("/api/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/send")
    public String sendHtmlEmail(@RequestParam String to,
                            @RequestParam String subject,
                            @RequestParam String htmlBody) throws MessagingException {
        emailService.sendHtmlEmail(to, subject, htmlBody);
        return "Email sent successfully!";
    }

   @PostMapping("/send2")
   public String sendHtmlEmail2(@RequestBody EmailRequest request) throws MessagingException {
       emailService.sendHtmlEmail(request.getTo(), request.getSubject(), request.getHtmlBody());
       return "Email sent successfully!";
   }
}

