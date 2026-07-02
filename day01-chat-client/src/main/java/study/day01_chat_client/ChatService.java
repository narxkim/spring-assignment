package study.day01_chat_client;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

@Service
public class ChatService {

    private final ChatClient chatClient;

    public ChatService(ChatClient.Builder builder ) {
        this.chatClient = builder.build();
    }

    public String gentInterview(String tech) {
        return chatClient.prompt()
                .system("""
                        너는 IT 기업 면접관이야.
                        입력된 기술 스택 관련 신입용 예상 질문 3개와
                        출제 의도를 리스트로 적어줘.
                        """)
                .user(tech)
                .call()
                .content();
    }

    public String getFaq(String q){
        return chatClient.prompt()
                .system("""
                        너는 인사담당자야.
                        입력된 자주 묻는 질문(FAQ)에 대해
                        정중한 비즈니스 톤의 답변 초안을 작성해줘.
                        """)
                .user(q)
                .call()
                .content();
    }
}
