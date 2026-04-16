package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChannelRepository {
    Channel save(Channel channel); // 등록
    Optional<Channel> findById(UUID id); // 조회(단건)
    List<Channel> findAll(); // 조회(다건)
    Channel update(Channel channel); // 수정
    void deleteById(UUID id); // 삭제(단건)
    boolean existsById(UUID id);
}
