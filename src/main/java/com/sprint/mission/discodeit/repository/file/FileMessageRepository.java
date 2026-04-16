package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public class FileMessageRepository implements MessageRepository {
    // 경로(path) 및 확장자 초기화
    private final Path DIRECTORY;
    private final String EXTENSION = ".dat";

    // 디렉토리 생성 및 초기화
    public FileMessageRepository() {
        this.DIRECTORY = Path.of(System.getProperty("user.dir"), "file-data-map", "Messages");

        if (Files.notExists(DIRECTORY)) {
            try {
                Files.createDirectories(DIRECTORY);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private Path resolvePath(UUID id) {
        return DIRECTORY.resolve(id + EXTENSION);
    }

    // 등록
    @Override
    public Message save(Message message) {
        Path path = resolvePath(message.getId());

        try (
                FileOutputStream fos = new FileOutputStream(path.toFile());
                ObjectOutputStream oos = new ObjectOutputStream(fos);
        ) {
            oos.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException("메세지를 등록하는 중 오류가 발생했습니다.", e);
        }
        return message;
    }

    // 조회(단건)
    @Override
    public Optional<Message> findById(UUID id) {
        Path path = resolvePath(id);

        if (Files.notExists(path)) {
            return Optional.empty();
        }

        try (
                FileInputStream fis = new FileInputStream(path.toFile());
                ObjectInputStream ois = new ObjectInputStream(fis);
        ) {
            Message message = (Message) ois.readObject();
            return Optional.ofNullable(message);
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("해당 메세지를 조회하는 중 오류가 발생했습니다.", e);
        }
    }

    // 조회(다건)
    @Override
    public List<Message> findAll() {
        try (Stream<Path> stream = Files.list(DIRECTORY)) {
            return stream.filter(path -> path.getFileName().toString().endsWith(EXTENSION))
                    .map(path -> {
                        try (
                                FileInputStream fis = new FileInputStream(path.toFile());
                                ObjectInputStream ois = new ObjectInputStream(fis);
                        ) {
                            return (Message) ois.readObject();
                        } catch (IOException | ClassNotFoundException e) {
                            throw new RuntimeException("메세지를 조회하는 중 오류가 발생했습니다.", e);
                        }
                    })
                    .sorted()
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException("메세지 목록을 조회하는 중 오류가 발생했습니다.", e);
        }
    }

    // 수정
    @Override
    public Message update(Message message) {
        Path path = resolvePath(message.getId());

        try (
                FileOutputStream fos = new FileOutputStream(path.toFile());
                ObjectOutputStream oos = new ObjectOutputStream(fos);
        ) {
            oos.writeObject(message);
            return message;
        } catch (IOException e) {
            throw new RuntimeException("해당 메세지를 수정하는 중 오류가 발생했습니다.", e);
        }
    }

    // 존재 여부(중복체크)
    @Override
    public boolean existsById(UUID id) {
        Path path = resolvePath(id);
        return Files.exists(path);
    }

    // 삭제(단건)
    @Override
    public void deleteById(UUID id) {
        Path path = resolvePath(id);

        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new RuntimeException("해당 메세지를 삭제하는데 실패했습니다.", e);
        }
    }

    // 삭제(다건)
    @Override
    public void deleteAll() {
        try (Stream<Path> stream = Files.list(DIRECTORY)) {
            stream.filter((path) -> path.getFileName().toString().endsWith(EXTENSION))
                    .forEach(path -> {
                        try {
                            Files.deleteIfExists(path);
                        } catch (IOException e) {
                            throw new RuntimeException("메세지를 삭제하는데 실패했습니다", e);
                        }
                    });
        } catch (Exception e) {
            throw new RuntimeException("메세지를 전체 삭제하는데 실패했습니다.", e);
        }
    }
}
