package ro.cristiansterie.vote.dto;

public class DashboardTotalsDTO {
    private long users;
    private long candidates;

    public long getUsers() {
        return users;
    }

    public void setUsers(long users) {
        this.users = users;
    }

    public long getCandidates() {
        return candidates;
    }

    public void setCandidates(long candidates) {
        this.candidates = candidates;
    }
}
