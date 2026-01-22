package com.emailassistant.email_assistant.controller;

import com.emailassistant.email_assistant.model.request.EmailRequest;
import com.emailassistant.email_assistant.service.EmailGeneratorService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.emailassistant.email_assistant.common.ApiConstant;

@RestController
@RequestMapping(ApiConstant.BASE_API)
@AllArgsConstructor
public class EmailGeneratorController {

    private final EmailGeneratorService emailGeneratorService;

    @PostMapping(ApiConstant.GENERATE_EMAIL)
    public ResponseEntity<String> generateEmail(@RequestBody EmailRequest emailRequest) {
       String response = emailGeneratorService.generateEmail(emailRequest);
        return ResponseEntity.ok("Email generated");
    }
}
