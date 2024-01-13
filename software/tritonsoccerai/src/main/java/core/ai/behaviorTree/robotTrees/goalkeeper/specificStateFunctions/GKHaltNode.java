package main.java.core.ai.behaviorTree.robotTrees.goalkeeper.specificStateFunctions;

import java.util.Vector;

import main.java.core.ai.GameInfo;
import main.java.core.ai.behaviorTree.nodes.NodeState;
import main.java.core.ai.behaviorTree.nodes.taskNodes.TaskNode;
import main.java.core.constants.ProgramConstants;
import static main.java.core.messaging.Exchange.AI_BIASED_ROBOT_COMMAND;
import main.java.core.util.Vector2d;
import static main.java.core.util.ProtobufUtils.getPos;
import static main.java.core.util.ObjectHelper.generateLocalMoveCommand;

import static proto.simulation.SslSimulationRobotControl.RobotCommand;

public class GKHaltNode extends TaskNode {

    public GKHaltNode() {
        super("GK Halt Node: " + 0, 0);
    }

    @Override
    public NodeState execute() {
        // set velocity to 0
        RobotCommand localCommand = generateLocalMoveCommand(0, 0, 0.0f, 
                                                    GameInfo.getAlly(allyID).getOrientation(), allyID);
        ProgramConstants.commandPublishingModule.publish(AI_BIASED_ROBOT_COMMAND, localCommand);
        return NodeState.SUCCESS;
    }

}
