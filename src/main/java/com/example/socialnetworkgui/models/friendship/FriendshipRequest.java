package com.example.socialnetworkgui.models.friendship;

import com.example.socialnetworkgui.models.Entity;
import com.example.socialnetworkgui.models.account.User;
import javafx.util.Pair;

import java.time.LocalDateTime;

public class FriendshipRequest extends Entity<Pair<Long, Long>> {

    private final User user1;
    private final User user2;
    private String status;
    private final User sender;
    private final LocalDateTime sentTime;

    public FriendshipRequest(User user1, User user2, String status, User sender, LocalDateTime sentTime) {
        this.user1 = user1;
        this.user2 = user2;
        this.status = status;
        this.sender = sender;
        this.sentTime = sentTime;
        Long user1ID = Math.min(user1.getId(), user2.getId());
        Long user2ID = Math.max(user1.getId(), user2.getId());
        this.setId(new Pair<>(user1ID, user2ID));
    }

    public FriendshipRequest(User user1, User user2, User sender) {
        this.user1 = user1;
        this.user2 = user2;
        this.status = "Pending";
        this.sender = sender;
        this.sentTime = LocalDateTime.now();
        Long user1ID = Math.min(user1.getId(), user2.getId());
        Long user2ID = Math.max(user1.getId(), user2.getId());
        Pair<Long, Long> ID = new Pair<>(user1ID, user2ID);
        this.setId(ID);
    }

    public LocalDateTime getSentTime() {
        return sentTime;
    }

    public User getSender() {
        return sender;
    }

    public User getUser1() {
        return user1;
    }

    public User getUser2() {
        return user2;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
