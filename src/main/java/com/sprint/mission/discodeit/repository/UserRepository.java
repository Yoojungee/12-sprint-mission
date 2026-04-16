package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    User save(User user); // 등록
    Optional<User> findById(UUID id); // 조회(단건)
    List<User> findAll(); // 조회(다건)
    User update(User user); // 수정
    void deleteById(UUID id); // 삭제(단건)
    boolean existsById(UUID id);
    boolean existsByUsername(String username);
    boolean existsByNickname(String nickname);
    boolean existsByEmail(String email);
}
