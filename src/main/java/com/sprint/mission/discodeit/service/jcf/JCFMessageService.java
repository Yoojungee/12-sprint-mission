package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JCFMessageService implements MessageService {

    private final List<Message> data;

    public JCFMessageService() {
        data = new ArrayList<>();
    }

    // 등록
    @Override
    public Message save(Message message) {
        data.add(message);
        return message;
    }

    // 조회(단건)
    @Override
    public Message findById(UUID id) {
        return data.stream()
                .filter(message -> message.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    // 조회(다건)
    @Override
    public List<Message> findAll() {
        return data;
    }

    // 수정
    @Override
    public Message update(Message message) {
        Message messageUpdate = findById(message.getId());

        if(messageUpdate == null) {
            return null;
        }

        messageUpdate.update(message.getContent());
        return messageUpdate;
    }

    // 삭제(단건)
    @Override
    public void deleteByID(UUID id) {
        data.removeIf(message -> message.getId().equals(id));
    }

    // 삭제(다건)
    @Override
    public void deleteAll() {
        data.clear();
    }

}
