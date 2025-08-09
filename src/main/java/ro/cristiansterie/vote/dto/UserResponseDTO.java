package ro.cristiansterie.vote.dto;

import java.util.List;

public class UserResponseDTO {
    private List<UserDTO> users;
    private int total;
    private int adminUsersCount;
    private int votantUsersCount;

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

    public int getAdminUsersCount() {
        return adminUsersCount;
    }

    public void setAdminUsersCount(int adminUsersCount) {
        this.adminUsersCount = adminUsersCount;
    }

    public int getVotantUsersCount() {
        return votantUsersCount;
    }

    public void setVotantUsersCount(int votantUsersCount) {
        this.votantUsersCount = votantUsersCount;
    }

}
