package com.study.day5toolmcp.tool;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BudgetTools {

    public record BudgetGuide(
            int budget,
            String budgetLevel,
            List<String> accommodationGuide,
            List<String> mealGuide,
            List<String> transportGuide,
            String planningGuide
    ){}

    @Tool(description = "여행 예산을 분석해 예산 수준, 숙소, 식사, 교통, 일정 구성 가이드를 반환한다. 예산은 원 단위 숫자다.")
    public BudgetGuide analyzeTravelBudget(
            @ToolParam(description = "여행 전체 예산. 원 단위 숫자. 예: 150000")
            int budget
    ) {
        if (budget < 100000) {
            return new BudgetGuide(
                    budget,
                    "절약형",
                    List.of("게스트하우스", "캡슐호텔", "저가 비즈니스호텔"),
                    List.of("시장 음식", "분식", "로컬 가성비 식당"),
                    List.of("도보", "버스", "지하철"),
                    "유료 관광지를 줄이고 무료 산책 코스와 가성비 식당 중심으로 일정을 구성한다."
            );
        }

        if (budget < 250000) {
            return new BudgetGuide(
                    budget,
                    "균형형",
                    List.of("비즈니스호텔", "가성비 호텔", "위치 좋은 숙소"),
                    List.of("로컬 맛집", "카페", "대표 음식 1~2회"),
                    List.of("대중교통", "필요 시 택시 1~2회"),
                    "숙소 위치를 중심지에 잡고, 식사와 관광을 균형 있게 배치한다."
            );
        }

        if (budget < 500000) {
            return new BudgetGuide(
                    budget,
                    "여유형",
                    List.of("오션뷰 호텔", "부티크 호텔", "감성 숙소"),
                    List.of("인기 맛집", "디저트 카페", "분위기 좋은 식당"),
                    List.of("택시", "렌터카", "대중교통 병행"),
                    "이동 피로를 줄이고 숙소, 식사, 관광 경험의 만족도를 높이는 방향으로 구성한다."
            );
        }

        return new BudgetGuide(
                budget,
                "프리미엄형",
                List.of("고급 호텔", "리조트", "프리미엄 숙소"),
                List.of("파인다이닝", "오마카세", "프리미엄 카페"),
                List.of("렌터카", "택시", "프라이빗 이동"),
                "이동 효율과 경험의 질을 우선하고, 예약이 필요한 식당이나 체험을 포함한다."
        );
    }
}
