package org.example.backend.chat;


import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;


public interface MessageRepository extends JpaRepository<Message, Long> {
    // 메시지 내용을 검색하는 쿼리 메서드
    List<Message> findByContentIgnoreCaseContainingAndTimestampAfter(String content, LocalDateTime timestamp);
}