package com.study.day2promptoutput.dto;

public record MenuRecommendation(
        String menu,
        String category,
        String priceRange,
        String reason,
        String confidence
) {
}
