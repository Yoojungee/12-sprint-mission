package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

public class BasicUserService implements UserService {

    private final UserRepository userRepository;

    public BasicUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 등록
    @Override
    public User save(User user) {
        // 회원 정보 유효성 검사
        validateUsername(user.getUsername());
        validateNickname(user.getNickname());
        validateEmail(user.getEmail());

        return userRepository.save(user);
    }

    // 유효성 검사
    // 아이디(유저 아이디) 유효성 검사
    public void validateUsername(String username) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("아이디는 필수 정보입니다.");
        }

        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException(username + "은(는) 이미 등록된 아이디입니다!");
        }
    }

    // 닉네임 유효성 검사
    public void validateNickname(String nickname) {
        if (nickname == null || nickname.isBlank()) {
            throw new IllegalArgumentException("닉네임은 필수 정보입니다.");
        }

        if (userRepository.existsByNickname(nickname)) {
            throw new IllegalArgumentException(nickname + "은(는) 이미 등록된 닉네임입니다!");
        }
    }

    // 이메일 유효성 검사
    public void validateEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("이메일은 필수 정보입니다.");
        }

        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException(email + "은(는) 이미 등록된 이메일입니다!");
        }
    }

    // 조회(단건)
    @Override
    public User findById(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new NoSuchElementException("해당하는 유저가 없습니다!"));
    }

    // 조회(다건)
    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    // 수정
    @Override
    public User update(User user, UUID loginUserId) {
        User userToUpdate = findById(user.getId());

        if (!userToUpdate.getId().equals(loginUserId)) {
            throw new IllegalArgumentException("계정 본인만 수정할 수 있습니다!");
        }

        userToUpdate.update(
                user.getUsername(),
                user.getNickname(),
                user.getEmail(),
                user.getPassword()
        );

        return userRepository.save(userToUpdate);
    }

    // 삭제(단건)
    @Override
    public void deleteById(UUID id, UUID loginUserId) {
        User userToDelete = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("삭제할 유저를 찾을 수 없습니다."));

        if (!userToDelete.getId().equals(loginUserId)) {
            throw new IllegalArgumentException("계정 본인만 삭제할 수 있습니다.");
        }

        userRepository.deleteById(id);
    }

}
