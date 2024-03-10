package main.java.core.constants;

import main.java.core.module.testModule.*;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ScheduledThreadPoolExecutor;


public enum AITest {
    MOVE(MoveTestModule.class, "Test the ability of robots to move a target location."),
    OBSTACLE(ObstacleTestModule.class, "Test robot obstacle avoidance."),
    DRIBBLE(DribbleTestModule.class, "Test ability of robot to dribble with the ball."),
<<<<<<< HEAD
    SPIN_WITH_BALL(SpinWBallTestModule.class, "Test ability of robot to spin in place with the ball."),
    SPIN_WITHOUT_BALL(SpinWOBallTestModule.class, "Test ability of robot to spin in place without the ball.")
=======
    ROTATE(RotateBotTestModule.class, "Test the ability for the robot to rotate to a particular orientation."),
    COORDINATEDPASS(CoordinatePassTestModule.class, "Test the ability to perform a coordinated pass.")
>>>>>>> 118736c (fixed merge conflicts and commit)
    ;

    private final Class<? extends TestModule> testClass;
    private final String desc;

    AITest(Class<? extends TestModule> testClass, String desc) {
        this.testClass = testClass;
        this.desc = desc;
    }

    public Class<? extends TestModule> getTestRunnerClass() {
        return testClass;
    }

    public String getDesc() {
        return desc;
    }

    public TestModule createNewTestModule(ScheduledThreadPoolExecutor executor) {
        try {
            return testClass.getConstructor(ScheduledThreadPoolExecutor.class).newInstance(executor);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        throw new IllegalStateException();
    }
    
}
