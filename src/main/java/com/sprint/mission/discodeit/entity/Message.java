package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Message {
    private UUID id;
    private User sendUser; // 상대방
    private Channel channel; // 채널
    private String content; // 채팅 내용
    private Long createdAt;
    private Long updatedAt;

    public Message(User sendUser, Channel channel, String content) {
        this.id = UUID.randomUUID();
        this.sendUser = sendUser;
        this.channel = channel;
        this.content = content;
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
    }

    public UUID getId() {
        return id;
    }

    public User getSendUser() {
        return sendUser;
    }

    public Channel getChannel() {
        return channel;
    }

    public String getContent() {
        return content;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    // 수정 update 함수
    public void update(String content){
        this.content = content;
        this.updatedAt = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", sendUser=" + sendUser +
                ", channel=" + channel +
                ", content='" + content + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
