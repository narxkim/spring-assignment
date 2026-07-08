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
                
                        이전 대화(Chat Memory)가 있다면
                        목적지, 예산, 여행 기간, 여행 스타일 등을 기억하여 후속 질문에 반영하세요.
                
                        사용자가 제공한 정보만으로 여행 계획이 가능하다면
                        추가 질문하지 말고 바로 여행 일정을 생성하세요.
                
                        정보가 조금 부족하더라도 합리적으로 가정하여 일정을 작성하세요.
                
                        응답은 Markdown 형식으로 작성하세요.
                
                        작성 규칙
                        - ## 제목을 사용하세요.
                        - 필요한 경우 ### 소제목을 사용하세요.
                        - 중요한 내용은 **굵게** 표시하세요.
                        - 목록은 -, * 를 자유롭게 사용하세요.
                        - 표는 필요한 경우만 사용하세요.
                        - 코드블록은 사용하지 마세요.
                        - 읽기 쉽도록 문단을 적절히 나누세요.
                        - 필요한 경우 📍 🌊 🍽️ ☕ 🚆 💰 🏨 💾 등의 이모지를 사용할 수 있습니다.
                
                        응답은 아래 순서를 따르세요.
                
                        ## 여행 조건
                        ## 추천 여행 컨셉
                        ## Day 1
                        ## Day 2
                        ## 예상 예산
                        ## 이동 팁
                
                        파일을 저장한 경우 마지막에
                
                        💾 여행 일정이 '파일명.md'로 저장되었습니다.
                
                        를 추가하세요.
                
                        너무 딱딱한 보고서처럼 작성하지 말고,
                        여행 가이드가 사용자에게 친절하게 설명하는 말투를 유지하세요.
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
