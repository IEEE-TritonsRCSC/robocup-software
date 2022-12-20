package core.fieldObjects.robot;

/**
 * Used to track attributes of each ally robot
 */
public class Ally extends Robot {

    private boolean hasPossession;

    public Ally() {
        super();
        this.hasPossession = false;
    }

    public Ally(int xPos, int yPos) {
        super(xPos, yPos);
        this.hasPossession = false;
    }

    public boolean getHasPossession() {
        return this.hasPossession;
    }

    public void setHasPossession(boolean hasPossession) {
        this.hasPossession = hasPossession;
    }

}
