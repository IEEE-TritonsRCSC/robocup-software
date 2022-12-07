package core.fieldObjects;

import math.linearAlgebra.Vec2D;

// root class for all on-field objects
public abstract class FieldObject {

    private Vec2D pos;

    public FieldObject() {
        this.pos = new Vec2D(0, 0);
    }

    public FieldObject(int x, int y) {
        this.pos = new Vec2D(x, y);
    }

    public Vec2D getPos() {
        return pos;
    }

    public void setPos(int x, int y) {
        this.pos.x = x;
        this.pos.y = y;
    }

}
