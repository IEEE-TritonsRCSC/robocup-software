package core.ai.behaviorTree.robotTrees.goalkeeper.specificStateFunctions;

import java.util.Vector;

import core.ai.GameInfo;
import core.ai.behaviorTree.nodes.NodeState;
import core.ai.behaviorTree.nodes.taskNodes.TaskNode;
import core.constants.ProgramConstants;
import core.ai.behaviorTree.robotTrees.fielder.specificStateFunctions.HaltNode;
import static core.messaging.Exchange.AI_BIASED_ROBOT_COMMAND;
import core.util.Vector2d;
import static core.util.ProtobufUtils.getPos;
import static core.util.ObjectHelper.generateLocalMoveCommand;

import static proto.simulation.SslSimulationRobotControl.RobotCommand;

public class GKHaltNode extends TaskNode {

    private final HaltNode haltNode;

    public GKHaltNode() {
        super("GK Halt Node: " + 0, 0);
        this.haltNode = new HaltNode(allyID);
    }

    @Override
    public NodeState execute() {
        // set velocity to 0
        this.haltNode.execute();
        return NodeState.SUCCESS;
    }

}
