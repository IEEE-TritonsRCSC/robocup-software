package main.java.core.ai.behaviorTree.robotTrees.fielder.specificStateFunctions;

import main.java.core.ai.GameInfo;
import main.java.core.ai.behaviorTree.nodes.NodeState;
import main.java.core.ai.behaviorTree.nodes.taskNodes.TaskNode;
import main.java.core.ai.behaviorTree.robotTrees.basicFunctions.ClosestToBallNode;
import main.java.core.ai.behaviorTree.robotTrees.basicFunctions.DribbleBallNode;
import main.java.core.ai.behaviorTree.robotTrees.basicFunctions.MoveToPositionNode;
import main.java.core.ai.behaviorTree.robotTrees.basicFunctions.ChaseBallNode;
import static proto.triton.FilteredObject.Robot;
import static proto.gc.SslGcRefereeMessage.Referee;

import static main.java.core.util.ProtobufUtils.getPos;
import main.java.core.util.Vector2d;
import main.java.core.constants.Team;

/**
 * Handles Ball Placement game state
 * If our possession, place ball at designated location
 */
public class BallPlacementNode extends TaskNode {
    
    private final ClosestToBallNode closestToBallNode;
    private final DribbleBallNode dribbleBallNode;
    private final MoveToPositionNode moveToPositionNode; //
    private final ChaseBallNode ChaseBallNode;

    public BallPlacementNode(int allyID, ClosestToBallNode closestToBallNode) {
        super("Ball Placement Node: " + allyID, allyID);
        this.closestToBallNode = closestToBallNode;
        this.dribbleBallNode = new DribbleBallNode(allyID);
        this.moveToPositionNode = new MoveToPositionNode(allyID); //
        this.ChaseBallNode = new ChaseBallNode(allyID);
    }

    /**
     * If our possession AND robot closest to ball, dribble ball to placement location
     * Otherwise, move away from placement location
     */
    @Override
    public NodeState execute() {
        float DISTANCE_CONSTANT = 1;
        if((GameInfo.getCurrCommand() == Referee.Command.BALL_PLACEMENT_YELLOW && GameInfo.getTeamColor() == Team.YELLOW 
        ||GameInfo.getCurrCommand() == Referee.Command.BALL_PLACEMENT_BLUE && GameInfo.getTeamColor() == Team.BLUE) 
        && closestToBallNode.execute() == NodeState.SUCCESS){
                while (getPos(GameInfo.getBall()).dist(GameInfo.getBallPlacementLocation()) > DISTANCE_CONSTANT) {
                    if(!GameInfo.getPossessBall(allyID)){
                        this.ChaseBallNode.execute();
                    }
                    else{
                        this.dribbleBallNode.execute(GameInfo.getBallPlacementLocation());
                    }
            }
        }
    

        else {
            // TODO - move away from placement location if close to it
            Robot ally = GameInfo.getAlly(allyID);
            Vector2d allyPos = getPos(ally);
            Vector2d ballPlacePos = GameInfo.getBallPlacementLocation();
            Vector2d ballPos = getPos(GameInfo.getBall());
            System.out.println("ballplacementposition:" + ballPlacePos);
            System.out.println("allyposition:" + allyPos);
            Vector2d locToAlley = getPos(ally).sub(ballPlacePos);
            Vector2d ballToAlley = getPos(ally).sub(ballPos);
            System.out.println("Dist:" + locToAlley);
            if(locToAlley.mag()<500 && ballToAlley.mag()<500 ){
                Vector2d targetPos =  ballPlacePos.add(new Vector2d(50000/(allyPos.x - ballPlacePos.x), 50000/(allyPos.y - ballPlacePos.y)));
                targetPos.add(new Vector2d(50000/(allyPos.x - ballPos.x), 50000/(allyPos.y - ballPos.y)));
                System.out.println("targetPos:" + targetPos);
                MoveToPositionNode MoveToPosition = new MoveToPositionNode(allyID);
                MoveToPosition.execute(targetPos);
            }
           
        }
        return NodeState.SUCCESS;
    }

}
