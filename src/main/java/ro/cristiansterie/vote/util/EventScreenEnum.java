package ro.cristiansterie.vote.util;

public enum EventScreenEnum {
    ALL("all"),
    LOGIN("login"),
    REGISTER("register"),
    ELECTIONS("elections"),
    CANDIDATES("candidates"),
    VOTE("vote"),
    RESULTS("results"),
    SETTINGS("settings");

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
