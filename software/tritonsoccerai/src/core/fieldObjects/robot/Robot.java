package core.fieldObjects.robot;

import core.fieldObjects.FieldObject;

public class Robot extends FieldObject {

    private boolean unguarded;

    public Robot() {
        super();
        this.unguarded = true;
    }

    public Robot(int xPos, int yPos) {
        super(xPos, yPos);
        this.unguarded = true;
    }

    public boolean isUnguarded() {
        return this.unguarded;
    }

    public void setUnguarded(boolean isUnguarded) {
        this.unguarded = isUnguarded;
    }

}
