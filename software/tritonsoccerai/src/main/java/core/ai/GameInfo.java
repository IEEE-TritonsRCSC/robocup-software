package main.java.core.ai;

import static proto.triton.FilteredObject.*;
import static proto.vision.MessagesRobocupSslGeometry.SSL_GeometryFieldSize;
import static proto.gc.SslGcRefereeMessage.Referee;
import main.java.core.util.Vector2d;
import main.java.core.constants.Team;

import static main.java.core.constants.RobotConstants.DRIBBLE_THRESHOLD;

import java.util.ArrayList;

import static main.java.core.util.ProtobufUtils.getPos;

/**
 * central hub to find all info related to game
 * vision module writes info here
 * BT uses info here to make decisions
 */
public class GameInfo {

    public static FilteredWrapperPacket wrapper;
    public static Referee ref;
    public static Referee.Command prevCommand;

    public static boolean inOpenPlay = false;

    private static Team TEAM_COLOR;
    private static Team FOE_TEAM_COLOR;

    // private static Vector2d ballPlacementLocation;

    /**
     * Initialize game information
     */
    public void initialize(FilteredWrapperPacket wrapper, Team teamColor, Team foeTeamColor) {
        GameInfo.wrapper = wrapper;
        GameInfo.TEAM_COLOR = teamColor;
        GameInfo.FOE_TEAM_COLOR = foeTeamColor;
    }

    public static FilteredWrapperPacket getWrapper() {
        return wrapper;
    }

    public static Referee getReferee() {
        return ref;
    }

    public static Team getTeamColor() {
        return TEAM_COLOR;
    }

    public static Team getFoeTeamColor() {
        return FOE_TEAM_COLOR;
    }

    public static ArrayList<Robot> getFielders() {
        ArrayList<Robot> allies = getAllies();
        allies.remove(0);
        return allies;
    }

    public static Robot getKeeper() {
        return getAllies().get(0);
    }

    public static ArrayList<Robot> getAllies() {
        ArrayList<Robot> allies = new ArrayList<Robot>(wrapper.getAlliesMap().values());
        return allies;
    }

    public static ArrayList<Robot> getFoes() {
        ArrayList<Robot> foes = new ArrayList<Robot>(wrapper.getFoesMap().values());
        return foes;
    }

    public static ArrayList<Robot> getFoeFielders() {
        ArrayList<Robot> foes = getFoes();
        foes.remove(0);
        return foes;
    }

    public static Robot getFoeKeeper() {
        return getFoes().get(0);
    }

    public static Robot getAlly(int id) {
        return getAllies().get(id);
    }

    public static Robot getFoe(int id) {
        return getFoes().get(id);
    }

    public static Boolean getPossessBall() {
        for (Robot ally : getAllies()) {
            if (getPossessBall(ally.getId())) {return true;}
        }
        return false;
    }

    public static Boolean getPossessBall(int allyID) {
        return getPos(GameInfo.getAlly(allyID)).dist(getPos(GameInfo.getBall())) <= DRIBBLE_THRESHOLD;
    }

    public static Boolean getFoePossessBall(int foeID) {
        return getPos(GameInfo.getFoe(foeID)).dist(getPos(GameInfo.getBall())) <= DRIBBLE_THRESHOLD;
    }

    public static Robot getAllyClosestToBall() {
        ArrayList<Robot> allies = getAllies();
        Vector2d ballPos = getPos(wrapper.getBall());
        Robot closest = allies.get(1);
        float minDist = getPos(closest).dist(ballPos);
        float distToBall;
        for (int i = 2; i < allies.size(); i++) {
            distToBall = getPos(allies.get(i)).dist(ballPos);
            if (distToBall < minDist) {
                closest = allies.get(i);
                minDist = distToBall;
            }
        }
        return closest;
    }

    public static Robot getFoeClosestToBall() {
        ArrayList<Robot> foes = getFoes();
        Vector2d ballPos = getPos(wrapper.getBall());
        Robot closest = foes.get(0);
        float minDist = getPos(closest).dist(ballPos);
        float distToBall;
        for (int i = 1; i < foes.size(); i++) {
            distToBall = getPos(foes.get(i)).dist(ballPos);
            if (distToBall < minDist) {
                closest = foes.get(i);
                minDist = distToBall;
            }
        }
        return closest;
    }

    public static Referee.Command getCurrCommand() {
        return ref.getCommand();
    }

    public static Referee.Command getNextCommand() {
        return ref.getNextCommand();
    }

    public static Referee.Command getPrevCommand() {
        return prevCommand;
    }

    public static Vector2d getBallPlacementLocation() {
        Referee.Point placementLocation = ref.getDesignatedPosition();
        //  System.out.println("Placement location: " + placementLocation.getX() + ", " + placementLocation.getY());
        return new Vector2d(placementLocation.getX(), placementLocation.getY());
    }

    public static Ball getBall() {
        return wrapper.getBall();
    }

    public static SSL_GeometryFieldSize getField() {
        return wrapper.getField();
    }

    public static boolean inOpenPlay() {
        return inOpenPlay;
    }

    public static void setWrapper(FilteredWrapperPacket wrapper) {
        GameInfo.wrapper = wrapper;
    }

    public static void setReferee(Referee ref) {
        GameInfo.ref = ref;
    }

    public static void setPrevCommand(Referee.Command command) {
        GameInfo.prevCommand = command;
    }

    public static void setTeamColor(Team teamColor) {
        GameInfo.TEAM_COLOR = teamColor;
    }

    public static void setFoeTeamColor(Team teamColor) {
        GameInfo.FOE_TEAM_COLOR = teamColor;
    }

    public static void setInOpenPlay(boolean inOpenPlay) {
        GameInfo.inOpenPlay = inOpenPlay;
    }

}
