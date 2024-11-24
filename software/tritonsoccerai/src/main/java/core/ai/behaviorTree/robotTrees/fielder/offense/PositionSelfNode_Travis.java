package main.java.core.ai.behaviorTree.robotTrees.fielder.offense;

import main.java.core.ai.GameInfo;
import main.java.core.ai.behaviorTree.nodes.NodeState;
import main.java.core.ai.behaviorTree.nodes.taskNodes.TaskNode;
import main.java.core.ai.behaviorTree.robotTrees.basicFunctions.MoveToPositionNode;

import static proto.triton.FilteredObject.Robot;
import main.java.core.util.Vector2d;
import proto.triton.FilteredObject.Robot;

import java.util.ArrayList;
import java.util.List;

import java.util.Random;


import static main.java.core.util.ProtobufUtils.getPos;

/**
 * Positions ally at optimal position
 */
public class PositionSelfNode extends TaskNode {

    private final MoveToPositionNode moveToPositionNode;
    private final Robot allyWithBall;
    private final int ID;
    private final  ArrayList<Robot> allies;
    private final  ArrayList<Robot> foes;
    private final Vector2d ballPos;
    

    public PositionSelfNode(int allyID) {
        super("Position Self Node: " + allyID, allyID);
        
        this.allyWithBall = GameInfo.getAllyClosestToBall();

        this.allies = GameInfo.getAllies();
        this.foes = GameInfo.getFoes();
        this.ballPos = GameInfo.getPos(GameInfo.getBall());

        this.moveToPositionNode = new MoveToPositionNode(allyID);
        this.ID = allyID;
    }

    /**
     * Decides where to position self, then moves to that location
     */
    @Override
    public NodeState execute() {
        ArrayList<Vector2d> configuration = compute_offense_plan(50, 100);
        Vector2d pos = configuration[self.ID];
        this.moveToPositionNode.execute(pos);
        return NodeState.SUCCESS;
    }


    /**
     * Computes the optimal offense positioning for all fielders based on the current game status at
     * this timestamp
     * @param radius The radius of the circle within which the robot can move in a time unit
     * @param directShootingBonus Bonus point for shooting at the gate directly
     * @return The destinations of each ally as an ArrayList
     */
    private ArrayList<Vector2d> compute_offense_plan(int radius, int directShootingBonus) {
        Robot finalShooter = this.allyWithBall;
        double bestScore = 0;
        int n = (int)(3.14 * radius * radius / 100); // Sample points for fielders
        ArrayList<Vector2d> output = new ArrayList<Vector2d>();
        ArrayList<Vector2d> sample_points_start = new ArrayList<Vector2d>(); 
            // Stores the sample points around this.allyWithBall
        ArrayList<Vector2d> sample_points_intermediate = new ArrayList<Vector2d>();
            // Stores the sample points around the other fielders, in groups of n

        // Sampling
        if(!GameInfo.getPossessBall(this.allyWithBall.getID())) {
            // If self.allyWithBall doesn't possess, go to the ball
            sample_points_start.add(GameInfo.getPos(this.ball));
        }
        else {
            // If self.allyWithBall possesses the ball, sample points around this.allyWithBall
            sample_points_start = sample_points(radius, this.ballPos, n);
        }
        for(Robot fielder: this.allies) {
            sample_points_intermediate.addAll(sample_points(radius, GameInfo.getPos(fielder), n));
        }
        
        // Determining optimal passing plan
        Vector2d startPoint;
        Vector2d shootPoint;
        int maxReward = 0;
        ArrayList<Robot> alliesWithoutBall = new ArrayList<Robot>(this.allies);
        alliesWithoutBall.remove(this.allyWithBall);
        for(Vector2d p1: sample_points_start) {
            // Evaluate the plan of moving to p1 and then shooting at the gate, using the reward function
            double pathLength = this.ballPos.sub(GameInfo.getGate()).mag();
            double foeDist = Vector2d.distToPath(p1, GameInfo.getGate(), this.foes());
            int reward = reward(foeDist, pathLength);
            if(reward > maxReward) {
                maxReward = reward;
                startPoint = p1;
                shootPoint = p1;
                finalShooter = this.allyWithBall;
            }

            for(int i = 0; i < sample_points_intermediate.size(); i++) {
                Vector2d p2 = sample_points_intermediate.get(i);
                // Evaluate the plan of moving to p1, pass to p2, then shoot to gate, using the reward function
                double pathLength = p2.sub(GameInfo.getGate()).mag() + p1.sub(p2).mag();
                double foeDist = Vector2d.distToPath(p2, GameInfo.getGate(), this.foes()) + 
                    Vector2d.distToPath(p1, p2, this.foes());
                int reward = reward(foeDist, pathLength);
                if(reward > maxReward) {
                    maxReward = reward;
                    startPoint = p1;
                    shootPoint = p2;
                    finalShooter = alliesWithoutBall.get(i % n);
                }
            }
        }

        // For every fielder not passing ball, assign to one foe; For the ones passing ball, assign according to the plan
        for(Robot fielder: this.allies) {
            if(fielder != this.allyWithBall && fielder != finalShooter) {
                Vector2d foe = GameInfo.getPos(identifyFoeToGuard(this, this.foes));
                output.add(GameInfo.getPos(foe));
            }
            else {
                if(fielder == this.allyWithBall) {
                    output.add(startPoint);
                }
                else if(fielder == finalShooter) {
                    output.add(shootPoint);
                }
            }
        }
        return output;
    }

    /**
     * Sample a given number of points in a circle uniformly at random
     * @param radius The radius of the circle 
     * @param center The center of the circle
     * @param n The number of points to sample
     * @return The coordinates of each sample point as an ArrayList
     */
    private ArrayList<Vector2d> sample_points(int radius, Vector2d center, int n) {
        ArrayList<Vector2d> points = new ArrayList<Vector2d>();
        for(int i = 0; i < n; i++) {
            double angle = random.uniform(0, 2 * 3.14);
            double d = random.uniform(0, radius);
            int x = (int)(center.x + d * Math.cos(angle));
            int y = (int)(center.y + d * Math.sin(angle));
            if(x < 0 || x >= 9000 || y < 0 || y >= 6000) {
                i--;
                continue;
            }
            points.add(new Vector2d(x, y));
        }
        return points;
    }

    private int reward(double minDistToPath, double totalPathDist) {
        if(minDistToPath == 0 || totalPathDist == 0) {
            return -1;
        }
        return (int)(5000 / minDistToPath + 10000 / totalPathDist);
    }

}