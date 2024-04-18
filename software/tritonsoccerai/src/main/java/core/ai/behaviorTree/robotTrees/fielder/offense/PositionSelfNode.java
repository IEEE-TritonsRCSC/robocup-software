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


import static main.java.core.util.ProtobufUtils.getPos;

/**
 * Positions ally at optimal position
 */
public class PositionSelfNode extends TaskNode {

    private final MoveToPositionNode moveToPositionNode;
    

    public PositionSelfNode(int allyID) {
        super("Position Self Node: " + allyID, allyID);
        this.moveToPositionNode = new MoveToPositionNode(allyID);
    }

    /**
     * Decides where to position self, then moves to that location
     */
    @Override
    public NodeState execute() {
        int zoneWidth = 1000;
        Vector2d pos = null;
        while(pos == null){
            pos = findPositioningLocation(zoneWidth);
            zoneWidth = zoneWidth-100;
        }
        this.moveToPositionNode.execute(pos);
        return NodeState.SUCCESS;
    }

    private static ArrayList<int[]> possiblePos(int h, int w, int width) {
        ArrayList<int[]> out = new ArrayList<>();
        for (int i = 0; i < h / width; i++) {
            for (int j = 0; i < w / width; i++) {
                out.add(new int[] { i, j });
            }
        }
        return out;
    }
    private static double distance(int[] a, double[] b) {
        double dist = Math.sqrt(Math.pow(a[0] - b[0], 2) - Math.pow(a[1] - b[1], 2));
        return dist;
    }

    /**
     * Finds optimal location to position self
     */
    private Vector2d findPositioningLocation(int zoneWidth) {
        int h = 6000; // 6000
        int w = 9000; // 9000
        ArrayList<int[]> empty = possiblePos(h, w, zoneWidth);
        ArrayList<Robot> foesList = new ArrayList<>(GameInfo.getFoes());
        ArrayList<Robot> alliesList = new ArrayList<>(GameInfo.getFielders());
        List<Vector2d> obstaclePositions = new ArrayList<>();

        // remove self
        alliesList.remove(allyID);

        // add the other ally positions and foe positions to the obstaclesPositions list
        for(int i=0;i<alliesList.size();i++) {
			obstaclePositions.add(getPos(alliesList.get(i)));
            obstaclePositions.add(getPos(foesList.get(i)));
		}


        for (Vector2d obstacle : obstaclePositions) {
            int px = Math.round((float) obstacle.x / (float) zoneWidth);
            int py = Math.round((float) obstacle.y / (float) zoneWidth);
            int[][] transforms = { { -1, -1 }, { -1, 0 }, { 0, -1 }, { 0, 0 } };
            for (int[] transform : transforms) {
                if (px + transform[0] > 0 && py + transform[1] > 0 && px + transform[0] < h / zoneWidth
                        && py + transform[1] < w / zoneWidth) {
                    empty.remove(new int[] { px + transform[0], py + transform[1] });
                }
            }
        }
        // double dist = Double.MAX_VALUE;
        if(empty.size() == 0 ){
            return null;
        }
        int[] out = empty.get(0);

        
        return new Vector2d((float) out[0], (float) out[1]);
        
    }

}
