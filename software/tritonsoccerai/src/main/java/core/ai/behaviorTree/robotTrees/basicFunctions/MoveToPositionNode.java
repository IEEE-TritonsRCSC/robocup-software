package main.java.core.ai.behaviorTree.robotTrees.basicFunctions;

import java.util.LinkedList;

import main.java.core.ai.GameInfo;
import main.java.core.ai.behaviorTree.nodes.NodeState;
import main.java.core.ai.behaviorTree.nodes.taskNodes.TaskNode;
import main.java.core.util.Vector2d;
import main.java.core.search.implementation.*;
import main.java.core.search.node2d.Node2d;
import main.java.core.constants.ProgramConstants;

import static proto.triton.FilteredObject.Robot;
import static proto.simulation.SslSimulationRobotControl.RobotCommand;
import static proto.simulation.SslSimulationRobotControl.RobotMoveCommand;
import static proto.simulation.SslSimulationRobotControl.MoveGlobalVelocity;

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

    public NodeState execute(Vector2d endLoc) {
        Vector2d allyPos = getPos(super.ally);

        // Pathfinding to endLoc
        LinkedList<Node2d> route = pathfindGridGroup.findRoute(ally.getId(), allyPos, endLoc);
        Vector2d next = pathfindGridGroup.findNext(ally.getId(), route);

        // Build robot command to be published
        RobotCommand.Builder robotCommand = RobotCommand.newBuilder();
        robotCommand.setId(ally.getId());
        RobotMoveCommand.Builder moveCommand = RobotMoveCommand.newBuilder();
        MoveGlobalVelocity.Builder globalVelocity = MoveGlobalVelocity.newBuilder();
        Vector2d vel = endLoc.sub(allyPos).scale((float) 0.1);
        globalVelocity.setX(vel.x);
        globalVelocity.setY(vel.y);
        //globalVelocity.setAngular(angular);
        globalVelocity.setAngular(3.0f);
        moveCommand.setGlobalVelocity(globalVelocity);
        robotCommand.setMoveCommand(moveCommand);

        // Publish command to robot
        ProgramConstants.aiModule.publish(ProgramConstants.moduleToPublishAICommands, robotCommand.build());

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
