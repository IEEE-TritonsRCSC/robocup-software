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

public class MoveToPositionNode extends TaskNode {

    PathfindGridGroup pathfindGridGroup;
    Vector2d targetLocation;
    
    public MoveToPositionNode(int allyID) {
        super("Move To Position Node: " + allyID, allyID);
        this.pathfindGridGroup = new PathfindGridGroup(ProgramConstants.gameConfig.numBots, GameInfo.getField());
    }

    @Override
    public NodeState execute() {
        return null;
    }

    public NodeState execute(Vector2d endLoc) {
        Robot ally = GameInfo.getAlly(allyID);
        Ball ball = GameInfo.getBall();
        float targetOrientation = (float) Math.atan2(ball.getY() - ally.getY(), ball.getX() - ally.getX());
        return execute(endLoc, targetOrientation);
    }

    public NodeState execute(Vector2d endLoc, float targetOrientation) {
        Vector2d allyPos = getPos(GameInfo.getAlly(allyID));

        // Pathfinding to endLoc
        pathfindGridGroup.updateObstacles(GameInfo.getWrapper());
        LinkedList<Node2d> route = pathfindGridGroup.findRoute(allyID, allyPos, endLoc);
        Vector2d next = pathfindGridGroup.findNext(allyID, route);

        if (next == null) {
            return NodeState.FAILURE;
        }

        // Build robot command to be published
        Vector2d vel = next.sub(allyPos).scale(RobotConstants.MOVE_VELOCITY_DAMPENER);
        float angular = 3.0f * (Vector2d.angleDifference(GameInfo.getAlly(allyID).getOrientation(), targetOrientation));
        RobotCommand localCommand = generateLocalMoveCommand(vel.x, vel.y, angular, 
                                                            GameInfo.getAlly(allyID).getOrientation(), allyID);

        // Publish command to robot
        ProgramConstants.commandPublishingModule.publish(AI_BIASED_ROBOT_COMMAND, localCommand);

        return NodeState.SUCCESS;
    }

    /**
     * Execute with target location set to (0, 0)
     */
    @Override
    public void run() {
        if (this.targetLocation != null) {
            execute(this.targetLocation);
        } else {
            execute(new Vector2d(0, 0));
        }
    }

    public void setTargetLocation(Vector2d targetLocation) {
        this.targetLocation = targetLocation;
    }

}
