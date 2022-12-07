package core.ai;

import core.fieldObjects.ball.Ball;
import core.fieldObjects.robot.Ally;
import core.fieldObjects.robot.Foe;

import java.util.ArrayList;

// central hub to find all info related to game
// vision module writes info here
// BT uses info here to make decisions

public class GameInfo {

    private static ArrayList<Ally> fielders;
    private static ArrayList<Foe> foes;
    private static Ally keeper;
    private static Boolean possessBall;
    private static GameState currState;
    private static Ball ball;

    public void initialize(ArrayList<Ally> fielders, ArrayList<Foe> foes,
                           Ally keeper, GameState currState, Ball ball) {
        GameInfo.fielders = fielders;
        GameInfo.foes = foes;
        GameInfo.keeper = keeper;
        GameInfo.currState = currState;
        GameInfo.ball = ball;
    }

    public static ArrayList<Ally> getFielders() {
        return fielders;
    }

    public static ArrayList<Foe> getFoes() {
        return foes;
    }

    public static Ally getKeeper() {
        return keeper;
    }

    public static Boolean getPossessBall() {
        return possessBall;
    }

    public static GameState getCurrState() {
        return currState;
    }

    public static void setPossessBall(Boolean possessBall) {
        GameInfo.possessBall = possessBall;
    }

    public static void setCurrState(GameState currState) {
        GameInfo.currState = currState;
    }

}
