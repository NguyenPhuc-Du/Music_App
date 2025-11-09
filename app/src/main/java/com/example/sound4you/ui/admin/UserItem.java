package com.example.sound4you.ui.admin;

public class UserItem {
    private int id;
    private String userName;
    private String emailTitle;
    private String userProfilePicture;
    private int role;

    public UserItem(int id, String userName, String emailTitle, String userProfilePicture, int role) {
        this.id = id;
        this.userName = userName;
        this.emailTitle = emailTitle;
        this.userProfilePicture = userProfilePicture;
        this.role = role;
    }

    public String getEmailTitle() {
        return emailTitle;
    }

    public int getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserProfilePicture() {
        return userProfilePicture;
    }

    public int getRole() {
        return role;
    }
}
