package core.ai.behaviorTree.robotTrees.fielder;

import core.ai.GameInfo;
import core.ai.behaviorTree.nodes.NodeState;
import core.ai.behaviorTree.nodes.compositeNodes.CompositeNode;
import core.ai.behaviorTree.nodes.conditionalNodes.ConditionalNode;
import core.ai.behaviorTree.robotTrees.fielder.defense.DefenseRootNode;
import core.ai.behaviorTree.robotTrees.fielder.offense.OffenseRootNode;

public class FielderRootNode extends CompositeNode {

    private final ConditionalNode haveBall;
    private final OffenseRootNode offense;
    private final DefenseRootNode defense;
    private boolean onOffense;

    public FielderRootNode() {
        super("Fielder Root");
        this.haveBall = new ConditionalNode() {
            @Override
            public boolean conditionSatisfied() {
                return GameInfo.getPossessBall();
            }
        };
        this.offense = new OffenseRootNode();
        this.defense = new DefenseRootNode();
        this.onOffense = false;
    }

    @Override
    public NodeState execute() {
        if (NodeState.isSuccess(this.haveBall.execute()) != this.onOffense) {
            if (onOffense) {
                // TODO
                // kill offense node execution
                this.defense.execute();
            }
            else {
                // TODO
                // kill defense node execution
                this.offense.execute();
            }
        }
        return NodeState.RUNNING;
    }

}
