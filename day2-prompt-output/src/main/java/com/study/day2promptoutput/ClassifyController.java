package com.study.day2promptoutput;


import com.study.day2promptoutput.dto.MenuRecommendation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClassifyController {

    private final ClassifyService classifyService;

    public ClassifyController(ClassifyService classifyService) {
        this.classifyService = classifyService;
    }

    @GetMapping("/api/menu")
    public MenuRecommendation menu(
            @RequestParam String mood,
            @RequestParam String weather,
            @RequestParam String budget,
            @RequestParam String spicy,
            @RequestParam String alone
    ){
        return classifyService.classify(mood, weather, budget, spicy, alone);
    }
}
