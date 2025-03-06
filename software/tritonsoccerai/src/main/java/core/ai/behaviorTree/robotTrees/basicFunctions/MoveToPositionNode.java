package core.ai.behaviorTree.robotTrees.basicFunctions;

import java.util.LinkedList;

import core.ai.GameInfo;
import core.ai.behaviorTree.nodes.NodeState;
import core.ai.behaviorTree.nodes.taskNodes.TaskNode;
import core.util.Vector2d;
import proto.simulation.SslSimulationRobotControl.RobotCommand;
import proto.triton.FilteredObject.Ball;
import proto.triton.FilteredObject.Robot;
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

/**
 * MoveToPositionNode is a behavior tree node responsible for moving a robot 
 * to a specified target position on the field.
 * This class handles pathfinding, velocity calculation (with smoother 
 * deceleration), and angular deceleration for orientation adjustment. 
 * Commands are published to control the robot's movement and optionally 
 * activate dribbling.
 */
public class MoveToPositionNode extends TaskNode {

    PathfindGridGroup pathfindGridGroup;
    Vector2d targetLocation;
    boolean dribbleOn;
    boolean avoidBall;
    
    /**
     * Constructor for MoveToPositionNode for a specific robot (ID is allyID).
     * Initializes the pathfinding system and default configuration 
     * for movement behavior.
     * 
     * @param allyID The unique identifier of the robot that is receiving the command.
     */
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

    /**
     * Executes the move-to-position behavior for the robot, given a target location.
     * Pathfinding and deceleration is applied to generate movement commands,
     * which are then published to the robot for execution. Exponential deceleration is 
     * for the velocity of the robot, and also applied to angular deceleration when the 
     * robot is turning to its target orientation.
     * 
     * @param endLoc The target location for the robot.
     * @return The state of the node after execution:
     *         - NodeState.SUCCESS if the command is successfully executed.
     *         - NodeState.FAILURE if the pathfinding fails or no route is found.
     */
    public NodeState execute(Vector2d endLoc) {
        //ally is the current robot fyi, not their teammates
        Robot ally = GameInfo.getAlly(allyID);
        Ball ball = GameInfo.getBall();
        
        Vector2d allyPos = getPos(GameInfo.getAlly(allyID));
        
        // Pathfinding to endLoc
        //Update obstacles for the specific robot in the pathfinding grid 
        pathfindGridGroup.updateObstacles(GameInfo.getWrapper(), this.avoidBall);
        //Linkedlist of the positions leading to the target route 
        LinkedList<Node2d> route = pathfindGridGroup.findRoute(allyID, allyPos, endLoc);
        //Next target position in the pathfinding grid
        Vector2d next = pathfindGridGroup.findNext(allyID, route);

        //if there is no next target position, don't execute node
        if (next == null) {
            return NodeState.FAILURE;
        }


        // Build robot command to be published
        Vector2d direction = next.sub(allyPos);  // Direction vector from current to next position
        float distance = direction.mag();        // Distance to the next position
        Vector2d vel;          
        //obtain robot's current speed
        Vector2d current_velocity = core.util.ProtobufUtils.getVel(ally).scale(0.001f);
        float current_speed = (float) current_velocity.mag();           

        //Decelerating smoothly towards the ball
        //if dribbling is enabled (while the robot is dribbling)
        if (this.dribbleOn) {
            float maxDribbleSpeed = 2.0f;  
            float speed_target = 0.5f; //speed for first touch to approach ball
            float deceleration_constant = 8.0f; //increase for sharper deceleration
            float min_distance = 0.01f; //to avoid distance going to zero for division

        
            float effectiveDistance = Math.max(distance, min_distance); //avoid dist going to 0

            //mathematical formula to decelerate smoothly
            //as distance gets smaller, sharper deceleration
            //robot is always approaching speed_target (for first touch)
            float scaledSpeed = speed_target + ((current_speed - speed_target) * 
            (effectiveDistance / (effectiveDistance + deceleration_constant)));

            
            // Double-clamp to prevent override, velocity constraints 
            scaledSpeed = Math.min(scaledSpeed, maxDribbleSpeed); // Never exceed max
            scaledSpeed = Math.max(scaledSpeed, speed_target * 0.1f);    // never go to near zero values!!
            
            //adjusted velocity vector after normalizing and scaling direction vector 
            vel = direction.norm().scale(scaledSpeed);
            
        } else { //dribbling not enabled, robot not dribbling
            float maxMoveSpeed = 3.0f;  // Max move speed (constant)            
            float speed_target = 1.0f; //speed for first touch to approach ball
            float deceleration_constant = 10.0f; //increase for sharper deceleration
            float min_distance = 0.01f; //to avoid distance going to zero for division

            //obtain robot's current speed
            float effectiveDistance = Math.max(distance, min_distance); //avoid dist going to 0

            //mathematical formula to decelerate smoothly towards the ball
            //as distance gets smaller, sharper deceleration
            //robot is always approaching speed_target (for first touch)
            float scaledSpeed = speed_target + ((current_speed - speed_target) * 
            (effectiveDistance / (effectiveDistance + deceleration_constant)));

            
            // Double-clamp to prevent override, velocity constraints 
            scaledSpeed = Math.min(scaledSpeed, maxMoveSpeed); // Never exceed max
            scaledSpeed = Math.max(scaledSpeed, speed_target * 0.1f);    // never go to near zero values!!
           
            //adjusted velocity vector after normalizing and scaling direction vector 
            vel = direction.norm().scale(scaledSpeed);
            
            
        }

        float targetOrientation;
        if (this.dribbleOn) {targetOrientation = (float) Math.atan2(next.y - ally.getY(), next.x - ally.getX());}
        else {targetOrientation = (float) Math.atan2(ball.getY() - ally.getY(), ball.getX() - ally.getX());}

        float angular = 3.0f * (Vector2d.angleDifference(GameInfo.getAlly(allyID).getOrientation(), targetOrientation));
        //TODO: clarify how the robot rotates with mechanical team!!!!
        if (distance <= 50){
            // Calculate angle difference between current orientation and target orientation
            float angleDifference = Vector2d.angleDifference(GameInfo.getAlly(allyID).getOrientation(), targetOrientation);
            
            // Angular deceleration scaling
            float angularDecelerationRadius = RobotConstants.ANGULAR_DECELERATION_RADIUS; // Define in constants
            //ensure that the scaling factor does  not exceed 1 
            float angularScalingFactor = Math.min(1, distance / angularDecelerationRadius); // Scale angular velocity based on distance
            float scaledAngularVelocity = angularScalingFactor * angleDifference * RobotConstants.MAX_ANGULAR_VELOCITY;
            angular = 3.0f * scaledAngularVelocity;
        }
        
        // Set angular velocity
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

