package core.fieldObjects.ball;

import core.fieldObjects.FieldObject;

public class Ball extends FieldObject {

    private boolean inAir;

    public Ball() {
        super();
        this.inAir = false;
    }

    public Ball(int xPos, int yPos) {
        super(xPos, yPos);
        this.inAir = false;
    }

    public boolean isInAir() {
        return this.inAir;
    }

}
