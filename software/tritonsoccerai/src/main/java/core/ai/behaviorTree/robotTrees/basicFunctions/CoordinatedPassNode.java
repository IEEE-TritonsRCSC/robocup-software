package core.ai.behaviorTree.robotTrees.basicFunctions;


import core.ai.GameInfo;
import core.ai.behaviorTree.nodes.NodeState;
import core.ai.behaviorTree.nodes.compositeNodes.SequenceNode;
import core.ai.behaviorTree.robotTrees.basicFunctions.RotateBotNode;
import core.ai.behaviorTree.robotTrees.basicFunctions.RotateInPlaceNode;
import core.constants.RobotConstants;
import core.messaging.Exchange;


import static core.constants.ProgramConstants.aiConfig;
import static core.util.ObjectHelper.distToPath;
import static core.util.ProtobufUtils.getPos;
import core.util.Vector2d;
import proto.triton.CoordinatedPassInfo;
import proto.triton.FilteredObject.Robot;


import java.util.ArrayList;
import java.util.List;


import core.constants.ProgramConstants;


import static proto.triton.FilteredObject.*;
import static proto.triton.CoordinatedPassInfo.CoordinatedPass;


 /**
 * Defines the sequence of tasks needed to successfully perform a coordinated pass between two allies.
 * It is assumed that the passer kicks the ball from the spot when he has it.
 */
public class CoordinatedPassNode extends SequenceNode {


    public final int passerID;
    private final KickBallNode kickBall;
    //private final RotateInPlaceNode spinBall;
    //private final RotateBotNode RotateBot;


    public CoordinatedPassNode(int passerID) {
        super("Coordinated Pass Node: " + passerID);
        this.passerID = passerID;
        this.kickBall = new KickBallNode(passerID);
        //this.spinBall = new RotateInPlaceNode(passerID);
        //this.RotateBot = new RotateBotNode(passerID);
    }


    /**
     * 1. Identifies the best pass receiver.
     * 2. Sends message to the central coordinator with pass details.
     * 3. Performs pass
     */
    @Override
    public NodeState execute() {
        return execute(false);
    }


    public NodeState execute(boolean chip) {
        // TODO Send message to central coordinator with pass details


        //kicks the ball to the pass receiver
        CoordinatedPassInfo.CoordinatedPass message = getPassInfo();
        if(message == null) return NodeState.FAILURE;

        Vector2d receiverPos = new Vector2d(message.getPassLocX(), message.getPassLocY());
        Vector2d passerPos = getPos(GameInfo.getAlly(passerID));
        
        // Orientation Alignment
        //Computes the desired angle to face the receiver.
        float targetOrientation = (float) Math.atan2(receiverPos.y - passerPos.y, receiverPos.x - passerPos.x);
        //Determines the current orientation of the passer.
        float currentOrientation = GameInfo.getAlly(passerID).getOrientation();
        //angleDiff between receiver and passer
        float angleDiff = Vector2d.angleDifference(currentOrientation, targetOrientation);
        
        //Proportional orientation control adjusts rotation speed based on angle difference.
        float kP = 1.2f;
        //if angle difference is too big, it rotates and returns 
        double angleTooBig = Math.toRadians(2.5);
        if(Math.abs(angleDiff) > angleTooBig) {
            float angularVel = (float) (Math.atan2(Math.sin(angleDiff), Math.cos(angleDiff)) * kP);
            new RotateBotNode(passerID).execute(angularVel);
            return NodeState.RUNNING;
        }

        // Physics-Based Kick Force
        //value of distance between passer and receiver
        double distance = passerPos.dist(receiverPos);
        //uhh not sure if we can adapt kick velocity right now... kicker moves at const speed
        double kickVelocity = calculateOptimalApproachVelocity(distance);
        
        // Execute kick towards receiver 
        NodeState result = this.kickBall.execute(receiverPos, kickVelocity, false);
        return result == NodeState.SUCCESS ? NodeState.SUCCESS : NodeState.FAILURE;
    }

