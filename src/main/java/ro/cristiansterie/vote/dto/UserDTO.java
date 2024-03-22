package ro.cristiansterie.vote.dto;

import ro.cristiansterie.vote.util.UserRoleEnum;

public class UserDTO {
    private Integer id;
    private String username;
    private UserRoleEnum role;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UserRoleEnum getRole() {
        return role;
    }

    public void setRole(UserRoleEnum role) {
        this.role = role;
    }
}
