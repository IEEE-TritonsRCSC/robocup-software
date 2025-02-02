package core.ai.behaviorTree.robotTrees.basicFunctions;

import java.util.LinkedList;

import core.ai.GameInfo;
import core.ai.behaviorTree.nodes.NodeState;
import core.ai.behaviorTree.nodes.taskNodes.TaskNode;
import core.util.Vector2d;
import core.search.implementation.*;
import core.search.node2d.Node2d;
import core.constants.ProgramConstants;
import static core.constants.ProgramConstants.aiConfig;
import core.constants.RobotConstants;

import static core.messaging.Exchange.AI_BIASED_ROBOT_COMMAND;

import static proto.triton.FilteredObject.*;
import static proto.simulation.SslSimulationRobotControl.RobotCommand;
import static proto.simulation.SslSimulationRobotControl.RobotMoveCommand;
import static proto.simulation.SslSimulationRobotControl.MoveLocalVelocity;

import static core.util.ProtobufUtils.getPos;
import static core.util.ObjectHelper.generateLocalMoveCommand;

public class MoveToPositionNode extends TaskNode {

    PathfindGridGroup pathfindGridGroup;
    Vector2d targetLocation;
    boolean dribbleOn;
    boolean avoidBall;
    
    public MoveToPositionNode(int allyID) {
        super("Move To Position Node: " + allyID, allyID);
        this.pathfindGridGroup = new PathfindGridGroup(ProgramConstants.gameConfig.numBots, GameInfo.getField());
        this.dribbleOn = false;
        this.avoidBall = false;
    }

    @Override
    public NodeState execute() {
        return null;
    }

    public NodeState execute(Vector2d endLoc) {
        Robot ally = GameInfo.getAlly(allyID);
        Ball ball = GameInfo.getBall();
        
        Vector2d allyPos = getPos(GameInfo.getAlly(allyID));

        // Pathfinding to endLoc
        pathfindGridGroup.updateObstacles(GameInfo.getWrapper(), this.avoidBall);
        LinkedList<Node2d> route = pathfindGridGroup.findRoute(allyID, allyPos, endLoc);
        Vector2d next = pathfindGridGroup.findNext(allyID, route);

        if (next == null) {
            return NodeState.FAILURE;
        }

        // Build robot command to be published
        Vector2d direction = next.sub(allyPos);
        Vector2d vel = direction;
        
        if (this.dribbleOn) {
            float mag = direction.mag();
            vel = direction.norm().scale(Math.min(mag, RobotConstants.MAX_DRIBBLE_MOVE_VELOCITY));
        }
        vel = vel.scale(RobotConstants.MOVE_VELOCITY_DAMPENER);

        float targetOrientation;
        if (this.dribbleOn) {targetOrientation = (float) Math.atan2(next.y - ally.getY(), next.x - ally.getX());}
        else {targetOrientation = (float) Math.atan2(ball.getY() - ally.getY(), ball.getX() - ally.getX());}

        float angular = 3.0f * (Vector2d.angleDifference(GameInfo.getAlly(allyID).getOrientation(), targetOrientation));
        RobotCommand localCommand = generateLocalMoveCommand(vel.x, vel.y, angular, 
                                                            GameInfo.getAlly(allyID).getOrientation(), allyID);
        if (this.dribbleOn) {
            localCommand = localCommand.toBuilder().setDribblerSpeed(RobotConstants.DRIBBLE_RPM).build();
        }

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

    /**
     * Sets the target location of the MoveToPositionNode (otherwise (0, 0) by default)
     * @param targetLocation the new target location
     */
    public void setTargetLocation(Vector2d targetLocation) {
        this.targetLocation = targetLocation;
    }

    /**
     * Sets the dribble setting
     * @param dribbleOn whether robot velocity should be restricted to max dribbling speed
     */
    public void setDribbleOn(boolean dribbleOn) {
        this.dribbleOn = dribbleOn;
    }

    /**
     * Sets the dribble setting
     * @param dribbleOn whether robot velocity should be restricted to max dribbling speed
     */
    public void setAvoidBall(boolean avoidBall) {
        // this.avoidBall = avoidBall;
    }

}
