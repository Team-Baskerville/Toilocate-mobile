package com.baskerville.toilocate.security;

public class UserHandler {
    private static User user = null;

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        UserHandler.user = user;
    }

    public static void removeUser(){
        user = null;
    }
}
