package ro.cristiansterie.vote.dto;

import ro.cristiansterie.vote.util.UserRoleEnum;

public class UserDTO {
    private Integer id;

    // these are here for frontend login purposes - the whole object actually
    private String username;
    private String password;

    private UserRoleEnum role;
    private Boolean hasVoted;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public UserRoleEnum getRole() {
        return role;
    }

    public void setRole(UserRoleEnum role) {
        this.role = role;
    }

    public Boolean getHasVoted() {
        return hasVoted;
    }

    public void setHasVoted(Boolean hasVoted) {
        this.hasVoted = hasVoted;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
