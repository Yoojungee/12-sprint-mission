package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.util.*;

public class JCFUserRepository implements UserRepository {

    private final Map<UUID, User> data;

    public JCFUserRepository() {
        data = new HashMap<>();
    }

    // 등록
    @Override
    public User save(User user) {
        data.put(user.getId(), user);
        return user;
    }

    // 조회(단건)
    @Override
    public Optional<User> findById(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    // 조회(다건)
    @Override
    public List<User> findAll() {
        return new ArrayList<>(data.values());
    }

    // 수정
    @Override
    public User update(User user) {
        return save(user);
    }

    // 존재 여부(유효성 검사)
    @Override
    public boolean existsById(UUID id) {
        return data.containsKey(id);
    }

    // 존재 여부(유효성 검사) - 유저아이디
    @Override
    public boolean existsByUsername(String username) {
        return findAll().stream()
                .anyMatch(user -> user.getUsername().equals(username));
    }

    // 존재 여부(유효성 검사) - 닉네임
    @Override
    public boolean existsByNickname(String nickname) {
        return findAll().stream()
                .anyMatch(user -> user.getNickname().equals(nickname));
    }

    // 존재 여부(유효성 검사) - 이메일
    @Override
    public boolean existsByEmail(String email) {
        return findAll().stream()
                .anyMatch(user -> user.getEmail().equals(email));
    }

    // 삭제(단건)
    @Override
    public void deleteById(UUID id) {
        data.remove(id);
    }
}
