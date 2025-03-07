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


    /*@Override
    public void run(){
        // System.out.println("CoordinatedPassNode.run()");
        this.execute(false);
    }*/


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
        Vector2d direction = new Vector2d(message.getPassLocX(), message.getPassLocY());
        ProgramConstants.commandPublishingModule.publish(Exchange.CENTRAL_COORDINATOR_PASSING, message);
        // this.spinBall.execute();
        double dy = (double) GameInfo.getBall().getY() - direction.y;
        double dx = (double) GameInfo.getBall().getX() - direction.x;
        
        // Set a fixed final orientation for demonstration purposes
        float kP = 0.6f;
        float final_orientation = (float) (Math.atan2(dy,dx));
        
        // Log the calculated dx, dy, and final orientation
        // System.out.println("dx: " + dx);
        // System.out.println("dy: " + dy);
        // System.out.println("final: " + final_orientation);
        
        // Retrieve the current orientation of the ally
        float orientation = GameInfo.getAlly(this.passerID).getOrientation();
        // Normalize orientation to be within [0, 2*PI]
        if (orientation < 0) {
            orientation += 2 * Math.PI;
        }
        
        // Calculate the initial difference in orientation
        float diff = final_orientation - orientation;
        
        // Adjust orientation until the difference is within a threshold
        //if (Math.abs(diff) > 0.5) {
            // Execute rotation command with proportional control (kP is assumed to be defined elsewhere)
            // this.RotateBot.execute(diff * kP);
            // this.spinBall.execute();
            
            // Update the orientation and difference for the next iteration
            /* orientation = GameInfo.getAlly(this.passerID).getOrientation();
            if (orientation < 0) {
                orientation += 2 * Math.PI;
            }
            diff = final_orientation - orientation;*/
            
            // Log the current orientation and difference
            // System.out.println("orientation: " + orientation);
            // System.out.println("diff: " + diff);
        //} 
        //else {
            NodeState result = this.kickBall.execute(direction, RobotConstants.MAX_KICK_VELOCITY, false);
            if (result == NodeState.SUCCESS) {System.out.println("Passed ball.");}
        //}
        return NodeState.SUCCESS;
    }


    /**
     * Identifies the best pass receiver
     */
    private CoordinatedPassInfo.CoordinatedPass getPassInfo() {
        // Might need to edit later to work with Proto Robots
        ArrayList<Robot> foesList = new ArrayList<>(GameInfo.getFoeFielders());
        ArrayList<Robot> alliesList = new ArrayList<>(GameInfo.getFielders());
        List<Robot> obstacles = new ArrayList<>();

        //remove the passer
        alliesList.remove(GameInfo.getAlly(passerID));


        //add allys to the target list
        List<Vector2d> passLocs = new ArrayList<>();
        for(int i = 0; i < alliesList.size(); i++) {
            passLocs.add(getPos(alliesList.get(i)));
        }


        //add foes to the obstacles list
        obstacles.addAll(foesList);


        // System.out.println("Allies_list:" + alliesList);
        // // Sleep for 10 seconds
        // try {
        //     Thread.sleep(10000);
        // }
        // catch (InterruptedException e) {
        //     e.printStackTrace();
        // }


        //best kick direction
        int bestLocIndex = 0;


        float maxScore = Float.MIN_VALUE;


        // Choose the best pass receiver from allysList
        for (int i = 0; i < passLocs.size(); i++) {
            Vector2d loc = passLocs.get(i);
            float distToObstacles = distToPath(getPos(GameInfo.getAlly(passerID)), loc, obstacles);


            // TODO Maybe have to change how to calculate the score
            float score = aiConfig.passDistToObstaclesScoreFactor * distToObstacles;


            if (score > maxScore) {
                bestLocIndex = i;
                maxScore = score;
            }
        }


        CoordinatedPassInfo.CoordinatedPass.Builder passInfo = CoordinatedPassInfo.CoordinatedPass.newBuilder();
        passInfo.setReceiverID(alliesList.get(bestLocIndex).getId());
        passInfo.setSenderID(this.passerID);
        passInfo.setPassLocX(passLocs.get(bestLocIndex).x);
        passInfo.setPassLocY(passLocs.get(bestLocIndex).y);

        // System.out.println(passInfo.getReceiverID());

        return passInfo.build();
    }


    private float distance(float x1, float y1, float x2, float y2){
        return (float) Math.pow((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2), 0.5);
    }
}



