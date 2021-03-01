package com.example.coinfolio;

public class User {
    private static User instance;

    private String uuid;

    public static User getInstance() {
        if (instance == null)
            instance = new User();
        return instance;
    }

    private User () {
        // any code that you need to construct
        this.uuid = "";
    }

    public void setUserUUId(String request) {
        // using the connection
        uuid = request;
    }
}
