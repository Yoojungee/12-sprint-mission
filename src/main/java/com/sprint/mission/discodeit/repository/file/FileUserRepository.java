package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public class FileUserRepository implements UserRepository {
    // 경로(path) 및 확장자 초기화
    private final Path DIRECTORY;
    private final String EXTENSION = ".dat";

    // 디렉토리 생성 및 초기화
    public FileUserRepository() {
        this.DIRECTORY = Path.of(System.getProperty("user.dir"), "file-data-map", "Users");

        if (Files.notExists(DIRECTORY)) {
            try {
                Files.createDirectories(DIRECTORY);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    // 전체 경로(path) 생성
    private Path resolvePath(UUID id) {
        return DIRECTORY.resolve(id + EXTENSION);
    }

    // 등록
    @Override
    public User save(User user) {
        Path path = resolvePath(user.getId());

        try (
                FileOutputStream fos = new FileOutputStream(path.toFile());
                ObjectOutputStream oos = new ObjectOutputStream(fos);
        ) {
            oos.writeObject(user);
        } catch (IOException e) {
            throw new RuntimeException("회원 등록 중 오류가 발생했습니다.", e);
        }
        return user;
    }

    // 조회(단건)
    @Override
    public Optional<User> findById(UUID id) {
        Path path = resolvePath(id);

        if (Files.notExists(path)) {
            return Optional.empty();
        }

        try (
                FileInputStream fis = new FileInputStream(path.toFile());
                ObjectInputStream ois = new ObjectInputStream(fis);
        ) {
            User user = (User) ois.readObject();
            return Optional.ofNullable(user);
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("해당 유저 조회 중 오류가 발생했습니다.", e);
        }
    }

    // 조회(다건)
    @Override
    public List<User> findAll() {
        try (Stream<Path> stream = Files.list(DIRECTORY)) {
            return stream.filter(path -> path.getFileName().toString().endsWith(EXTENSION))
                    .map(path -> {
                        try (
                                FileInputStream fis = new FileInputStream(path.toFile());
                                ObjectInputStream ois = new ObjectInputStream(fis);
                        ) {
                            return (User) ois.readObject();
                        } catch (IOException | ClassNotFoundException e) {
                            throw new RuntimeException("해당 유저 조회 중 오류가 발생했습니다.", e);
                        }
                    })
                    .sorted()
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException("유저 목록을 조회 중 오류가 발생했습니다.", e);
        }
    }

    // 수정
    @Override
    public User update(User user) {
        Path path = resolvePath(user.getId());

        try (
                FileOutputStream fos = new FileOutputStream(path.toFile());
                ObjectOutputStream oos = new ObjectOutputStream(fos);
        ) {
            oos.writeObject(user);
            return user;
        } catch (IOException e) {
            throw new RuntimeException("해당 유저 정보를 수정하는 중 오류가 발생했습니다.", e);
        }
    }

    // 존재 여부(중복 체크)
    @Override
    public boolean existsById(UUID id) {
        Path path = resolvePath(id);
        return Files.exists(path);
    }

    @Override
    public boolean existsByUsername(String username) {
        return findAll().stream().anyMatch(user -> user.getUsername().equals(username));
    }

    @Override
    public boolean existsByNickname(String nickname) {
        return findAll().stream().anyMatch(user -> user.getNickname().equals(nickname));
    }

    @Override
    public boolean existsByEmail(String email) {
        return findAll().stream().anyMatch(user -> user.getEmail().equals(email));
    }

    // 삭제(단건)
    @Override
    public void deleteById(UUID id) {
        Path path = resolvePath(id);

        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new RuntimeException("해당 유저 정보를 삭제하는데 실패했습니다.", e);
        }
    }
}
