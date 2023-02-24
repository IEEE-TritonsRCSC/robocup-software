package java.core.fieldObjects.robot;

import java.core.fieldObjects.FieldObject;

/**
 * Superclass for all types of robots
 */
public class Robot extends FieldObject {

    private boolean unguarded;
    private int id;

    public Robot(int xPos, int yPos, int id) {
        super(xPos, yPos);
        this.unguarded = true;
        this.id = id;
    }

    public boolean isUnguarded() {
        return this.unguarded;
    }

    public int getId() {
        return id;
    }

    public void setUnguarded(boolean isUnguarded) {
        this.unguarded = isUnguarded;
    }

    public void setId(int newID) {
        this.id = newID;
    }

}
