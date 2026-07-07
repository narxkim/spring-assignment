package com.study.day4multimodal.dto;

import java.util.List;

public record LectureSummary(
        String summary,
        List<String> concept,
        List<String> keywords,
        List<String> questions
) {
}
