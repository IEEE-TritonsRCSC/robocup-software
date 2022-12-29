package core.fieldObjects.robot;

/**
 * Defines the possible team colors to be assigned
 */
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
