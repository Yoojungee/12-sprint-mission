package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;

    public BasicChannelService(ChannelRepository channelRepository, UserRepository userRepository) {
        this.channelRepository = channelRepository;
        this.userRepository = userRepository;
    }

    // 등록
    @Override
    public Channel save(Channel channel) {
        validateChannel(channel);
        return channelRepository.save(channel);
    }

    // 유효성 검사
    public void validateChannel(Channel channel) {
        if (channel.getChannelName() == null || channel.getChannelName().isBlank()) {
            throw new IllegalArgumentException("채널 이름을 입력해주세요.");
        }

        if (channel.getOwnerId() == null) {
            throw new IllegalArgumentException("방장 정보가 누락되었습니다.");
        }

        if (channel.getChannelType() == null) {
            throw new IllegalArgumentException("채널 타입을 선택해주세요.");
        }

        if (channel.getMemberIds() == null || !channel.getMemberIds().contains(channel.getOwnerId())) {
            throw new IllegalArgumentException("방장은 반드시 채널의 멤버에 포함되어야 합니다.");
        }

        for (UUID memberId : channel.getMemberIds()) {
            if (!userRepository.existsById(memberId)) {
                if (memberId.equals(channel.getOwnerId())) {
                    throw new NoSuchElementException("방장이 존재하지 않습니다.");
                }
                throw new NoSuchElementException("존재하지 않는 유저(ID: " + memberId + ")가 멤버에 포함되어 있습니다!");
            }
        }
    }

    // 조회(단건)
    @Override
    public Channel findById(UUID id) {
        return channelRepository.findById(id).orElseThrow(() -> new NoSuchElementException("채널을 불러올 수 없습니다."));
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

        if (!channelToDelete.getOwnerId().equals(loginUserId)) {
            throw new IllegalArgumentException("방장만 채널을 삭제할 수 있습니다.");
        }

        channelRepository.deleteById(id);
    }
}
