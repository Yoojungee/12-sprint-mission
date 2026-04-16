package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

public class User implements Serializable, Comparable<User> {
    private static final long serialVersionUID = 1L;

    private UUID id;
    private String username; // 유저명(아이디)
    private String nickname; // 닉네임
    private String email; // 이메일
    private transient String password; // 패스워드
    private final Instant createdAt;
    private Instant updatedAt;

    public User(String username, String nickname, String email, String password) {
        this.id = UUID.randomUUID();
        this.username = username;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getNickname() {
        return nickname;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    // 수정 update 함수
    public void update(String username, String nickname, String email, String password) {
        this.username = username;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.updatedAt = Instant.now();
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", nickname='" + nickname + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    @Override
    public int compareTo(User o) {
        int nameCompare = this.username.compareTo(o.username);

        if (nameCompare != 0) {
            return nameCompare;
        }

        return this.createdAt.compareTo(o.createdAt);
    }
}