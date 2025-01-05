package core.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Defines 2D vectors and their main functions
 * Utilized to track position, velocity, and acceleration of field objects
 */
public class Vector2d {
    public float x;
    public float y;

    public Vector2d(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2d(Vector2d vector) {
        this.x = vector.x;
        this.y = vector.y;
    }

    public Vector2d(float angle) {
        this.x = (float) Math.cos(angle);
        this.y = (float) Math.sin(angle);
    }

    public static float angleDifference(float angle1, float angle2) {
        double diff = (angle2 - angle1 + Math.PI) % (2 * Math.PI) - Math.PI;
        return (float) (diff < -Math.PI ? diff + 2 * Math.PI : diff);
    }

    public static float distToPath(Vector2d from, Vector2d to, List<Vector2d> points) {
        float minDist = Float.MAX_VALUE;
        Vector2d dir = to.sub(from).norm();
        for (Vector2d point : points) {
            Vector2d fromToPoint = point.sub(from);
            if (dir.dot(fromToPoint.norm()) < 0) continue;
            minDist = Math.min(dir.reject(fromToPoint).mag(), minDist);
        }
        return minDist;
    }

    public Vector2d norm() {
        float mag = mag();
        return this.scale(1f / mag);
    }

    public Vector2d sub(Vector2d vector) {
        return new Vector2d(x - vector.x, y - vector.y);
    }

    public float dot(Vector2d vector) {
        return x * vector.x + y * vector.y;
    }

    public float mag() {
        return (float) Math.sqrt(x * x + y * y);
    }

    public Vector2d reject(Vector2d vector) {
        return vector.sub(project(vector));
    }

    public Vector2d scale(float scale) {
        return new Vector2d(x * scale, y * scale);
    }

    public Vector2d project(Vector2d vector) {
        return vector.scale(dot(vector) / vector.dot(vector));
    }

    public static boolean checkDistToPath(Vector2d from, Vector2d to, List<Vector2d> points, float dist) {
        Vector2d dir = to.sub(from).norm();
        for (Vector2d point : points) {
            Vector2d fromToPoint = point.sub(from);
            if (dir.dot(fromToPoint.norm()) < 0) continue;
            if (dir.reject(fromToPoint).mag() < dist)
                return true;
        }
        return false;
    }

    public float angle() {
        return (float) Math.atan2(y, x);
    }

    public Vector2d add(Vector2d vector) {
        return new Vector2d(x + vector.x, y + vector.y);
    }

    public float scalarProj(Vector2d vector) {
        return dot(vector) / vector.mag();
    }

    public float angle(Vector2d vector) {
        return (float) Math.acos(dot(vector) / (mag() * vector.mag()));
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector2d vector2d = (Vector2d) o;
        return Float.compare(vector2d.x, x) == 0 && Float.compare(vector2d.y, y) == 0;
    }

    @Override
    public String toString() {
        return "Vector2d{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    public boolean isInRect(float x, float y, float width, float height) {
        return (this.x >= x)
                && (this.x <= (x + width))
                && (this.y >= y)
                && (this.y <= (y + height));
    }

    public List<Vector2d> getCenteredNeighbors(float dist, float spacing) {
        List<Vector2d> neighbors = new ArrayList<>();
        for (float offsetX = x; offsetX < dist / 2f; offsetX += spacing) {
            for (float offsetY = y; offsetY < dist / 2f; offsetY += spacing) {
                for (int xMul = -1; xMul < 2; xMul += 2) {
                    for (int yMul = -1; yMul < 2; yMul += 2) {
                        Vector2d neighbor = new Vector2d(xMul * offsetX, yMul * offsetY);
                        if (!neighbors.contains(neighbor))
                            neighbors.add(neighbor);
                    }
                }
            }
        }
        return neighbors;
    }

    public List<Vector2d> getGridNeighbors(float dist, float spacing) {
        List<Vector2d> neighbors = new ArrayList<>();
        for (float neighborX = x - dist / 2f; neighborX < x + dist / 2f; neighborX += spacing) {
            for (float neighborY = y - dist / 2f; neighborY < y + dist / 2f; neighborY += spacing) {
                Vector2d pos = new Vector2d(neighborX, neighborY);
                neighbors.add(pos.getNearestOnGrid(spacing));
            }
        }
        return neighbors;
    }

    public Vector2d getNearestOnGrid(float spacing) {
        float nearestX = Math.round(x / spacing) * spacing;
        float nearestY = Math.round(y / spacing) * spacing;
        return new Vector2d(nearestX, nearestY);
    }

    public float distToLine(Vector2d pointA, Vector2d pointB) {
        Vector2d dir = pointB.sub(pointA).norm();
        Vector2d dirToPoint = this.sub(pointA).norm();
        return dir.reject(dirToPoint).mag();
    }

    public float getMinDist(List<Vector2d> vectors) {
        float minDist = Float.MAX_VALUE;
        for (Vector2d vector : vectors)
            minDist = Math.min(minDist, dist(vector));
        return minDist;
    }

    public float dist(Vector2d vector) {
        return sub(vector).mag();
    }

    /**
     * Finds the intersection point of two lines
     * @param pointA A point on the first line
     * @param directionA Any vector describing the direction of the first line
     * @param pointB A point on the second line
     * @param directionB Any vector describing the direction of the second line
     * @return The intersection point of the two lines
     */
    public static Vector2d intersectLines(Vector2d pointA, Vector2d directionA, Vector2d pointB, Vector2d directionB) {
        // long startTime = System.nanoTime();

        // Extract components of the direction vectors
        float a1 = directionA.x;
        float b1 = -directionB.x;
        float c1 = pointB.x - pointA.x;

        float a2 = directionA.y;
        float b2 = -directionB.y;
        float c2 = pointB.y - pointA.y;

        // Calculate the determinant (cross product in 2D)
        float determinant = a1 * b2 - a2 * b1;

        // If the determinant is 0, lines are parallel or coincident
        if (Math.abs(determinant) < 1e-7) {
            return null; // Lines don't intersect uniquely
        }

        // Calculate the scalar parameters t1 and t2 (we only need t1 here)
        float t1 = (c1 * b2 - c2 * b1) / determinant;

        // Calculate the intersection point
        float intersectionX = pointA.x + t1 * directionA.x;
        float intersectionY = pointA.y + t1 * directionA.y;

        // Code to measure
        // long endTime = System.nanoTime();
        // System.out.println("Time taken intersect: " + (endTime - startTime) + " ns");

        return new Vector2d(intersectionX, intersectionY);
    }
}
