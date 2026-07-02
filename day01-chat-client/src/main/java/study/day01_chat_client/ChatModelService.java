package study.day01_chat_client;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// ChatModel을 사용하는 case
@Service
public class ChatModelService {

    @Autowired
    private ChatModel chatModel;

    public String chat(String message){
        String response = chatModel.call(message);
        return response;
    }

    // 1. 면접 질문 생성 (ChatModel 방식)
    public String getInterview(String tech) {
        String prompt = "IT 기업 면접관처럼 다음 기술 스택 관련 신입용 예상 질문 3개와 출제 의도를 적어줘: " + tech;
        return chatModel.call(prompt);
    }

    // 2. FAQ 답변 생성 (ChatModel 방식)
    public String getFaq(String q) {
        String prompt = "기업 인사담당자처럼 다음 자주 묻는 질문(FAQ)에 대해 정중한 답변 초안을 작성해줘: " + q;
        return chatModel.call(prompt);
    }
}