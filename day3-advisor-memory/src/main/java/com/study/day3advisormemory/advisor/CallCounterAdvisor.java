package com.study.day3advisormemory.advisor;

import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class CallCounterAdvisor implements CallAdvisor  {

    private final AtomicInteger callCount = new AtomicInteger(0);

    @Override
    public ChatClientResponse adviseCall(ChatClientRequest request, CallAdvisorChain chain) {

        ChatClientResponse response = chain.nextCall(request);
        int count = callCount.incrementAndGet();
        System.out.println("LLM 호출 횟수 : " + count);
        return response;
    }

    @Override
    public String getName() {
        return "CallCounterAdvisor";
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE -1 ;
    }

    public int getCallCount(){
        return callCount.get();
    }
}
