package ro.cristiansterie.vote.util;

public enum EventScreenEnum {
    ALL("all"),
    LOGIN("login"),
    DASHBOARD("dashboard"),
    USERS("users"),
    REGISTER("register"),
    ELECTIONS("elections"),
    ELECTIONS_HELPER("elections_helper"),
    CANDIDATES("candidates"),
    VOTE("vote"),
    RESULTS("results"),
    SETTINGS("settings"),
    NEWSFEED("newsfeed");

    private final String screen;

    EventScreenEnum(String screen) {
        this.screen = screen;
    }

    public String getScreen() {
        return screen;
    }

    @Override
    public String toString() {
        return screen;
    }
}
