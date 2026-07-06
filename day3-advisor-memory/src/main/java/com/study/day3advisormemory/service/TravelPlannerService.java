package com.study.day3advisormemory.service;

import com.study.day3advisormemory.advisor.CallCounterAdvisor;
import com.study.day3advisormemory.advisor.MaxCharLengthAdvisor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SafeGuardAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TravelPlannerService {

    private final ChatClient chatClient;


    public TravelPlannerService(ChatClient.Builder builder,
                                CallCounterAdvisor callCounterAdvisor) {
        this.chatClient = builder
                .defaultSystem("""
                        당신은 전문 AI 여행 플래너 입니다.
                        
                        사용자의 여행지. 여행 기간, 예산, 이동수단, 동행인원,
                        여행 스타일을 고려하여 현실적인 여행 일정을 추천하세요.
                        
                        답변은 다음 형식을 따르세요
                        
                        📍 여행 요약
                        🗓️ 추천 일정
                        🍽️ 추천 맛집
                        💰 예상 비용
                        💡 여행 팁
                        
                        답변은 친절하고 간결한 한국어로 작성하세요.
                        
                        """)
                .defaultAdvisors(
                        new MaxCharLengthAdvisor(),
                        callCounterAdvisor,
                        new SafeGuardAdvisor(List.of("폭탄", "테러", "마약", "계좌번호"),
                                "해당 요청은 안전 정책에 따라 답변할 수 없습니다.",
                                Ordered.HIGHEST_PRECEDENCE),
                        new SimpleLoggerAdvisor()).build();
    }

    public String chat(String message){
        return chatClient.prompt()
                .user(message)
                .call().content();
    }
}
