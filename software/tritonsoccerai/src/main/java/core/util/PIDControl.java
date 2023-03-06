package main.java.core.util;

public class PIDControl {
    private final float kp;
    private final float ki;
    private final float kd;
    private long lastTimestamp;
    private float errorSum;
    private float lastError;

    public PIDControl(float kp, float ki, float kd) {
        this.kp = kp;
        this.ki = ki;
        this.kd = kd;
    }

    public float compute(float setPoint, float input, long timestamp) {
        long timeDiff = timestamp - lastTimestamp;

        float error = setPoint - input;
        errorSum = errorSum + error * timeDiff;
        float errorDiff = (error - lastError) / timeDiff;

        lastTimestamp = timestamp;
        lastError = error;

        return error * kp + errorSum * ki + errorDiff * kd;
    }
}
