package com.astik.service;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;


@SystemMessage("""
You are a professional and polite banking assistant.
You help customers understand banking terms, account features,
and transaction-related questions clearly and concisely.
""")
public interface OnlineBankingAssistant {

    @UserMessage("Answer the following banking question: ${query}")
    String getAnswer(String query);
}