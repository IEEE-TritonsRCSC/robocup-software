package main.java.core.ai.behaviorTree.robotTrees.basicFunctions;

import java.util.LinkedList;

import main.java.core.ai.GameInfo;
import main.java.core.ai.behaviorTree.nodes.NodeState;
import main.java.core.ai.behaviorTree.nodes.taskNodes.TaskNode;
import main.java.core.util.Vector2d;
import main.java.core.search.implementation.*;
import main.java.core.search.node2d.Node2d;
import main.java.core.constants.ProgramConstants;
import static main.java.core.constants.ProgramConstants.aiConfig;
import main.java.core.constants.RobotConstants;

import static main.java.core.messaging.Exchange.AI_BIASED_ROBOT_COMMAND;

import static proto.triton.FilteredObject.*;
import static proto.simulation.SslSimulationRobotControl.RobotCommand;
import static proto.simulation.SslSimulationRobotControl.RobotMoveCommand;
import static proto.simulation.SslSimulationRobotControl.MoveLocalVelocity;

import static main.java.core.util.ProtobufUtils.getPos;
import static main.java.core.util.ObjectHelper.generateLocalMoveCommand;

public class RotateInPlaceNode extends TaskNode {

    boolean dribbleOn;
    
    public RotateInPlaceNode(int allyID) {
        super("Rotate In Place Node: " + allyID, allyID);
        this.dribbleOn = false;
    }

    @Override
    public NodeState execute() {
        float angular = 3.0f;
        if (dribbleOn) {angular = RobotConstants.MAX_DRIBBLE_ROTATE_ANGULAR;}
        execute(angular, false);

        return NodeState.SUCCESS;
    }

    public static float getAngular(float targetOrientation, int allyID, boolean dribbleOn) {
        float angular = 3.0f * (Vector2d.angleDifference(GameInfo.getAlly(allyID).getOrientation(), targetOrientation));
        // if (dribbleOn) {
        //     angular = angular / Math.abs(angular) * Math.min(Math.abs(angular), RobotConstants.MAX_DRIBBLE_ROTATE_ANGULAR);
        // }
        return angular;
    }

    public NodeState execute(float input, boolean calcAngular) {
        float angular = input; // angular velocity (radians/s)
        if (calcAngular) {
            angular = RotateInPlaceNode.getAngular(input, allyID, dribbleOn);
        }
        // float forward = 0.0f;
        // if (this.dribbleOn) {
        //     forward = 0.02f;
        // }
        
        RobotCommand localCommand = generateLocalMoveCommand(0.0f, 0.0f, angular, 
                                                            GameInfo.getAlly(allyID).getOrientation(), allyID);
        if (this.dribbleOn) {
            localCommand = localCommand.toBuilder().setDribblerSpeed(RobotConstants.DRIBBLE_RPM).build();
        }

        // Publish command to robot
        ProgramConstants.commandPublishingModule.publish(AI_BIASED_ROBOT_COMMAND, localCommand);

        return NodeState.SUCCESS;
    }

    /**
     * Sets the dribble setting
     * @param dribbleOn whether robot velocity should be restricted to max dribbling speed
     */
    public void setDribbleOn(boolean dribbleOn) {
        this.dribbleOn = dribbleOn;
    }

}