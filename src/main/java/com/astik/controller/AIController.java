package com.astik.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.astik.service.OnlineBankingAssistant;

@RestController
@RequestMapping("/ai-chat")
public class AIController {

    private final OnlineBankingAssistant assistant;

    public AIController(OnlineBankingAssistant assistant) {
        this.assistant = assistant;
    }

    @GetMapping("/help")
    public String askQuestion(@RequestParam String query) {
        if (query == null || query.isBlank()) {
            return "Please provide a valid banking question!";
        }
        System.out.println("Incoming query: " + query);
        String response = assistant.getAnswer(query);
        System.out.println("Gemini response: " + response);
        return response != null ? response : "No response from AI";
    }

}
