package core.ai.behaviorTree.robotTrees.central;

import core.ai.behaviorTree.nodes.serviceNodes.ServiceNode;
import core.ai.behaviorTree.robotTrees.basicFunctions.MoveToPositionNode;
import core.ai.GameInfo;
import core.ai.GameState;
import static proto.triton.FilteredObject.Robot;

import java.util.List;

import core.util.Vector2d;
import core.ai.behaviorTree.nodes.NodeState;

import static proto.gc.SslGcRefereeMessage.Referee;

import static core.util.ProtobufUtils.getPos;

// TODO : Not sure how to send pass details and process them
public class CentralCoordinatorService extends ServiceNode {

    // used to track starting position of ball
    // to determine if ball kicked into play
    private Vector2d ballStartPos = null;

    public CentralCoordinatorService() {
        super("Central Coordinator Service");
    }

    /**
     * Sends appropriate messages to robot trees based on provided pass details
     * Instructs receiver to expect ball at a given pass location
     * Instructs all other robot trees to position based on pass location
     */
    private static void coordinatedPass(Robot receiver, Vector2d passLoc) {
        // TODO
        for (Robot ally : GameInfo.getAllies()) {
            if (ally == receiver) {
                // send message to receiver to expect ball at passLoc
            }
            else {
                // send message to all other robots to position based on passLoc
            }
        }
    }


    // --------------------------------- TEAM FORMATION ---------------------------------
    
    //  Formation offsets for each robot in the team
    private static final Vector2d[] FORMATION_OFFSETS = {
        new Vector2d(-0.2f, -0.3f),  // Left Defender (closer to our goal)
        new Vector2d(0.2f, -0.3f),   // Right Defender
        new Vector2d(-0.25f, 0f),    // Left Midfielder
        new Vector2d(0.25f, 0f),     // Right Midfielder
        new Vector2d(0f, 0.3f),      // Striker (closer to opponent's goal)
        new Vector2d(0f, -0.4f)      // Goalkeeper (not assigned here)
    };

    // to communicate with robot trees
    protected void declareConsumes() {
        // No consumption setup required for now
    }

    // computes and assigns team formation to robot trees
    public void assignFormation() {
        List<Robot> allies = GameInfo.getAllies();
        if (allies.isEmpty()) return;

        Vector2d formationCenter = computeFormationCenter(allies);
        assignPositions(allies, formationCenter);
    }
    
    // computes team formation centroid
    private Vector2d computeFormationCenter(List<Robot> allies) {
        float sumX = 0, sumY = 0;
        for (Robot ally : allies) {
            Vector2d pos = GameInfo.getPos(ally);
            sumX += pos.x;
            sumY += pos.y;
        }
        return new Vector2d(sumX / allies.size(), sumY / allies.size());
    }

    // scales formation width/height based on field dimensions and
    // in the future will scale according to defense/offense
    private Vector2d computeScaledOffset(int index) {
        float fieldWidth = (float) GameInfo.getFieldWidth();
        float fieldHeight = (float) GameInfo.getFieldLength();
        return new Vector2d(
            FORMATION_OFFSETS[index].x * (fieldWidth / 2),
            FORMATION_OFFSETS[index].y * (fieldHeight / 2)
        );
    }
    
    // assigns positions to robots based on formation center and scaled offsets
    private void assignPositions(List<Robot> allies, Vector2d formationCenter) {
        for (int i = 0; i < Math.min(allies.size(), FORMATION_OFFSETS.length); i++) {
            Vector2d targetPosition = formationCenter.add(computeScaledOffset(i));
            moveRobotToPosition(allies.get(i), targetPosition);
        }
    }

    // moves robot to target positions in formation
    private void moveRobotToPosition(Robot robot, Vector2d targetPosition) {
        MoveToPositionNode moveNode = new MoveToPositionNode(robot.getId());
        moveNode.execute(targetPosition);
    }
    
    // --------------------------------- END TEAM FORMATION ---------------------------------

    /**
     * Check for messages from robots and check if current state is NormalStart
     * When new message received, call correct coordinating method
     * If current state is NormalStart, change current state when ball kicked
     */
    @Override
    public NodeState execute() {
        float DISTANCE_CONSTANT = (float) 0.2;
        if (GameInfo.getCurrCommand() == Referee.Command.NORMAL_START) {
            /*if (this.ballStartPos == null) {
                this.ballStartPos = getPos(GameInfo.getBall());
            }
            // check if ball kicked
            if (getPos(GameInfo.getBall()).dist(ballStartPos) > DISTANCE_CONSTANT) {
                // if so, switch current game state to OPEN_PLAY
                GameInfo.setCurrState(GameState.OPEN_PLAY);
            }*/
            GameInfo.setInOpenPlay(true);
        }
        // System.out.println(String.valueOf(GameInfo.getBall().getX()) + " " + String.valueOf(GameInfo.getBall().getY()));
        // else check for new message to act upon
        return NodeState.SUCCESS;
    }
    
}
