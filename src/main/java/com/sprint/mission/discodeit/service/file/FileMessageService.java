package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

public class FileMessageService implements MessageService {
    private final MessageRepository messageRepository;
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;

    public FileMessageService(MessageRepository messageRepository, ChannelRepository channelRepository, UserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.channelRepository = channelRepository;
        this.userRepository = userRepository;
    }

    // 등록
    @Override
    public Message save(Message message) {
        userRepository.findById(message.getAuthorId());
        channelRepository.findById(message.getChannelId());
        return messageRepository.save(message);
    }

    // 조회(단건)
    @Override
    public Message findById(UUID id) {
        return messageRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당 메세지를 찾을 수 없습니다."));
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