    //identifying the best ally to pass to
    private CoordinatedPassInfo.CoordinatedPass getPassInfo() {
        //retrieving all the allies
        ArrayList<Robot> allies = new ArrayList<>(GameInfo.getFielders());
        allies.remove(GameInfo.getAlly(passerID));
        if(allies.isEmpty()) return null;

        // Scoring System Components 
        Vector2d passerPos = getPos(GameInfo.getAlly(passerID));
        List<Robot> foes = GameInfo.getFoeFielders();
        //store enemies as obstacles
        List<Robot> obstacles = new ArrayList<>(foes);
        
        //initializing variables for best receiver, we keep track using a score
        float maxScore = Float.MIN_VALUE;
        //index of best ally to pass to 
        int bestIndex = -1;
        
        for(int i = 0; i < allies.size(); i++) {
            Vector2d receiverPos = getPos(allies.get(i));
            
            // check that path is clear! if not don't continue
            if(!isPassPathClear(passerPos, receiverPos, obstacles)) continue;
            
            // base score: penalizes passes that go near obstacles
            //this was how they calculated the score previously 
            float baseScore = aiConfig.passDistToObstaclesScoreFactor * 
                                distToPath(passerPos, receiverPos, foes);
                           
            //favors players closer to the goalkeeper of the opponent (bc they're near the foe goal)
            float positionalAdvantage = 1.5f / 
                (float) receiverPos.dist(getPos(GameInfo.getFoeKeeper()));
                
            
            //penalizes passes likely to be intercepted
            float interceptionRisk = calculateInterceptionRisk(passerPos, receiverPos, foes);
            
            float totalScore = baseScore + positionalAdvantage - interceptionRisk;
            
            //store the maximum score here, with index of best ally to pass to
            if(totalScore > maxScore) {
                maxScore = totalScore;
                bestIndex = i;
            }
        }

        if(bestIndex == -1) return null;
        
        //TODO: need to setpassvelocityinfo!! update in coordinatedpassinfo
        // return CoordinatedPassInfo.CoordinatedPass.newBuilder()
        //     .setReceiverID(allies.get(bestIndex).getId())
        //     .setSenderID(passerID)
        //     .setPassLocX(getPos(allies.get(bestIndex)).x)
        //     .setPassLocY(getPos(allies.get(bestIndex)).y)
        //     .setPassVelocity((float) calculateOptimalApproachVelocity(
        //         passerPos.dist(getPos(allies.get(bestIndex)))
        //     )).build();

        // building a CoordinatedPassInfo object that contains all the pass details
        return CoordinatedPassInfo.CoordinatedPass.newBuilder()
            .setReceiverID(allies.get(bestIndex).getId())
            .setSenderID(passerID)
            .setPassLocX(getPos(allies.get(bestIndex)).x)
            .setPassLocY(getPos(allies.get(bestIndex)).y).build();
    }

    // Helper Methods
    
    //TODO: talk to mechanical about how we should approach writing this function 
    //Uses distance-based scaling to balance speed and control.
    // private double calculateOptimalKickVelocity(double distance) {
    //     double minVel = 1.5;  // Minimum effective pass speed
    //     double maxVel = RobotConstants.MAX_KICK_VELOCITY;
    //     double ideal = distance * 1.5;  //linear increase in ideal velocity
    //     return Math.min(maxVel, Math.max(minVel, ideal + 0.2 * Math.pow(distance, 1.2)));
    // }

    //might want to calculate optimal kick distance instead because velocity is constant
    // ensures fast approach for short distances and gradual approach for long distances.
    private double calculateOptimalApproachVelocity(double distance) {
        double minVel = 100;  // Minimum velocity so the robot is always moving
        double maxVel = RobotConstants.MAX_KICK_VELOCITY; // 3200 in your case
        double k = 0.5;  //k controls how quickly the velocity drops with distance

        return Math.max(minVel, maxVel / (1 + k * distance));
    }

    //use interpolation to check if there are any obstacles along the passing vector we've defined
    private boolean isPassPathClear(Vector2d start, Vector2d end, List<Robot> obstacles) {
        final float SAFETY_MARGIN = 0.6f;
        final double STEP_SIZE = 1.0;  // adjust based on field size
        
        double distance = start.dist(end);
        //the steps are the no of times we perform a check along the passing vector
        int steps = Math.max(2, (int)(distance / STEP_SIZE)); // ensure at least 2 checks
        
        for (int i = 0; i <= steps; i++) {
            double t = (double) i / steps;
            Vector2d point = start.scale((float)(1 - t)).add(end.scale((float)t)); 
            // manual interpolation

            //looping through every robot in obstacle list to check 
            //if any of them are too close to a specific point
            //basically we're comparing every point along the pass vector with the obstacle robots 
            for (Robot robot : obstacles) {
                //if any obstacle is found within the distance of safety_margin, path is not clear
                if (point.dist(getPos(robot)) < SAFETY_MARGIN) {
                    return false; // obstacle detected
                }
            }
        }
        return true; // no obstacles
    }

    private float calculateInterceptionRisk(Vector2d passerPos, Vector2d receiverPos, List<Robot> foes) {
        Vector2d center = passerPos.add(receiverPos).scale(0.5f); //take midpoint, highest interception probability
        //defining the Interception Ellipse
        double a = passerPos.dist(receiverPos)/2;  
        // major axis, half the distance between passer and receiver, long side of ellipsis
        double b = 2.0;                                
        // Minor axis, short side of ellipsis, fixed value, can increase if necessary
        //want to ensure ellipsis is elongated along the pass direction
        
        float risk = 0;
        //for every single enemy robot in our foe list, check if they are in the interception zone
        for(Robot foe : foes) {
            Vector2d p = getPos(foe);
            double dx = (p.x - center.x)/a;
            double dy = (p.y - center.y)/b;
            //translates the opponent’s position relative to the ellipse center.
            //scales their x and y distances by the ellipse’s major and minor axes
            //if a point satisfies dx² + dy² < 1, it means it lies inside the ellipse
            if(dx*dx + dy*dy < 1) risk += 2.0f;  // in interception ellipse
        }
        return risk;
    }
}



