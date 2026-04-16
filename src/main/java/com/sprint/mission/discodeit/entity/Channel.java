package com.sprint.mission.discodeit.entity;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Channel implements Serializable, Comparable<Channel> {
    private static final long serialVersionUID = 1L;

    private UUID id;
    private String channelName; // 채널명
    private ChannelType channelType; // 채널타입(텍스트, 음성)
    private UUID ownerId; // 방장
    private List<UUID> memberIds; // 채널 멤버
    private String description; // 채널 설명
    private final Instant createdAt;
    private Instant updatedAt;

    public Channel(String channelName, ChannelType channelType, UUID ownerId, String description) {
        this.id = UUID.randomUUID();
        this.channelName = channelName;
        this.channelType = channelType;
        this.ownerId = ownerId;
        this.memberIds = new ArrayList<>();
        this.description = description;
        this.memberIds.add(ownerId);
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public String getChannelName() {
        return channelName;
    }

    public ChannelType getChannelType() {
        return channelType;
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    public List<UUID> getMemberIds() {
        return memberIds;
    }

    public String getDescription() {
        return description;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    // 수정 update 함수
    public void update(String channelName, ChannelType channelType, String description) {
        this.channelName = channelName;
        this.channelType = channelType;
        this.description = description;
        this.updatedAt = Instant.now();
    }

    @Override
    public String toString() {
        return "Channel{" +
                "id=" + id +
                ", channelName='" + channelName + '\'' +
                ", channelType=" + channelType +
                ", ownerId=" + ownerId +
                ", memberIds=" + memberIds +
                ", description='" + description + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    @Override
    public int compareTo(Channel o) {
        int nameCompare = this.channelName.compareTo(o.channelName);

        if (nameCompare != 0) {
            return nameCompare;
        }

        return this.createdAt.compareTo(o.createdAt);
    }
}
