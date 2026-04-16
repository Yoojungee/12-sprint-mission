package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.util.*;

public class JCFMessageRepository implements MessageRepository {

    private final Map<UUID, Message> data;

    public JCFMessageRepository() {
        data = new HashMap<>();
    }

    // 등록
    @Override
    public Message save(Message message) {
        data.put(message.getId(), message);
        return message;
    }

    // 조회(단건)
    @Override
    public Optional<Message> findById(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    // 조회(다건)
    @Override
    public List<Message> findAll() {
        return new ArrayList<>(data.values());
    }

    // 수정
    @Override
    public Message update(Message message) {
        return save(message);
    }

    // 존재 여부
    @Override
    public boolean existsById(UUID id) {
        return data.containsKey(id);
    }

    // 삭제(단건)
    @Override
    public void deleteById(UUID id) {
        data.remove(id);
    }

    // 삭제(다건)
    @Override
    public void deleteAll() {
        data.clear();
    }
}
