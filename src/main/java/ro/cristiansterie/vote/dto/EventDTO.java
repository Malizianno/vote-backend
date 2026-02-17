package ro.cristiansterie.vote.dto;

import ro.cristiansterie.vote.util.EventActionEnum;
import ro.cristiansterie.vote.util.EventScreenEnum;
import ro.cristiansterie.vote.util.UserRoleEnum;

public class EventDTO {
    private Long id;
    private String username;
    private UserRoleEnum role;
    private EventActionEnum action;
    private EventScreenEnum screen;
    private String message;
    private String timestamp;

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

    public UserRoleEnum getRole() {
        return role;
    }

    public void setRole(UserRoleEnum role) {
        this.role = role;
    }

    public EventActionEnum getAction() {
        return action;
    }

    public void setAction(EventActionEnum action) {
        this.action = action;
    }

    public EventScreenEnum getScreen() {
        return screen;
    }

    public void setScreen(EventScreenEnum screen) {
        this.screen = screen;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
