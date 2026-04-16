package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

public class BasicMessageService implements MessageService {

    private final MessageRepository messageRepository;
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;

    public BasicMessageService(MessageRepository messageRepository, ChannelRepository channelRepository, UserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.channelRepository = channelRepository;
        this.userRepository = userRepository;
    }

    // 등록
    @Override
    public Message save(Message message) {
        validateMessage(message);
        return messageRepository.save(message);
    }

    // 유효성 검사
    public void validateMessage(Message message) {
        if (message.getAuthorId() == null) throw new IllegalArgumentException("작성자 정보가 누락되었습니다.");

        if (!userRepository.existsById(message.getAuthorId())) {
            throw new NoSuchElementException("유효하지 않은 작성자 계정입니다.");
        }

        if (message.getContent() == null || message.getContent().isBlank()) {
            throw new IllegalArgumentException("내용을 입력해주세요.");
        }

        if (message.getChannelId() == null || !channelRepository.existsById(message.getChannelId())) {
            throw new NoSuchElementException("존재하지 않는 채널입니다.");
        }

        if (message.getReceiverId() != null && !userRepository.existsById(message.getReceiverId())) {
            throw new NoSuchElementException("존재하지 않는 수신자 계정입니다.");
        }
    }

    // 조회(단건)
    @Override
    public Message findById(UUID id) {
        return messageRepository.findById(id).orElseThrow(() -> new NoSuchElementException("메세지를 불러올 수 없습니다."));
    }

    // 조회(다건)
    @Override
    public List<Message> findAll() {
        return messageRepository.findAll();
    }

    // 수정
    @Override
    public Message update(Message message, UUID loginUserId) {
        Message messageUpdate = findById(message.getId());

        if (!messageUpdate.getAuthorId().equals(loginUserId)) {
            throw new IllegalArgumentException("본인만 메세지를 수정할 수 있습니다.");
        }

        if (message.getContent() == null || message.getContent().isBlank()) {
            throw new IllegalArgumentException("수정할 내용을 입력해주세요.");
        }

        messageUpdate.update(message.getContent());

        return messageRepository.update(messageUpdate);
    }

    // 삭제(단건)
    @Override
    public void deleteById(UUID id, UUID loginUserId) {
        Message messageToDelete = findById(id);

        if (!messageToDelete.getAuthorId().equals(loginUserId)) {
            throw new IllegalArgumentException("본인만 메세지를 삭제할 수 있습니다.");
        }

        messageRepository.deleteById(id);
    }

    // 삭제(다건)
    @Override
    public void deleteAll() {
        messageRepository.deleteAll();
    }
}
