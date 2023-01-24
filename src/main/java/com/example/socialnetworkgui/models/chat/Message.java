package com.example.socialnetworkgui.models.chat;

import com.example.socialnetworkgui.models.Entity;
import com.example.socialnetworkgui.models.account.User;

import java.time.LocalDateTime;
import java.util.Objects;

public class Message extends Entity<Long> {

    private User sender;
    private IndividualChat chat;
    private String message;
    private LocalDateTime sentTime;

    public Message(Long ID, User sender, IndividualChat chat, String message, LocalDateTime sentTime) {
        this.setId(ID);
        this.sender = sender;
        this.chat = chat;
        this.message = message;
        this.sentTime = sentTime;
    }

    public Message(User sender, IndividualChat chat, String message, LocalDateTime sentTime) {
        this.sender = sender;
        this.chat = chat;
        this.message = message;
        this.sentTime = sentTime;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public IndividualChat getChat() {
        return chat;
    }

    public void setChat(IndividualChat chat) {
        this.chat = chat;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getSentTime() {
        return sentTime;
    }

    public void setSentTime(LocalDateTime sentTime) {
        this.sentTime = sentTime;
    }

    @Override
    public String toString() {
        return "Message{" +
                "sender=" + sender +
                ", chat=" + chat +
                ", message='" + message + '\'' +
                ", sentTime=" + sentTime +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Message)) return false;
        Message message = (Message) o;
        return Objects.equals(this.getId(), message.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
