package main.java.core.ai.behaviorTree.robotTrees.basicFunctions;

import main.java.core.ai.behaviorTree.nodes.NodeState;
import main.java.core.ai.behaviorTree.nodes.taskNodes.TaskNode;
import main.java.core.constants.ProgramConstants;
import main.java.core.util.Vector2d;
import main.java.core.search.implementation.PathfindGridGroup;
import main.java.core.ai.behaviorTree.robotTrees.basicFunctions.MoveToPositionNode;
import main.java.core.ai.behaviorTree.robotTrees.basicFunctions.ChaseBallNode;

import main.java.core.ai.GameInfo;

import static main.java.core.messaging.Exchange.AI_BIASED_ROBOT_COMMAND;
import static main.java.core.constants.RobotConstants.*;

import proto.simulation.SslSimulationRobotControl;
import static proto.triton.FilteredObject.Robot;

import static main.java.core.util.ProtobufUtils.getPos;

/**
 * Defines tasks to be performed to dribble ball
 */
public class DribbleBallNode extends TaskNode {

    PathfindGridGroup pathfindGridGroup;
    private final MoveToPositionNode moveToPositionNode;
    private final RotateInPlaceNode rotateNode;
    private final ChaseBallNode chaseBallNode;

    public DribbleBallNode(int allyID) {
        super("Dribble Ball Node: " + allyID, allyID);
        this.moveToPositionNode = new MoveToPositionNode(allyID);
        this.moveToPositionNode.setDribbleOn(true);
        this.rotateNode = new RotateInPlaceNode(allyID);
        this.rotateNode.setDribbleOn(true);
        this.chaseBallNode = new ChaseBallNode(allyID);
    }

     /**
      * Move the kicker at speed that keeps the ball close to the robot.
      * Publish the robot command to execute dribble. 
      */
    @Override
    public NodeState execute() {
        // Set dribbler speed and publish the command
        SslSimulationRobotControl.RobotCommand.Builder robotCommand = SslSimulationRobotControl.RobotCommand.newBuilder();

        robotCommand.setId(allyID);
        robotCommand.setDribblerSpeed(DRIBBLE_RPM);
        robotCommand.setKickSpeed(0);

        ProgramConstants.commandPublishingModule.publish(AI_BIASED_ROBOT_COMMAND, robotCommand.build());

        return NodeState.SUCCESS;
    }

    public NodeState execute(Vector2d location) {
        if (getPos(GameInfo.getAlly(allyID)).dist(getPos(GameInfo.getBall())) > (1.0 * DRIBBLE_THRESHOLD)) {
            this.chaseBallNode.execute();
        }
        else {
            this.execute();
            float orientation = this.moveToPositionNode.execute(location);
            if (orientation != 0.0f) {
                this.rotateNode.execute(orientation, true);
            }
        }
        
        return NodeState.SUCCESS;
    }

}
