package core.ai;

import core.fieldObjects.ball.Ball;
import core.fieldObjects.robot.Ally;
import core.fieldObjects.robot.Foe;
import core.fieldObjects.robot.Team;
import core.util.Vector2d;

import java.util.ArrayList;

/**
 * central hub to find all info related to game
 * vision module writes info here
 * BT uses info here to make decisions
 */
public class GameInfo {

    private static Team TEAM_COLOR;
    private static Team FOE_TEAM_COLOR;

    private static ArrayList<Ally> fielders;
    private static Ally keeper;
    private static ArrayList<Ally> allies;

    // last foe in list should be opposing goalkeeper
    private static ArrayList<Foe> foes;
    private static ArrayList<Foe> foeFielders;
    private static Foe foeKeeper;
    private static Boolean possessBall;
    private static Ally allyClosestToBall;
    private static Foe foeClosestToBall;
    private static GameState currState;
    private static Vector2d ballPlacementLocation;
    private static Ball ball;

    /**
     * Initialize game information
     */
    public void initialize(Team teamColor, Team foeTeamColor, ArrayList<Ally> fielders, Ally keeper,
                           ArrayList<Foe> foes, GameState currState, Ball ball) {
        GameInfo.TEAM_COLOR = teamColor;
        GameInfo.FOE_TEAM_COLOR = foeTeamColor;
        GameInfo.fielders = fielders;
        GameInfo.keeper = keeper;
        GameInfo.allies = new ArrayList<>(fielders);
        GameInfo.allies.add(keeper);
        GameInfo.foes = foes;
        GameInfo.foeFielders = (ArrayList<Foe>) foes.subList(0, foes.size() - 1);
        GameInfo.foeKeeper = foes.get(foes.size() - 1);
        GameInfo.currState = currState;
        GameInfo.ball = ball;
    }

    public static Team getTeamColor() {
        return TEAM_COLOR;
    }

    public static Team getFoeTeamColor() {
        return FOE_TEAM_COLOR;
    }

    public static ArrayList<Ally> getFielders() {
        return fielders;
    }

    public static Ally getKeeper() {
        return keeper;
    }

    public static ArrayList<Ally> getAllies() {
        return allies;
    }

    public static ArrayList<Foe> getFoes() {
        return foes;
    }

    public static ArrayList<Foe> getFoeFielders() {
        return foeFielders;
    }

    public static Foe getFoeKeeper() {
        return foeKeeper;
    }

    public static Boolean getPossessBall() {
        return possessBall;
    }

    public static Ally getAllyClosestToBall() {
        return allyClosestToBall;
    }

    public static Foe getFoeClosestToBall() {
        return foeClosestToBall;
    }

    public static GameState getCurrState() {
        return currState;
    }

    public static Vector2d getBallPlacementLocation() {
        return ballPlacementLocation;
    }

    public static Ball getBall() {
        return ball;
    }

    public static void setTeamColor(Team teamColor) {
        GameInfo.TEAM_COLOR = teamColor;
    }

    public static void setFoeTeamColor(Team teamColor) {
        GameInfo.FOE_TEAM_COLOR = teamColor;
    }

    public static void setPossessBall(Boolean possessBall) {
        GameInfo.possessBall = possessBall;
    }

    public static void setAllyClosestToBall(Ally ally) {
        GameInfo.allyClosestToBall = ally;
    }

    public static void setFoeClosestToBall(Foe foeClosestToBall) {
        GameInfo.foeClosestToBall = foeClosestToBall;
    }

    public static void setCurrState(GameState currState) {
        GameInfo.currState = currState;
    }

    public static void setBallPlacementLocation(Vector2d placementLocation) {
        GameInfo.ballPlacementLocation = placementLocation;
    }

}
