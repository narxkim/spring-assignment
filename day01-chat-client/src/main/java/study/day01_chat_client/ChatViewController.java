package study.day01_chat_client;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ChatViewController {

    @GetMapping("/chat")
    public String chatView() {
        return "chat";
    }

    // 면접 질문 생성기 화면 매핑 (templates/interview.html)
    @GetMapping("/interview")
    public String interviewView() {
        return "interview";
    }

    // 기업 FAQ 응답기 화면 매핑 (templates/faq.html)
    @GetMapping("/faq")
    public String faqView() {
        return "faq";
    }
}