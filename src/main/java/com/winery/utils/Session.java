package com.winery.utils;

import com.winery.entities.User;

public class Session {
    private static Session INSTANCE;
    private User user;

    private Session() {
        // Private constructor to prevent instantiation.
    }

    public static Session getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Session();
        }
        return INSTANCE;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}