package main.java.core.ai.behaviorTree.robotTrees.fielder.offense;

import main.java.core.ai.GameInfo;
import main.java.core.ai.behaviorTree.nodes.NodeState;
import main.java.core.ai.behaviorTree.nodes.taskNodes.TaskNode;
import main.java.core.ai.behaviorTree.robotTrees.basicFunctions.MoveToPositionNode;

import static proto.triton.FilteredObject.Robot;
import main.java.core.util.Vector2d;

import java.util.ArrayList;
import java.util.List;

import static main.java.core.util.ProtobufUtils.getPos;

/**
 * Positions ally at optimal position
 */
public class PositionSelfNode extends TaskNode {

    private final MoveToPositionNode moveToPositionNode;

    public PositionSelfNode(int allyID) {
        super("Position Self Node: " + allyID, allyID);
        this.moveToPositionNode = new MoveToPositionNode(allyID);
    }

    /**
     * Decides where to position self, then moves to that location
     */
    @Override
    public NodeState execute() {
        this.moveToPositionNode.execute(findPositioningLocation());
        return NodeState.SUCCESS;
    }

    /**
     * Finds optimal location to position self
     */
    private Vector2d findPositioningLocation() {

        // TODO We have to change how to calculate the optimal position (Maybe we have to use Numerical Optimization Algorithm)
        // This is just a temporary implementation

        ArrayList<Robot> foesList = new ArrayList<>(GameInfo.getFoes());
        ArrayList<Robot> alliesList = new ArrayList<>(GameInfo.getFielders());
        List<Vector2d> obstaclePositions = new ArrayList<>();

        // remove self
        alliesList.remove(allyID);

        // add the other ally positions and foe positions to the obstaclesPositions list
        for(int i=0;i<alliesList.size();i++) {
			obstaclePositions.add(getPos(alliesList.get(i)));
            obstaclePositions.add(getPos(foesList.get(i)));
		}

        // add ballposition to the obstaclesPositions list
        obstaclePositions.add(getPos(GameInfo.getBall()));


        // distance from the nearest of several obstacles
        Vector2d nearestObstacle = null;
        float distance = 5;
        float minDistance = Float.MAX_VALUE;

        for (Vector2d obstacle : obstaclePositions) {
            float currentdist = getPos(GameInfo.getAlly(allyID)).dist(obstacle);

            if (currentdist < minDistance) {
                minDistance = currentdist;
                nearestObstacle = obstacle;
            }
        }

         // end position
         float newX = nearestObstacle.x + (GameInfo.getAlly(allyID).getX() - nearestObstacle.x) * distance / minDistance;
         float newY = nearestObstacle.y + (GameInfo.getAlly(allyID).getY() - nearestObstacle.y) * distance / minDistance;
 
         Vector2d endLoc = new Vector2d(newX, newY);

        return endLoc;
    }

}
