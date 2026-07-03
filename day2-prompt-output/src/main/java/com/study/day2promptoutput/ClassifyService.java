package com.study.day2promptoutput;

import com.study.day2promptoutput.dto.MenuRecommendation;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class ClassifyService {

    private final ChatClient chatClient;

    private static final String MENU_TEMPLATE = """
        당신은 점심 메뉴 추천 전문가입니다.

        아래 정보를 참고하여 점심 메뉴를 추천하세요.

        - 오늘 기분 : {mood}
        - 날씨 : {weather}
        - 예산 : {budget}
        - 맵기 선호 : {spicy}
        - 혼밥 여부 : {alone}

        반드시 아래 필드만 포함하여 응답하세요.

        - menu : 추천 메뉴
        - category : 한식, 중식, 일식, 양식, 분식 중 하나
        - priceRange : 예상 가격대
        - reason : 추천 이유를 한 문장으로 작성
        - confidence : 반드시 HIGH, MEDIUM, LOW 중 하나만 작성

        메뉴 이름은 하나만 추천하세요.
        추가 설명이나 인사말은 포함하지 마세요.
        """;


    public ClassifyService(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    public MenuRecommendation classify(String mood,
                           String weather,
                           String budget,
                           String spicy,
                           String alone){
        return chatClient.prompt()
                .user(u -> u.text(MENU_TEMPLATE)
                        .param("mood", mood)
                        .param("weather", weather)
                        .param("budget", budget)
                        .param("spicy", spicy)
                        .param("alone", alone))
                .call().entity(MenuRecommendation.class);
    }

}
