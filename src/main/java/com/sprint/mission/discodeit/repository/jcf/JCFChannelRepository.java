package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.util.*;

public class JCFChannelRepository implements ChannelRepository {

    private final Map<UUID, Channel> data;

    public JCFChannelRepository() {
        data = new HashMap<>();
    }

    // 등록
    @Override
    public Channel save(Channel channel) {
        data.put(channel.getId(), channel);
        return channel;
    }

    // 조회(단건)
    @Override
    public Optional<Channel> findById(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    // 조회(다건)
    @Override
    public List<Channel> findAll() {
        return new ArrayList<>(data.values());
    }

    // 수정
    @Override
    public Channel update(Channel channel) {
        return save(channel);
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
}
