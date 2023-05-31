package com.example.myapplication.model;

import java.util.List;

public class JwtResponse {
    private String accessToken;
    private String tokenType = "Bearer";
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private List<String> roles;
    private boolean isEnabled;

    public JwtResponse(String accessToken, Long id, String username, List<String> roles, String firstName, String lastName, boolean isEnabled) {
        this.accessToken = accessToken;
        this.id = id;
        this.username = username;
        this.roles = roles;
        this.firstName = firstName;
        this.lastName = lastName;
        this.isEnabled = isEnabled;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getRoles() {
        return roles;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }
}
