package com.study.day5toolmcp.tool;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TravelPreferenceTools {

    public record TravelPreference(
            String style,
            List<String> recommendedCategories,
            String travelConcept,
            String planningGuide
    ){}

    @Tool(description = "여행 스타일을 분석해 추천 카테고리, 여행 콘셉트, 일정 구성 가이드를 반환한다. " +
            "스타일 예: 힐링, 맛집, 액티비티, 가족, 커플, 혼자")
    public TravelPreference recommendTravelPreference(
            @ToolParam(description = "사용자가 원하는 여행 스타일. 예: 힐링, 맛집, 액티비티, 가족, 커플, 혼자") String style
    ){
        return switch (style) {
            case "힐링" -> new TravelPreference("힐링", List.of(" 바다", "카페", "산책", "야경"),
                    "여유롭고 조용한 회복 중심 여행",
                    "이동 거리를 짧게 잡고, 바다나 공원처럼 오래 머물 수 있는 장소를 중심으로 구성한다");
            case "맛집" -> new TravelPreference(
                    "맛집",
                    List.of("시장", "로컬 식당", "디저트", "카페"),
                    "지역 음식 중심 미식 여행",
                    "식사 시간을 기준으로 동선을 짜고, 관광지는 식당 주변의 짧은 코스로 배치한다."
            );

            case "액티비티" -> new TravelPreference(
                    "액티비티",
                    List.of("체험", "전망대", "해양 스포츠", "야외 활동"),
                    "활동량이 많은 체험 중심 여행",
                    "오전에는 이동과 체험을 배치하고, 저녁에는 피로도를 고려해 휴식 코스를 넣는다."
            );

            case "가족" -> new TravelPreference(
                    "가족",
                    List.of("공원", "실내 관광지", "체험관", "편한 식당"),
                    "이동 부담이 적은 안정형 여행",
                    "대기 시간이 긴 장소는 피하고, 식사와 휴식 장소를 일정 중간에 충분히 넣는다."
            );

            case "커플" -> new TravelPreference(
                    "커플",
                    List.of("야경", "사진 명소", "감성 카페", "분위기 좋은 식당"),
                    "분위기와 추억 중심 감성 여행",
                    "사진 찍기 좋은 장소와 야경 코스를 중심으로 구성하고, 저녁 시간을 여유롭게 둔다."
            );

            case "혼자" -> new TravelPreference(
                    "혼자",
                    List.of("카페", "서점", "산책", "전망 좋은 장소"),
                    "혼자 머물기 편한 자유 여행",
                    "예약이 복잡한 장소보다 즉흥적으로 이동 가능한 장소를 중심으로 구성한다."
            );

            default -> new TravelPreference(style, List.of("대표 관광지", "맛집", "카페", "산책"),
                    "균형형 기본여행",
                    "사용자의 스타일이 명확하지 않으므로 관광, 식사, 휴식을 균형 있게 배치한다.");
        };
    }
}
