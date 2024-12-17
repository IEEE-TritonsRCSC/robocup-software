package core.ai.behaviorTree.robotTrees.fielder.specificStateFunctions;

import core.ai.behaviorTree.nodes.NodeState;
import core.ai.behaviorTree.nodes.compositeNodes.SequenceNode;
import core.ai.behaviorTree.robotTrees.basicFunctions.MoveToPositionNode;
import core.ai.GameInfo;
import static proto.triton.FilteredObject.Robot;
import core.util.Vector2d;

import core.constants.ProgramConstants;

import static core.messaging.Exchange.AI_BIASED_ROBOT_COMMAND;

import static proto.simulation.SslSimulationRobotControl.RobotCommand;
import static proto.simulation.SslSimulationRobotControl.MoveLocalVelocity;

import static core.util.ObjectHelper.generateLocalMoveCommand;
import static core.util.ProtobufUtils.getPos;

/**
 * Handles Halt game state
 * Stops robot within 2 seconds without manipulating ball
 */
public class HaltNode extends SequenceNode {

    private final int allyID;
    private final MoveToPositionNode moveToPositionNode;

    public HaltNode(int allyID) {
        super("Halt Node: " + allyID);
        this.allyID = allyID;
        this.moveToPositionNode = new MoveToPositionNode(allyID);
    }

    @Override
    public NodeState execute() {
        // move to current location until moving slow enough
        float MAX_VEL_CONSTANT = 2.0f;
        /*while(new Vector2d(GameInfo.getAlly(allyID).getVx(), GameInfo.getAlly(allyID).getVy()).mag() < MAX_VEL_CONSTANT) {
            this.moveToPositionNode.execute(getPos(GameInfo.getAlly(allyID)));
        }*/
        RobotCommand localCommand = generateLocalMoveCommand(0, 0, 0.0f, 
                                                    GameInfo.getAlly(allyID).getOrientation(), allyID);
        localCommand = localCommand.toBuilder()
                                    .setDribblerSpeed(0f)
                                    .build();
        ProgramConstants.commandPublishingModule.publish(AI_BIASED_ROBOT_COMMAND, localCommand);
        return NodeState.SUCCESS;
    }

}
