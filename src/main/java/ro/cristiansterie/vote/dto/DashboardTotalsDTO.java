package ro.cristiansterie.vote.dto;

public class DashboardTotalsDTO {
    private int users;
    private int candidates;

    public int getUsers() {
        return users;
    }

    public void setUsers(int users) {
        this.users = users;
    }

    public int getCandidates() {
        return candidates;
    }

    public void setCandidates(int candidates) {
        this.candidates = candidates;
    }
}
