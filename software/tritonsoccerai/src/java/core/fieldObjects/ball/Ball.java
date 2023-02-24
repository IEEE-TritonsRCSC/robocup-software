package java.core.fieldObjects.ball;

import java.core.fieldObjects.FieldObject;
import java.core.fieldObjects.robot.Robot;

/**
 * Utilized to track attributes of the game ball
 */
public class Ball extends FieldObject {

    private boolean inAir;
    private Robot possessedBy;

    public Ball() {
        super();
        this.inAir = false;
        this.possessedBy = null;
    }

    public Ball(int xPos, int yPos) {
        super(xPos, yPos);
        this.inAir = false;
        this.possessedBy = null;
    }

    public boolean isInAir() {
        return this.inAir;
    }

    public void setInAir(boolean inAir) {
        this.inAir = inAir;
    }

    public Robot getPossessedBy() {
        return this.possessedBy;
    }

    public void setPossessedBy(Robot ballholder) {
        this.possessedBy = ballholder;
    }

}
