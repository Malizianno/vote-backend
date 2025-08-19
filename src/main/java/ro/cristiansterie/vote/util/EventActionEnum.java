package ro.cristiansterie.vote.util;

public enum EventActionEnum {
    ALL("all"),
    CREATE("create"),
    UPDATE("update"),
    DELETE("delete"),
    GET("get"),
    GET_ALL("getAll"),
    GET_FILTERED("getFiltered"),
    GET_LAST_10("getLast10"),
    VOTE("vote"),
    LOGIN("login"),
    LOGOUT("logout"),
    REGISTER("register");

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
