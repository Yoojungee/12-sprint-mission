package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageRepository {
    Message save(Message message); // 등록
    Optional<Message> findById(UUID id); // 조회(단건)
    List<Message> findAll(); // 조회(다건)
    Message update(Message message); // 수정
    void deleteById(UUID id); // 삭제(단건)
    void deleteAll(); // 삭제(다건)
    boolean existsById(UUID id);
}
