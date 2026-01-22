package com.emailassistant.email_assistant.service;

import com.emailassistant.email_assistant.model.request.EmailRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Service
public class EmailGeneratorService {

    private final String baseUrl;

    private final String apikey;

    private final WebClient webClient;

    public EmailGeneratorService(@Value("${gemini.api.url}") String baseUrl,
                                 @Value("${gemini.api.key}") String apikey,
                                 WebClient.Builder webClientBuilder) {
        this.baseUrl = baseUrl;
        this.apikey = apikey;
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    public String generateEmail(EmailRequest emailRequest) {
        String prompt = buildPrompt(emailRequest);

        String requestBody = String.format("""
                {
                    "contents": [
                      {
                        "parts": [
                          {
                            "text": "%s"
                          }
                        ]
                      }
                    ]
                  }""",prompt);

        String response = webClient.post()
                .uri(uriBuilder -> uriBuilder.path("/v1beta/models/gemini-3-flash-preview:generateContent").build())
                .header("x-goog-api-key", apikey)
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return extractResponse(response);
    }

    private String extractResponse(String response) {
        if (response == null || response.isEmpty()) {
            throw new RuntimeException("Empty response from Gemini API");
        }

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode root = objectMapper.readTree(response);
            JsonNode textNode = root.path("candidates").get(0).path("content").path("parts").get(0).path("text");
            if (textNode.isMissingNode() || textNode.isNull()) {
                throw new RuntimeException("No text found in Gemini response");
            }
            return textNode.asText();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String buildPrompt(EmailRequest emailRequest) {
        StringBuilder promptBuilder = new StringBuilder();
        promptBuilder.append("Generate an email reply for the following content: ");
        if(emailRequest.getTone()!=null && !emailRequest.getTone().isEmpty()) {
            promptBuilder.append(" with a ").append(emailRequest.getTone()).append(" tone.");
        }
        promptBuilder.append("Original Email: \n").append(emailRequest.getEmailContent());
        return promptBuilder.toString();
    }
}
