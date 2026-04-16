package com.sprint.mission.discodeit.entity;

import java.awt.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

public class Message implements Serializable, Comparable<Message> {
    private static final long serialVersionUID = 1L;

    private UUID id;
    private UUID authorId; // 작성자(발신자)
    private UUID receiverId; // 상대방(수신자)
    private UUID channelId; // 채널
    private String content; // 채팅 내용
    private final Instant createdAt;
    private Instant updatedAt;

    public Message(UUID authorId, UUID receiverId, UUID channelId, String content) {
        this.id = UUID.randomUUID();
        this.authorId = authorId;
        this.receiverId = receiverId;
        this.channelId = channelId;
        this.content = content;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public UUID getAuthorId() {
        return authorId;
    }

    public UUID getReceiverId() {
        return receiverId;
    }

    public UUID getChannelId() {
        return channelId;
    }

    public String getContent() {
        return content;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    // 수정 update 함수
    public void update(String content) {
        this.content = content;
        this.updatedAt = Instant.now();
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", authorId=" + authorId +
                ", receiverId=" + receiverId +
                ", channel=" + channelId +
                ", content='" + content + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    @Override
    public int compareTo(Message o) {
        return this.createdAt.compareTo(o.createdAt);
    }
}
