package com.study.day3advisormemory.service;

import org.springframework.stereotype.Service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Qualifier;


@Service
public class PersistentChatService {

    private final ChatClient chatClient;

    public PersistentChatService(ChatClient.Builder builder,
                                 @Qualifier("jdbcChatMemory") ChatMemory chatMemory) {
        this.chatClient = builder
                .defaultSystem("""
                        당신은 전문 AI 여행 플래너입니다.
                        
                        사용자의 여행지, 여행 기간, 예산, 이동수단,
                        동행 인원, 여행 스타일을 기억하며
                        이전 대화를 기반으로 여행 계획을 수정하고 추천하세요.
                        
                        답변은 친절하고 간결한 한국어로 작성하세요.
                        """)
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .build();
    }

    public String chat(String message, String conversationId) {
        return chatClient.prompt()
                .user(message)
                .advisors(spec -> spec.param(ChatMemory.CONVERSATION_ID,
                        conversationId
                ))
                .call()
                .content();
    }
}
