package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JCFChannelService implements ChannelService {

    private final List<Channel> data;

    public JCFChannelService() {
        data = new ArrayList<>();
    }

    // 등록
    @Override
    public Channel save(Channel channel) {
        data.add(channel);
        return channel;
    }

    // 조회(단건)
    @Override
    public Channel findById(UUID id) {
        return data.stream()
                    .filter(channel -> channel.getId().equals(id))
                    .findFirst()
                    .orElse(null);
    }

    // 조회(다건)
    @Override
    public List<Channel> findAll() {
        return data;
    }

    // 수정
    @Override
    public Channel update(Channel channel) {
        Channel channelToUpdate = findById(channel.getId());

        if(channelToUpdate == null) {
            return null;
        }

        channelToUpdate.update(
                channel.getChannelName(),
                channel.getChannelType(),
                channel.getDescription()
        );
        return channelToUpdate;
    }

    // 삭제(단건)
    @Override
    public void deleteById(UUID id) {
        data.removeIf(channel -> channel.getId().equals(id));
    }
}
