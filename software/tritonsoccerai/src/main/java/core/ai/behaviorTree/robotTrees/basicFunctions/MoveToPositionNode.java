package main.java.core.ai.behaviorTree.robotTrees.basicFunctions;

import java.util.LinkedList;

import main.java.core.ai.GameInfo;
import main.java.core.ai.behaviorTree.nodes.NodeState;
import main.java.core.ai.behaviorTree.nodes.taskNodes.TaskNode;
import main.java.core.util.Vector2d;
import main.java.core.search.implementation.*;
import main.java.core.search.node2d.Node2d;
import main.java.core.constants.ProgramConstants;

import static main.java.core.messaging.Exchange.AI_BIASED_ROBOT_COMMAND;

import static proto.triton.FilteredObject.Robot;
import static proto.simulation.SslSimulationRobotControl.RobotCommand;
import static proto.simulation.SslSimulationRobotControl.RobotMoveCommand;
import static proto.simulation.SslSimulationRobotControl.MoveLocalVelocity;

import static main.java.core.util.ProtobufUtils.getPos;

public class MoveToPositionNode extends TaskNode {

    PathfindGridGroup pathfindGridGroup;
    
    public MoveToPositionNode(Robot ally) {
        super("Move To Position Node: " + ally, ally);
        this.pathfindGridGroup = new PathfindGridGroup(ProgramConstants.gameConfig.numBots, GameInfo.getField());
    }

    @Override
    public NodeState execute() {
        return null;
    }

    // TODO: Fix this method
    // Use global velocities to calculate local velocities
    // Either pass the command to TritonBotMessageBuilder or
    // SimulatorRobotCommandInterface based on if in competition
    // vs. simulator setup respectively
    public NodeState execute(Vector2d endLoc) {
        Vector2d allyPos = getPos(super.ally);

        // Pathfinding to endLoc
        LinkedList<Node2d> route = pathfindGridGroup.findRoute(ally.getId(), allyPos, endLoc);
        Vector2d next = pathfindGridGroup.findNext(ally.getId(), route);

        // Build robot command to be published
        RobotCommand.Builder robotCommand = RobotCommand.newBuilder();
        robotCommand.setId(ally.getId());
        RobotMoveCommand.Builder moveCommand = RobotMoveCommand.newBuilder();
        MoveLocalVelocity.Builder localVelocity = MoveLocalVelocity.newBuilder();
        Vector2d vel = endLoc.sub(allyPos).scale((float) 0.1);
        localVelocity.setForward(vel.x);
        localVelocity.setLeft(vel.y);
        //globalVelocity.setAngular(angular);
        localVelocity.setAngular(3.0f);
        moveCommand.setLocalVelocity(localVelocity);
        robotCommand.setMoveCommand(moveCommand);

        // Publish command to robot
        ProgramConstants.aiModule.publish(AI_BIASED_ROBOT_COMMAND, robotCommand.build());

        return NodeState.SUCCESS;
    }

    /**
     * Execute with target location set to (0, 0)
     */
    @Override
    public void run() {
        execute(new Vector2d(0, 0));
    }

}
