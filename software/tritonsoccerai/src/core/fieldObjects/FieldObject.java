package core.fieldObjects;

import core.util.Vector2d;

/**
 * Root class for all on-field objects
 */
public abstract class FieldObject {

    private Vector2d pos;
    private Vector2d vel;
    private Vector2d acc;

    public FieldObject() {
        this.pos = new Vector2d(0, 0);
    }

    public FieldObject(int x, int y) {
        this.pos = new Vector2d(x, y);
    }

    public Vector2d getPos() {
        return pos;
    }

    public float getX() {
        return this.pos.x;
    }

    public float getY() {
        return this.pos.y;
    }

    public Vector2d getVel() {
        return this.vel;
    }

    public Vector2d getAcc() {
        return acc;
    }

    public void setPos(int x, int y) {
        this.pos.x = x;
        this.pos.y = y;
    }

    public void setVel(Vector2d vel) {
        this.vel = vel;
    }

    public void setAcc(Vector2d acc) {
        this.acc = acc;
    }

}
