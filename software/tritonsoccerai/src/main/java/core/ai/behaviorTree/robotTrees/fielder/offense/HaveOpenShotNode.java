package main.java.core.ai.behaviorTree.robotTrees.fielder.offense;

import main.java.core.ai.behaviorTree.nodes.conditionalNodes.ConditionalNode;
import static proto.triton.FilteredObject.Robot;
import main.java.core.util.Vector2d;
import static proto.vision.MessagesRobocupSslGeometry.SSL_GeometryFieldSize;
import main.java.core.ai.GameInfo;
import static main.java.core.util.ProtobufUtils.getPos;

import java.util.ArrayList;
import java.util.List;

public class HaveOpenShotNode extends ConditionalNode {

    private final int allyID;

    public HaveOpenShotNode(int allyID) {
        this.allyID = allyID;
    }

    /**
     * Determines whether open shot exists for current ally ballholder
     * 
     * @return whether open shot exists
     */
    @Override
    public boolean conditionSatisfied() {
        boolean hasOpenShot = true;

        SSL_GeometryFieldSize field = GameInfo.getField();

        // Get the goal parameters
        float goalX = field.getGoalWidth() / 2f;
        float goalY = field.getFieldLength() / 2f;

        // Check if there is open shot
        ArrayList<Robot> foesList = new ArrayList<>(GameInfo.getFoes());
        ArrayList<Robot> alliesList = new ArrayList<>(GameInfo.getFielders());
        List<Vector2d> obstaclePositions = new ArrayList<>();

        //remove the ally closest to the ball
        alliesList.remove(GameInfo.getAllyClosestToBall());

        //add the other ally positions and foe positions to the obstaclesPositions list
        for(int i=0;i<alliesList.size();i++) {
			obstaclePositions.add(getPos(alliesList.get(i)));
            obstaclePositions.add(getPos(foesList.get(i)));
		}

        double slope = (goalY - GameInfo.getAllyClosestToBall().getY())
                         / (goalX - GameInfo.getAllyClosestToBall().getX());
        double yIntercept = goalY - slope * goalX;
        
        for (Vector2d obstacle : obstaclePositions) {
            if (obstacle.y == slope * obstacle.x + yIntercept) {
                hasOpenShot = false;
            }
        }

        return hasOpenShot;
    }

}