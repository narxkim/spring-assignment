package com.study.day3advisormemory.controller;

import com.study.day3advisormemory.service.MemoryTravelService;
import com.study.day3advisormemory.service.PersistentChatService;
import com.study.day3advisormemory.service.TravelPlannerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TravelController {

    private final TravelPlannerService travelPlannerService;
    private final MemoryTravelService memoryTravelService;
    private final PersistentChatService persistentChatService;

    public TravelController(TravelPlannerService travelPlannerService, MemoryTravelService memoryTravelService, PersistentChatService persistentChatService) {
        this.travelPlannerService = travelPlannerService;
        this.memoryTravelService = memoryTravelService;
        this.persistentChatService = persistentChatService;
    }

    @GetMapping("/api/travel-chat")
    public String chat(@RequestParam String message){
        return travelPlannerService.chat(message);
    }

    @GetMapping("/api/travel-memory")
    public String chatMemory(@RequestParam String message, @RequestParam String conversationId){
        return memoryTravelService.chat(message, conversationId);
    }

    @GetMapping("/api/travel-persistent")
    public String travelPersistent(@RequestParam String message, @RequestParam String conversationId){
        return persistentChatService.chat(message, conversationId);
    }


}
