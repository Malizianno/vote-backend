package ro.cristiansterie.vote.dto;

import java.util.List;

public class UserResponseDTO {
    private List<UserDTO> users;
    private int total;

    public List<UserDTO> getUsers() {
        return users;
    }

    public void setUsers(List<UserDTO> users) {
        this.users = users;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
