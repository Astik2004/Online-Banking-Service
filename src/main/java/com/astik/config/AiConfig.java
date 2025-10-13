//package com.astik.config;
//
//import org.springframework.ai.chat.client.ChatClient;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class AiConfig {
//
//    @Bean
//    public ChatClient chatClient() {
//        String apiKey = "AIzaSyDkw53EEuoss0g94qXc8S7r0RDoLIDa6Lk";
//        if (apiKey == null || apiKey.isEmpty()) {
//            throw new RuntimeException("GEMINI_API_KEY is not set in environment variables!");
//        }
//
//        return ChatClient.builder()
//                .apiKey(apiKey)                     // Gemini API key
//                .model("gemini-2.5-flash")          // Gemini model
//                .temperature(0.7)                   // Creativity
//                .maxTokens(500)                     // Max response length
//                .build();
//    }
//}
