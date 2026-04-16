package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.*;

public class JCFMessageService implements MessageService {
    private final Map<UUID, Message> data;
    private final Map<UUID, User> userData;
    private final Map<UUID, Channel> channelData;

    public JCFMessageService(Map<UUID, Message> data, Map<UUID, User> userData, Map<UUID, Channel> channelData) {
        this.data = data;
        this.userData = userData;
        this.channelData = channelData;
    }

    // 등록
    @Override
    public Message save(Message message) {
        validateMessage(message);
        data.put(message.getId(), message);
        return message;
    }

    // 유효성 검사 (JCF 버전)
    public void validateMessage(Message message) {
        if (message.getAuthorId() == null) throw new IllegalArgumentException("작성자 계정을 찾을 수 없습니다.");
        if (message.getChannelId() == null) throw new IllegalArgumentException("채널을 찾을 수 없습니다.");
        if (message.getContent() == null || message.getContent().isBlank()) {
            throw new IllegalArgumentException("내용을 입력해주세요.");
        }

        if (!userData.containsKey(message.getAuthorId())) {
            throw new NoSuchElementException("존재하지 않는 작성자입니다.");
        }

        if (!channelData.containsKey(message.getChannelId())) {
            throw new NoSuchElementException("존재하지 않는 채널입니다.");
        }

        if (message.getReceiverId() != null && !userData.containsKey(message.getReceiverId())) {
            throw new NoSuchElementException("존재하지 않는 상대입니다.");
        }
    }

    // 조회(단건)
    @Override
    public Message findById(UUID id) {
        Message message = data.get(id);
        if (message == null) {
            throw new NoSuchElementException("메세지를 불러올 수 없습니다.");
        }
        return message;
    }

    // 조회(다건)
    @Override
    public List<Message> findAll() {
        return new ArrayList<>(data.values());
    }

    // 수정
    @Override
    public Message update(Message message, UUID loginUserId) {
        Message messageUpdate = findById(message.getId());

        if (!messageUpdate.getAuthorId().equals(loginUserId)) {
            throw new IllegalArgumentException("본인만 메세지를 수정할 수 있습니다.");
        }

        messageUpdate.update(message.getContent());
        return messageUpdate;
    }

    // 삭제(단건)
    @Override
    public void deleteById(UUID id, UUID loginUserId) {
        Message messageToDelete = findById(id);

        if (!messageToDelete.getAuthorId().equals(loginUserId)) {
            throw new IllegalArgumentException("본인만 메세지를 삭제할 수 있습니다.");
        }

        data.remove(id);
    }

    // 삭제(다건)
    @Override
    public void deleteAll() {
        data.clear();
    }
}