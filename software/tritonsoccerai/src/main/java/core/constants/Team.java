package main.java.core.constants;

public enum Team {
    BLUE("blue"),
    YELLOW("yellow"),
    ;

    private final String teamString;

    Team(String teamString) {
        this.teamString = teamString;
    }

    public String getTeamString() {
        return teamString;
    }
}