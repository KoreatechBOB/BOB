package com.example.bob.ui.ChatList;

public class ChatDTO {

    private String User_Name;
    private String message;

    public ChatDTO() {}
    public ChatDTO(String userName, String message) {
        this.User_Name = userName;
        this.message = message;
    }

    public void setUserName(String userName) {
        this.User_Name = userName;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserName() {
        return User_Name;
    }

    public String getMessage() {
        return message;
    }
}