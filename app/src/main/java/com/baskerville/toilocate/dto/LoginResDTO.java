package com.baskerville.toilocate.dto;

import com.baskerville.toilocate.security.User;

import java.io.Serializable;

public class LoginResDTO implements Serializable {
    private String message;
    private User user;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "LoginResDTO{" +
                "message='" + message + '\'' +
                ", user=" + user.toString() +
                '}';
    }
}
