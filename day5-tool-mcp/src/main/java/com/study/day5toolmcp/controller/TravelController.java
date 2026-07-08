package com.study.day5toolmcp.controller;

import com.study.day5toolmcp.service.TravelChatService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class TravelController {

    private final TravelChatService travelChatService;

    public TravelController(TravelChatService travelChatService) {
        this.travelChatService = travelChatService;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @ResponseBody
    @GetMapping("/api/travel/chat")
    public String chat(
            @RequestParam(defaultValue = "chat-1") String chatId,
            @RequestParam String question
    ) {
        return travelChatService.chat(chatId, question);
    }
}