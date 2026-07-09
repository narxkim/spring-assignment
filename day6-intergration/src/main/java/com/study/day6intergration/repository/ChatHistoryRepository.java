package com.study.day6intergration.repository;

import com.study.day6intergration.entity.ChatHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatHistoryRepository extends JpaRepository<ChatHistoryEntity, Long> {

    List<ChatHistoryEntity> findByConversationIdOrderByIdAsc(String conversationId);
}
