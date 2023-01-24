package com.example.socialnetworkgui.models;

import com.example.socialnetworkgui.models.account.User;

import java.time.LocalDateTime;
import java.util.Objects;

public class Post extends Entity<Long> {

    private User user;
    private LocalDateTime postTime;
    private String text;

    public Post(User user, String text){
        this.user = user;
        this.text = text;
        this.postTime = LocalDateTime.now();
    }
    public Post(User user, String text, LocalDateTime postTime) {
        this.user = user;
        this.postTime = postTime;
        this.text = text;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getPostTime() {
        return postTime;
    }

    public void setPostTime(LocalDateTime postTime) {
        this.postTime = postTime;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Post)) return false;
        Post post = (Post) o;
        return this.getId().equals(post.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
