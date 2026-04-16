package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

public class FileChannelService implements ChannelService {

    private final ChannelRepository channelRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    public FileChannelService(ChannelRepository channelRepository, MessageRepository messageRepository, UserRepository userRepository) {
        this.channelRepository = channelRepository;
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
    }

    // 등록
    @Override
    public Channel save(Channel channel) {
        userRepository.findById(channel.getOwnerId());
        return channelRepository.save(channel);
    }

    // 조회(단건)
    @Override
    public Channel findById(UUID id) {
        return channelRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당 채널을 찾을 수 없습니다."));
    }

    // 조회(다건)
    @Override
    public List<Channel> findAll() {
        return channelRepository.findAll();
    }

    // 수정
    @Override
    public Channel update(Channel channel, UUID loginUserId) {
        Channel channelToUpdate = findById(channel.getId());

        if (!channelToUpdate.getOwnerId().equals(loginUserId)) {
            throw new IllegalArgumentException("방장만 채널을 수정할 수 있습니다.");
        }

        channelToUpdate.update(
                channel.getChannelName(),
                channel.getChannelType(),
                channel.getDescription()
        );

        return channelRepository.update(channelToUpdate);
    }

    // 삭제(단건)
    @Override
    public void deleteById(UUID id, UUID loginUserId) {
        Channel channelToDelete = findById(id);

        if (!channelToDelete.getId().equals(loginUserId)) {
            throw new IllegalArgumentException("본인만 계정 삭제가 가능합니다.");
        }

        channelRepository.deleteById(id);
    }
}
