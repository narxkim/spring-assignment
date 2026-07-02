package study.day01_chat_client;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ChatController {

    private final ChatService chatService;


    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @ResponseBody
    @GetMapping("/api/interview")
    public String getInterview(@RequestParam String tech){
        return chatService.gentInterview(tech);
    }

    @ResponseBody
    @GetMapping("/api/faq")
    public String getFaq(@RequestParam String q){
        return chatService.getFaq(q);
    }


}
