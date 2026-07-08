package com.study.day5toolmcp.service;

import com.study.day5toolmcp.mcp.McpToolCatalog;
import com.study.day5toolmcp.tool.BudgetTools;
import com.study.day5toolmcp.tool.LocalAreaTools;
import com.study.day5toolmcp.tool.TravelPreferenceTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.stereotype.Service;

@Service
public class TravelChatService {

    private final ChatClient chatClient;
    private final TravelPreferenceTools travelPreferenceTools;
    private final BudgetTools budgetTools;
    private final LocalAreaTools localAreaTools;
    private final McpToolCatalog mcpToolCatalog;


    public TravelChatService(ChatClient.Builder builder,
                             ChatMemory chatMemory,
                             TravelPreferenceTools travelPreferenceTools,
                             BudgetTools budgetTools,
                             LocalAreaTools localAreaTools, McpToolCatalog mcpToolCatalog) {

        this.travelPreferenceTools = travelPreferenceTools;
        this.budgetTools = budgetTools;
        this.localAreaTools = localAreaTools;
        this.mcpToolCatalog = mcpToolCatalog;

        this.chatClient = builder
                .defaultSystem("""
                        당신은 사용자의 여행을 함께 계획해 주는 AI 여행 플래너입니다.
                
                        사용자의 요청을 분석하여 목적지, 여행 기간, 여행 스타일, 예산, 이동 수단 등을 파악하고,
                        필요한 경우 등록된 Local Tool과 MCP Tool을 적극 활용하여 가장 적합한 여행 일정을 제안하세요.
                
                        [Local Tool]
                        - 여행 스타일 분석
                        - 예산 분석
                        - 지역 추천
                
                        [MCP Tool]
                        - fetch : URL의 웹 페이지 내용을 가져와 참고 자료로 활용
                        - filesystem : 여행 일정을 Markdown(.md) 파일로 저장
                
                        사용자의 질문에 URL이 포함되어 있거나
                        "웹 내용을 참고해줘", "URL을 반영해줘", "최신 정보를 참고해줘"와 같은 요청이 있으면
                        반드시 fetch MCP를 사용하여 웹 페이지 내용을 가져오세요.
                
                        사용자가
                        "파일로 저장해줘", "md 파일로 저장해줘", "markdown으로 저장해줘"와 같은 요청을 하면
                        반드시 filesystem MCP를 사용하여 여행 일정을 저장하세요.
                
                        이전 대화 내용(Chat Memory)이 있다면
                        목적지, 예산, 여행 기간, 여행 스타일 등을 기억하여 후속 질문에 반영하세요.
                
                        응답은 여행 가이드가 사용자에게 이야기하듯 자연스럽게 작성하세요.
                
                        규칙
                        - Markdown 문법(#, ##, ###, *, -, >, ``` )을 사용하지 마세요.
                        - 표(Table)를 사용하지 마세요.
                        - 코드블록을 사용하지 마세요.
                        - 굵게(**) 표시를 사용하지 마세요.
                        - 번호 목록을 사용하지 마세요.
                        - 보고서처럼 작성하지 마세요.
                        - 문단 중심으로 읽기 쉽게 작성하세요.
                        - 필요한 경우 📍 🌊 🍽️ ☕ 🚆 💰 🏨 등의 이모지만 적절히 사용하세요.
                
                        응답 순서는 자연스럽게 다음 내용을 포함하세요.
                        - 여행 조건 요약
                        - 추천 여행 컨셉
                        - Day 1 일정
                        - Day 2 일정
                        - 예상 예산
                        - 이동 팁 및 주의사항
                
                        여행 일정은 실제 여행자가 바로 사용할 수 있도록
                        구체적인 장소와 이동 순서를 포함하여 작성하세요.
                
                        파일은 Markdown(.md)으로 저장하더라도
                        사용자에게 보여주는 응답은 일반 채팅 형태의 자연스러운 텍스트로 작성하세요.
                
                        사용자가 제공한 정보만으로 여행 계획이 가능하다면
                        추가 질문을 하지 말고 바로 여행 일정을 생성하세요.
                        부족한 정보는 합리적으로 가정하여 일정에 반영하세요.
                        """)
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .build();
    }

    public String chat(String chatId, String question){
        return chatClient.prompt()
                .user(question)
                .advisors(spec -> spec.param(ChatMemory.CONVERSATION_ID, chatId))
                .tools(travelPreferenceTools, budgetTools, localAreaTools,
                        (Object[]) mcpToolCatalog.allTools())
                .call().content();
    }
}
