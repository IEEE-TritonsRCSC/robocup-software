package java.core.ai.behaviorTree.robotTrees.basicFunctions;

import java.util.LinkedList;

import java.core.ai.GameInfo;
import java.core.ai.behaviorTree.nodes.NodeState;
import java.core.ai.behaviorTree.nodes.taskNodes.TaskNode;
import java.core.util.Vector2d;
import java.core.search.implementation.*;
import java.core.search.node2d.Node2d;

import static proto.triton.FilteredObject.Robot;
import static proto.simulation.SslSimulationRobotControl.RobotCommand;
import static proto.simulation.SslSimulationRobotControl.RobotMoveCommand;
import static proto.simulation.SslSimulationRobotControl.MoveGlobalVelocity;

import static java.core.messaging.Exchange.AI_BIASED_ROBOT_COMMAND;
import static java.core.util.ProtobufUtils.getPos;

public class MoveToPositionNode extends TaskNode {

    PathfindGridGroup pathfindGridGroup;
    
    public MoveToPositionNode(Robot ally) {
        super("Move To Position Node: " + ally, ally);
    }

    @Override
    public NodeState execute() {
        return null;
    }

    public NodeState execute(Vector2d endLoc) {
        Vector2d allyPos = getPos(super.ally);

        // Pathfinding to endLoc
        LinkedList<Node2d> route = pathfindGridGroup.findRoute(ally.getId(), allyPos, endLoc);
        Vector2d next = pathfindGridGroup.findNext(ally.getId(), route);

        // Build robot command to be published
        SslSimulationRobotControl.RobotCommand.Builder robotCommand = SslSimulationRobotControl.RobotCommand.newBuilder();
        robotCommand.setId(ally.getId());
        SslSimulationRobotControl.RobotMoveCommand.Builder moveCommand = SslSimulationRobotControl.RobotMoveCommand.newBuilder();
        SslSimulationRobotControl.MoveGlobalVelocity.Builder globalVelocity = SslSimulationRobotControl.MoveGlobalVelocity.newBuilder();
        globalVelocity.setX(vel.x);
        globalVelocity.setY(vel.y);
        globalVelocity.setAngular(angular);
        moveCommand.setGlobalVelocity(globalVelocity);
        robotCommand.setMoveCommand(moveCommand);

        // Publish command to robot
        publish(AI_BIASED_ROBOT_COMMAND, robotCommand.build());
        return NodeState.SUCCESS;
    }
}
