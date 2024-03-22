package ro.cristiansterie.vote.dto;

import ro.cristiansterie.vote.util.UserRoleEnum;

public class LoginResponseDTO {

    private String username;
    private String token;
    private UserRoleEnum role;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserRoleEnum getRole() {
        return role;
    }

    public void setRole(UserRoleEnum role) {
        this.role = role;
    }

}
