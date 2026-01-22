package com.emailassistant.email_assistant.model.request;

import lombok.Data;

@Data
public class EmailRequest {
    private String emailContent;
    private String tone;
}
