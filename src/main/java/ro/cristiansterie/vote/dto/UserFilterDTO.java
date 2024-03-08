package ro.cristiansterie.vote.dto;

import ro.cristiansterie.vote.util.Paging;

public class UserFilterDTO {
    private UserDTO user;
    private Paging paging;

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public Paging getPaging() {
        return paging;
    }

    public void setPaging(Paging paging) {
        this.paging = paging;
    }
}
