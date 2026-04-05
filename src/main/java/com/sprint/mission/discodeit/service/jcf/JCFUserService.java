package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JCFUserService implements UserService {

    private final List<User> data;

    public JCFUserService() {
        data = new ArrayList<>();
    }

    // 등록
    @Override
    public User save(User user) {
        data.add(user);
        return user;
    }

    // 조회(단건)
    @Override
    public User findById(UUID id) {
      return data.stream()
            .filter(user -> user.getId().equals(id))
            .findFirst()
            .orElse(null);
    }

    // 조회(다건)
    @Override
    public List<User> findAll() {
        return data;
    }

    // 수정
    @Override
    public User update(User user) {
        User userToUpdate = findById(user.getId());

        if(userToUpdate == null) {
            return null;
        }

        userToUpdate.update(
            user.getUsername(),
            user.getEmail(),
            user.getPassword(),
            user.getNickname()
        );
        return userToUpdate;
    }

    // 삭제(단건)
    @Override
    public void deleteByID(UUID id) {
        data.removeIf(user -> user.getId().equals(id));
    }
}