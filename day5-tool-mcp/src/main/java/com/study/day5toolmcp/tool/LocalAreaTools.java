package com.study.day5toolmcp.tool;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LocalAreaTools {

    public record LocalAreaGuide(
            String destination,
            List<String> mainAreas,
            List<String> recommendedPlaces,
            List<String> foodKeywords,
            String routeGuide
    ){}

    @Tool(description = "여행 지역에 따라 주요 권역, 추천 장소, 음식 키워드, 동선 가이드를 반환한다. 지역 예: 부산, 서울, 제주, 경주, 여수")
    public LocalAreaGuide recommendLocalArea(
            @ToolParam(description = "여행 목적지 지역명. 예: 부산, 서울, 제주, 경주, 여수")
            String destination
    ) {
        return switch (destination) {
            case "부산" -> new LocalAreaGuide(
                    "부산",
                    List.of("해운대", "광안리", "서면", "영도", "남포동"),
                    List.of("해운대해수욕장", "광안리해수욕장", "감천문화마을", "흰여울문화마을", "태종대"),
                    List.of("돼지국밥", "밀면", "씨앗호떡", "해산물", "카페거리"),
                    "바다 중심 일정은 해운대·광안리 권역을 묶고, 감성 산책 코스는 영도·흰여울문화마을을 함께 배치한다."
            );

            case "서울" -> new LocalAreaGuide(
                    "서울",
                    List.of("종로", "성수", "홍대", "강남", "한강"),
                    List.of("경복궁", "북촌한옥마을", "성수 카페거리", "한강공원", "남산타워"),
                    List.of("한식", "브런치", "디저트", "카페", "야시장"),
                    "전통 코스는 종로·북촌을 묶고, 감성 카페 코스는 성수·한강을 함께 배치한다."
            );

            case "제주" -> new LocalAreaGuide(
                    "제주",
                    List.of("제주시", "애월", "성산", "서귀포", "중문"),
                    List.of("성산일출봉", "협재해수욕장", "카멜리아힐", "천지연폭포", "오설록"),
                    List.of("흑돼지", "고기국수", "해산물", "감귤 디저트", "카페"),
                    "제주는 이동 거리가 길기 때문에 동쪽, 서쪽, 남쪽 권역을 하루에 무리하게 섞지 않는다."
            );

            case "경주" -> new LocalAreaGuide(
                    "경주",
                    List.of("황리단길", "대릉원", "보문단지", "불국사", "동궁과 월지"),
                    List.of("첨성대", "대릉원", "동궁과 월지", "불국사", "월정교"),
                    List.of("한정식", "황남빵", "카페", "쌈밥", "전통 디저트"),
                    "역사 코스는 대릉원·첨성대·동궁과 월지를 묶고, 감성 코스는 황리단길 중심으로 구성한다."
            );

            case "여수" -> new LocalAreaGuide(
                    "여수",
                    List.of("종포해양공원", "돌산", "오동도", "낭만포차거리", "해상케이블카"),
                    List.of("오동도", "여수해상케이블카", "돌산공원", "향일암", "낭만포차거리"),
                    List.of("게장", "해산물", "갓김치", "장어탕", "카페"),
                    "야경 코스는 해상케이블카·돌산공원·낭만포차거리를 연결하면 좋다."
            );

            default -> new LocalAreaGuide(
                    destination,
                    List.of("중심가", "대표 관광지", "맛집 거리", "카페 거리"),
                    List.of("대표 관광지", "전망 좋은 장소", "지역 시장", "산책 코스"),
                    List.of("지역 대표 음식", "로컬 맛집", "디저트", "카페"),
                    "지역 정보가 부족하므로 대표 관광지, 식사, 휴식 장소를 균형 있게 배치한다."
            );
        };
    }
}
