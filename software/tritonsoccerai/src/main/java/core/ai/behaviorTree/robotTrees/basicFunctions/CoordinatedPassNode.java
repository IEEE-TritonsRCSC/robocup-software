package main.java.core.ai.behaviorTree.robotTrees.basicFunctions;


import main.java.core.ai.GameInfo;
import main.java.core.ai.behaviorTree.nodes.NodeState;
import main.java.core.ai.behaviorTree.nodes.compositeNodes.SequenceNode;
import main.java.core.constants.RobotConstants;
import main.java.core.messaging.Exchange;


import static main.java.core.constants.ProgramConstants.aiConfig;
import static main.java.core.util.ObjectHelper.distToPath;
import static main.java.core.util.ProtobufUtils.getPos;
import main.java.core.util.Vector2d;
import proto.triton.CoordinatedPassInfo;
import proto.triton.FilteredObject.Robot;


import java.util.ArrayList;
import java.util.List;


import main.java.core.constants.ProgramConstants;


import static proto.triton.FilteredObject.*;
import static proto.triton.CoordinatedPassInfo.CoordinatedPass;


 /**
 * Defines the sequence of tasks needed to successfully perform a coordinated pass between two allies.
 * It is assumed that the passer kicks the ball from the spot when he has it.
 */
public class CoordinatedPassNode extends SequenceNode {


    public final int passerID;
    private final KickBallNode kickBall;


    public CoordinatedPassNode(int passerID) {
        super("Coordinated Pass Node: " + passerID);
        this.passerID = passerID;
        this.kickBall = new KickBallNode(passerID);
    }


    @Override
    public void run(){
        System.out.println("CoordinatedPassNode.run()");
        this.execute(false);
    }


    /**
     * 1. Identifies the best pass receiver.
     * 2. Sends message to the central coordinator with pass details.
     * 3. Performs pass
     */
    @Override
    public NodeState execute() {
        return null;
    }


    public NodeState execute(boolean chip) {
        // TODO Send message to central coordinator with pass details


        //kicks the ball to the pass receiver
        CoordinatedPassInfo.CoordinatedPass message = getPassInfo();
        Vector2d direction = new Vector2d(message.getPassLocX(), message.getPassLocY());
        ProgramConstants.commandPublishingModule.publish(Exchange.CENTRAL_COORDINATOR_PASSING, message);
        this.kickBall.execute(direction, RobotConstants.MAX_KICK_VELOCITY, false);
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


        return passInfo.build();
    }


    private float distance(float x1, float y1, float x2, float y2){
        return (float) Math.pow((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2), 0.5);
    }
}



