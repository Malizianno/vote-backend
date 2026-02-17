package ro.cristiansterie.vote.dto;

import java.util.List;

public class UserResponseDTO {
    private List<UserDTO> users;
    private Long total;
    private Long adminUsersCount;
    private Long votantUsersCount;

    public List<UserDTO> getUsers() {
        return users;
    }

    public void setUsers(List<UserDTO> users) {
        this.users = users;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Long getAdminUsersCount() {
        return adminUsersCount;
    }

    public void setAdminUsersCount(Long adminUsersCount) {
        this.adminUsersCount = adminUsersCount;
    }

    public Long getVotantUsersCount() {
        return votantUsersCount;
    }

    public void setVotantUsersCount(Long votantUsersCount) {
        this.votantUsersCount = votantUsersCount;
    }

}
