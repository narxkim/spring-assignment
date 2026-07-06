package com.study.day3advisormemory.advisor;

import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.core.Ordered;

public class MaxCharLengthAdvisor implements CallAdvisor {

    public static final String MAX_CHAR_LENGTH = "maxCharLength";

    private final int defaultMaxCharLength;
    private final int order;

    public MaxCharLengthAdvisor() {
        this.defaultMaxCharLength = 700;
        this.order = Ordered.HIGHEST_PRECEDENCE;
    }

    public MaxCharLengthAdvisor(int defaultMaxCharLength, int order) {
        this.defaultMaxCharLength = defaultMaxCharLength;
        this.order = order;
    }

    @Override
    public ChatClientResponse adviseCall(ChatClientRequest request, CallAdvisorChain chain) {
        ChatClientRequest mutatedRequest = augmentPrompt(request);
        return chain.nextCall(mutatedRequest);
    }

    private ChatClientRequest augmentPrompt(ChatClientRequest request) {
        Integer maxCharLength = (Integer) request.context().get(MAX_CHAR_LENGTH);

        int limit = maxCharLength != null
                ? maxCharLength : this.defaultMaxCharLength;

        Prompt augmentedPrompt = request.prompt().augmentUserMessage(message ->
                UserMessage.builder()
                        .text(message.getText() + "\n\n" + limit + "자 이내로 답변해 주세요.")
                        .build()
        );
        return request.mutate()
                .prompt(augmentedPrompt)
                .build();
    }

    @Override
    public String getName() {
        return "MaxCharLengthAdvisor";
    }

    @Override
    public int getOrder() {
        return this.order;
    }
}
