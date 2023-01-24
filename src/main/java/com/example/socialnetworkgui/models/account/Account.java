package com.example.socialnetworkgui.models.account;

import com.example.socialnetworkgui.models.Entity;

import java.util.Objects;

public class Account extends Entity<String> {

    private String email;
    private String password;
    private User user;

    public Account(String email, String password, User user) {
        this.email = email;
        this.password = password;
        this.user = user;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return this.email + ";"+this.password+";"+this.user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return email.equals(account.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }
}
