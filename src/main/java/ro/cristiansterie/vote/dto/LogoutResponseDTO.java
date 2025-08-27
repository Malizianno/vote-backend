package ro.cristiansterie.vote.dto;

public class LogoutResponseDTO {

    private String username;
    private boolean response;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isResponse() {
        return response;
    }

    public void setResponse(boolean response) {
        this.response = response;
    }
}
