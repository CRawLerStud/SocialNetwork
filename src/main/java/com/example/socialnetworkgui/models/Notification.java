package com.example.socialnetworkgui.models;

import com.example.socialnetworkgui.models.account.User;

import java.util.Objects;

public class Notification extends Entity<Long>{

    private String text;
    private User user;

    public Notification(String text, User user){
        this.text = text;
        this.user = user;
    }

    public Notification(Long ID, String text, User user) {
        this.setId(ID);
        this.text = text;
        this.user = user;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Notification)) return false;
        Notification that = (Notification) o;
        return that.getId().equals(this.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
