package com.emailassistant.email_assistant.controller;

import com.emailassistant.email_assistant.model.request.EmailRequest;
import com.emailassistant.email_assistant.service.EmailGeneratorService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.emailassistant.email_assistant.common.ApiConstant;

@RestController
@RequestMapping(ApiConstant.BASE_API)
@AllArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class EmailGeneratorController {

    private final EmailGeneratorService emailGeneratorService;

    @PostMapping(ApiConstant.GENERATE_EMAIL)
    public ResponseEntity<String> generateEmail(@RequestBody EmailRequest emailRequest) {
       String response = emailGeneratorService.generateEmail(emailRequest);
        return ResponseEntity.ok(response);
    }
}
