package core.ai;

import core.fieldObjects.ball.Ball;
import core.fieldObjects.robot.Ally;
import core.fieldObjects.robot.Foe;
import core.fieldObjects.robot.Team;

import java.util.ArrayList;

// central hub to find all info related to game
// vision module writes info here
// BT uses info here to make decisions

public class GameInfo {

    private static Team TEAM_COLOR;

    private static ArrayList<Ally> fielders;
    private static Ally keeper;
    private static ArrayList<Ally> allies;
    private static ArrayList<Foe> foes;
    private static Boolean possessBall;
    private static Ally allyClosestToBall;
    private static GameState currState;
    private static Ball ball;

    public void initialize(Team teamColor, ArrayList<Ally> fielders, Ally keeper,
                           ArrayList<Foe> foes, GameState currState, Ball ball) {
        GameInfo.TEAM_COLOR = teamColor;
        GameInfo.fielders = fielders;
        GameInfo.keeper = keeper;
        GameInfo.allies = new ArrayList<>(fielders);
        GameInfo.allies.add(keeper);
        GameInfo.foes = foes;
        GameInfo.currState = currState;
        GameInfo.ball = ball;
    }

    public static Team getTeamColor() {
        return TEAM_COLOR;
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

    public static Boolean getPossessBall() {
        return possessBall;
    }
    public static Ally getAllyClosestToBall() {
        return allyClosestToBall;
    }

    public static GameState getCurrState() {
        return currState;
    }

    public static Ball getBall() {
        return ball;
    }

    public static void setPossessBall(Boolean possessBall) {
        GameInfo.possessBall = possessBall;
    }

    public static void setAllyClosestToBall(Ally ally) {
        GameInfo.allyClosestToBall = ally;
    }

    public static void setCurrState(GameState currState) {
        GameInfo.currState = currState;
    }

}
