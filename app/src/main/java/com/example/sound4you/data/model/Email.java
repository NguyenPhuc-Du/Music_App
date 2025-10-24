package com.example.sound4you.data.model;

public class Email {
    private String email;
    private String code;
    private String purpose; // register hoặc forgot_password

    public Email(String email, String purpose) {
        this.email = email;
        this.purpose = purpose;
    }

    public Email(String email, String code, String purpose) {
        this.email = email;
        this.code = code;
        this.purpose = purpose;
    }

    public String getEmail() { return email; }
    public String getCode() { return code; }
    public String getPurpose() { return purpose; }
}
