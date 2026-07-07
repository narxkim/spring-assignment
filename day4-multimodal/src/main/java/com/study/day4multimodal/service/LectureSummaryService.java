package com.study.day4multimodal.service;

import com.study.day4multimodal.dto.LectureSummary;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeType;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@Service
public class LectureSummaryService {

    private final ChatClient chatClient;

    private static final String ALLOWED_PDF_TYPE = "application/pdf";


    public LectureSummaryService(ChatClient.Builder builder, ChatMemory chatMemory) {
        this.chatClient = builder
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build()).build();
    }

    public LectureSummary summarizeLecture(MultipartFile file, String conversationId){
        validatePdf(file);
        ByteArrayResource resource = toResource(file);
        MimeType mimeType = new MimeType("application", "pdf");
        String prompt = """
                당신은 강의자료를 분석하는 AI입니다.
                업로드된 PDF를 읽고 아래 형식으로 정리하세요.
    
                1. 강의 전체 요약 (3~5줄)
                2. 핵심 개념 5개
                3. 핵심 키워드 5개
                4. 예상 질문 3개
    
                반드시 한국어로 작성하세요.
                """;

        return chatClient.prompt()
                .user(u -> u.text(prompt)
                        .media(mimeType, resource))
                .advisors(spec -> spec.param(ChatMemory.CONVERSATION_ID, conversationId))
                .call()
                .entity(LectureSummary.class);
    }

    private ByteArrayResource toResource(MultipartFile file){
        try {
            return new ByteArrayResource(file.getBytes());
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "파일을 읽는 중 오류가 발생했습니다.", e);
        }
    }

    private void validatePdf(MultipartFile file) {

        // 파일이 없거나 비어있는 경우
        if (file == null || file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "PDF 파일을 업로드 해주세요."
            );
        }

        // PDF 파일인지 확인
        if (!ALLOWED_PDF_TYPE.equals(file.getContentType())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "PDF 파일만 업로드할 수 있습니다. (받은 타입 :" + file.getContentType() + ")");
        }
    }
}
