package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public class FileChannelRepository implements ChannelRepository {
    // 경로(path) 및 확장자 초기화
    private final Path DIRECTORY;
    private final String EXTENSION = ".dat";

    // 디렉토리 생성 및 초기화
    public FileChannelRepository() {
        this.DIRECTORY = Path.of(System.getProperty("user.dir"), "file-data-map", "Channels");

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
    public Channel save(Channel channel) {
        Path path = resolvePath(channel.getId());

        try (
                FileOutputStream fos = new FileOutputStream(path.toFile());
                ObjectOutputStream oos = new ObjectOutputStream(fos);
        ) {
            oos.writeObject(channel);
        } catch (IOException e) {
            throw new RuntimeException("채널을 등록하는 중 오류가 발생했습니다.", e);
        }
        return channel;
    }

    // 조회(단건)
    @Override
    public Optional<Channel> findById(UUID id) {
        Path path = resolvePath(id);

        if (Files.notExists(path)) {
            return Optional.empty();
        }

        try (
                FileInputStream fis = new FileInputStream(path.toFile());
                ObjectInputStream ois = new ObjectInputStream(fis);
        ) {
            Channel channel = (Channel) ois.readObject();
            return Optional.ofNullable(channel);
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("해당 채널을 조회하는 중 오류가 발생했습니다.", e);
        }
    }

    // 조회(다건)
    @Override
    public List<Channel> findAll() {
        try (Stream<Path> stream = Files.list(DIRECTORY)) {
            return stream.filter(path -> path.getFileName().toString().endsWith(EXTENSION))
                    .map(path -> {
                        try (
                                FileInputStream fis = new FileInputStream(path.toFile());
                                ObjectInputStream ois = new ObjectInputStream(fis);
                        ) {
                            return (Channel) ois.readObject();
                        } catch (IOException | ClassNotFoundException e) {
                            throw new RuntimeException("채널을 조회하는 중 오류가 발생했습니다.", e);
                        }
                    })
                    .sorted()
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException("채널 목록을 조회하는 중 오류가 발생했습니다.", e);
        }
    }

    // 수정
    @Override
    public Channel update(Channel channel) {
        Path path = resolvePath(channel.getId());

        try (
                FileOutputStream fos = new FileOutputStream(path.toFile());
                ObjectOutputStream oos = new ObjectOutputStream(fos);
        ) {
            oos.writeObject(channel);
            return channel;
        } catch (IOException e) {
            throw new RuntimeException("해당 채널을 수정하는 중 오류가 발생했습니다.", e);
        }
    }

    // 존재유무
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
            throw new RuntimeException("해당 채널을 삭제하는데 실패했습니다.", e);
        }
    }
}
