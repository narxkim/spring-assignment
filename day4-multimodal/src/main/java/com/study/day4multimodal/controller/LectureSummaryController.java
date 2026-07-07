package com.study.day4multimodal.controller;

import com.study.day4multimodal.dto.LectureSummary;
import com.study.day4multimodal.service.LectureSummaryService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class LectureSummaryController {

    private final LectureSummaryService lectureSummaryService;

    public LectureSummaryController(LectureSummaryService lectureSummaryService) {
        this.lectureSummaryService = lectureSummaryService;
    }

    @GetMapping("/")
    public String home(){
        return "index";
    }

    @PostMapping("/api/lecture-summary")
    @ResponseBody
    public LectureSummary summarizeLecture(@RequestParam("file")MultipartFile file,
                                           @RequestParam(value = "conversationId", defaultValue = "lecture-summary")
                                           String conversationId){
        return lectureSummaryService.summarizeLecture(file, conversationId);
    }


}
