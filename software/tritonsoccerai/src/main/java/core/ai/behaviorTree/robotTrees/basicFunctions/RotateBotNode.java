// package core.ai.behaviorTree.robotTrees.basicFunctions;
package main.java.core.ai.behaviorTree.robotTrees.basicFunctions;

import java.util.LinkedList;

import main.java.core.ai.GameInfo;
import main.java.core.ai.behaviorTree.nodes.NodeState;
import main.java.core.constants.ProgramConstants;
import main.java.core.search.node2d.Node2d;
import main.java.core.ai.behaviorTree.nodes.taskNodes.TaskNode;
import proto.simulation.SslSimulationRobotControl.RobotCommand;
import static main.java.core.messaging.Exchange.AI_BIASED_ROBOT_COMMAND;
import static main.java.core.util.ObjectHelper.generateLocalMoveCommand;


public class RotateBotNode extends TaskNode{
    
    public RotateBotNode(int allyID){
        super("RotateBotNode" + allyID, allyID);
    }

    @Override
    public NodeState execute() {
        // Rotate Bot

        int allyID = 0;

        RobotCommand localCommand = generateLocalMoveCommand(0f, 0f, 1f,
                                                            0f, allyID);

        // Publish command to robot
        ProgramConstants.commandPublishingModule.publish(AI_BIASED_ROBOT_COMMAND, localCommand);

        return NodeState.SUCCESS;
    }

    public NodeState execute(float angular_velocity) {
        // Rotate Bot

        int allyID = 0;

        RobotCommand localCommand = generateLocalMoveCommand(0f, 0f, angular_velocity, 0f, allyID);

        // Publish command to robot
        ProgramConstants.commandPublishingModule.publish(AI_BIASED_ROBOT_COMMAND, localCommand);

        return NodeState.SUCCESS;
    }
}