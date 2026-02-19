package ro.cristiansterie.vote.util;

public enum EventActionEnum {
    ALL("all"),
    SAVE("save"),
    UPDATE("update"),
    DELETE("delete"),
    GET("get"),
    GET_ALL("getAll"),
    GET_FILTERED("getFiltered"),
    COUNT_FILTERED("countFiltered"),
    GET_LAST_10("getLast10"),
    VOTE("vote"),
    LOGIN("login"),
    LOGOUT("logout"),
    REGISTER("register"),
    GET_CAMPAIGN_STATUS("getCampaignStatus"),
    GET_ELECTION_RESULT("getElectionResult"),
    CHANGE_STATUS("changeStatus");

    private final String action;

    EventActionEnum(String action) {
        this.action = action;
    }

    public String getAction() {
        return action;
    }

    @Override
    public String toString() {
        return action;
    }
}
