package ro.cristiansterie.vote.dto;

import ro.cristiansterie.vote.util.Paging;

public class UserFilterDTO implements Filterable<UserDTO> {
    private UserDTO user;
    private Paging paging;

    public UserDTO getUser() {
        return getObject();
    }

    public void setUser(UserDTO user) {
        setObject(user);
    }

    public UserDTO getObject() {
        return user;
    }

    public void setObject(UserDTO user) {
        this.user = user;
    }

    public Paging getPaging() {
        return paging;
    }

    public void setPaging(Paging paging) {
        this.paging = paging;
    }
}
