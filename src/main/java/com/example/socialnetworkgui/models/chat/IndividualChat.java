package com.example.socialnetworkgui.models.chat;

import com.example.socialnetworkgui.models.Entity;
import com.example.socialnetworkgui.models.account.User;

import java.util.Objects;

public class IndividualChat extends Entity<String> {

    private final User user1;
    private final User user2;

    public IndividualChat(String ID, User user1, User user2){
        this.setId(ID);
        this.user1 = user1;
        this.user2 = user2;
    }

    public IndividualChat(User user1, User user2){
        Long minID = Math.min(user1.getId(), user2.getId());
        Long maxID = Math.max(user1.getId(), user2.getId());
        this.setId(minID + "_" + maxID);
        this.user1 = user1;
        this.user2 = user2;
    }

    public User getUser1() {
        return user1;
    }

    public User getUser2() {
        return user2;
    }

    @Override
    public String toString() {
        return user1.toString() + ";" + user2.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IndividualChat)) return false;
        IndividualChat that = (IndividualChat) o;
        return Objects.equals(this.getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }
}
