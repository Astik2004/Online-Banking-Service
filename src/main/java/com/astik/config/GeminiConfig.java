package com.astik.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.astik.service.OnlineBankingAssistant;

import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import dev.langchain4j.service.AiServices;

@Configuration
public class GeminiConfig {

    @Value("${GEMINI_API_KEY}")  // read from application.properties
    private String geminiApiKey;

    @Bean
    public OnlineBankingAssistant bankingAssistant() {
        if (geminiApiKey == null || geminiApiKey.isBlank()) {
            throw new IllegalArgumentException("Gemini API key is missing!");
        }
        System.out.println(geminiApiKey);
        GoogleAiGeminiChatModel model = GoogleAiGeminiChatModel.builder()
                .apiKey(geminiApiKey) // use the property value here
                .modelName("gemini-2.5-pro")
                .temperature(0.7)
                .maxOutputTokens(500)
                .build();

        return AiServices.builder(OnlineBankingAssistant.class)
                .chatModel(model)
                .chatMemory(MessageWindowChatMemory.withMaxMessages(5))
                .build();
    }
}
