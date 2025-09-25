package com.bluebell.project.dto;

public class LoginResponse {
    private String fullName;
    private String email;
    private  String role;
    private String token;

    public LoginResponse(String fullName, String email, String role, String token) {
        this.fullName = fullName;
        this.email = email;
        this.role = role;
        this.token = token;
    }

    public String getFullName() {
        return fullName;
    }
    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public String getToken() {
        return token;
    }
}
