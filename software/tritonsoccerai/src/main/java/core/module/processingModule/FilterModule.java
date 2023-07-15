package main.java.core.module.processingModule;

import com.rabbitmq.client.Delivery; // Class that encapsulates a message
import main.java.core.constants.ProgramConstants;
import main.java.core.constants.Team;
import main.java.core.ai.GameInfo;
import main.java.core.module.Module;
import main.java.core.util.Vector2d;
import proto.vision.MessagesRobocupSslWrapper.SSL_WrapperPacket;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

// enum for all Exchange relevant Objects
import static main.java.core.messaging.Exchange.*;
 // method for deserializing the message (in bytes)
import static main.java.core.messaging.SimpleSerialize.simpleDeserialize;
// getter method nearest Robot given a target vector and position of all Robots
import static main.java.core.util.ObjectHelper.getNearestRobot;
// getter method for the current position of a robot
import static main.java.core.util.ProtobufUtils.getPos;
// proto for feedback from a robot
import static proto.simulation.SslSimulationRobotFeedback.RobotFeedback; 
// proto for Ball, AllyCapture, FoeCapture, Robot, and FilteredWrapper Packet
import static proto.triton.FilteredObject.*; 
import static proto.triton.FilteredObject.Ball.CaptureStateCase;
import static proto.triton.FilteredObject.Ball.CaptureStateCase.*;
import static proto.triton.FilteredObject.Ball.newBuilder;
// proto for ball
import static proto.vision.MessagesRobocupSslDetection.SSL_DetectionBall; 
// proto for robot
import static proto.vision.MessagesRobocupSslDetection.SSL_DetectionRobot; 
// proto for all field dimensions 
import static proto.vision.MessagesRobocupSslGeometry.SSL_GeometryFieldSize; 

public class FilterModule extends Module {

    // constant period for the scheduled executor (10ms)
    private static final long DEFAULT_PUBLISH_PERIOD = 10;

    private FilteredWrapperPacket filteredWrapper;
    private Map<Integer, RobotFeedback> feedbacks;

    // Future object which will contain a ScheduledFuture    
    private Future publishFilteredWrapperFuture;

    /**
     * Constructor for FilterModule
     * 
     * @param executor Schedules commands periodically
     */
    public FilterModule(ScheduledThreadPoolExecutor executor) {
        super(executor);
    }

    /**
     * Execute method for FilterModule, which initializes the default
     * filtered wrapper, and assigns the ScheduledFuture to publishFilteredWrapperFuture
     */
    @Override
    public void run() {
        super.run();
        initDefaultFilteredWrapper();
        publishFilteredWrapperFuture = executor.scheduleAtFixedRate(this::publishFilteredWrapper, 0,    
                DEFAULT_PUBLISH_PERIOD, TimeUnit.MILLISECONDS);
    }

    /** 
     * Initializer method for the FilteredWrapperPacket filterWrapper
     * using FilteredWrapperPacket Builder
    */
    private void initDefaultFilteredWrapper() {
        long timestamp = System.currentTimeMillis();
        FilteredWrapperPacket.Builder filteredWrapper = FilteredWrapperPacket.newBuilder();
        filteredWrapper.setField(initDefaultField());
        filteredWrapper.setBall(initDefaultBall(timestamp));
        filteredWrapper.putAllAllies(initDefaultAllies(timestamp));
        filteredWrapper.putAllFoes(initDefaultFoes(timestamp));
        this.filteredWrapper = filteredWrapper.build();
    }

    /**
     * Publisher method for the FilteredWrapper (using the super class Module)
     * with AI_FILTERED_VISION_WRAPPER being the Exchange being published to
     * and filteredWrapper as the Object being published
     */
    private void publishFilteredWrapper() {
        super.publish(AI_FILTERED_VISION_WRAPPER, filteredWrapper);
    }

    /**
     * Initializer method for a Default Field
     * 
     * @return defaultField, the default field initialized
     */
    private SSL_GeometryFieldSize initDefaultField() {
        SSL_GeometryFieldSize.Builder defaultField = SSL_GeometryFieldSize.newBuilder();
        defaultField.setFieldLength(0);
        defaultField.setFieldWidth(0);
        defaultField.setGoalWidth(0);
        defaultField.setGoalDepth(0);
        defaultField.setBoundaryWidth(0);
        return defaultField.build();
    }

    /**
     * Initializer method for Ball 
     * 
     * @param timestamp, timestamp for the initialized Ball
     * @return the default ball intialized 
     */
    private Ball initDefaultBall(long timestamp) {
        Ball.Builder defaultBall = newBuilder();
        defaultBall.setTimestamp(timestamp);
        return defaultBall.build();
    }

    /**
     * Initializer method for Default Ally robots 
     * 
     * @param timestamp
     * @return Filtered Map of Ally Integer, Robot pairs
     */
    private Map<Integer, Robot> initDefaultAllies(long timestamp) {
        Map<Integer, Robot> defaultFilteredAllies = new HashMap<>();
        for (int id = 0; id < ProgramConstants.gameConfig.numBots; id++) {
            Robot.Builder defaultFilteredAlly = Robot.newBuilder();
            defaultFilteredAlly.setTimestamp(timestamp);
            defaultFilteredAllies.put(id, defaultFilteredAlly.build());
        }
        return defaultFilteredAllies;
    }

    /**
     * Initializer method for Default Foe robots 
     * 
     * @param timestamp
     * @return filtered Map of Foe Integer Robot pairs
     */
    private Map<Integer, Robot> initDefaultFoes(long timestamp) {
        Map<Integer, Robot> defaultFilteredFoes = new HashMap<>();
        for (int id = 0; id < ProgramConstants.gameConfig.numBots; id++) {
            Robot.Builder defaultFilteredFoe = Robot.newBuilder();
            defaultFilteredFoe.setTimestamp(timestamp);
            defaultFilteredFoe.setId(id);
            defaultFilteredFoes.put(id, defaultFilteredFoe.build());
        }
        return defaultFilteredFoes;
    }

    /**
     * Protectedly allows initialization of the default filtered wrapper
     */
    @Override
    protected void prepare() {
        initDefaultFilteredWrapper();
    }

    /**
     * Declares relevant exchanges to consume from (when the messageConsumer 
     * is called) and the method to be called once a message is received
     */
    @Override
    protected void declareConsumes() throws IOException, TimeoutException {
        declareConsume(AI_BIASED_VISION_WRAPPER, this::callbackWrapper);
        declareConsume(AI_ROBOT_FEEDBACKS, this::callbackFeedbacks);
    }

    /**
     * Method called when the messageConsumer has been called for the relevant 
     * Exchange
     * TODO
     * 
     * @param s ???
     * @param delivery
     */
    private void callbackWrapper(String s, Delivery delivery) {
        SSL_WrapperPacket wrapper = (SSL_WrapperPacket) simpleDeserialize(delivery.getBody());

        long timestamp = System.currentTimeMillis();

        FilteredWrapperPacket.Builder filteredWrapper = this.filteredWrapper.toBuilder();
        filteredWrapper.setField(wrapper.getGeometry().getField());
        filteredWrapper.setBall(filterBalls(wrapper.getDetection().getBallsList(), this.filteredWrapper.getBall(),
                feedbacks, this.filteredWrapper.getFoesMap(), timestamp));

        List<SSL_DetectionRobot> allies;
        List<SSL_DetectionRobot> foes;
        if (GameInfo.getTeamColor() == Team.YELLOW) {
            allies = wrapper.getDetection().getRobotsYellowList();
            foes = wrapper.getDetection().getRobotsBlueList();
        } else {
            allies = wrapper.getDetection().getRobotsBlueList();
            foes = wrapper.getDetection().getRobotsYellowList();
        }

        filteredWrapper.putAllAllies(filterAllies(allies, this.filteredWrapper.getAlliesMap(), feedbacks, timestamp));
        filteredWrapper.putAllFoes(filterFoes(foes, this.filteredWrapper.getFoesMap(), feedbacks, timestamp));
        this.filteredWrapper = filteredWrapper.build();
        GameInfo.setWrapper(this.filteredWrapper);
    }

    /**
     * Assigns the Map feedbacks using the RabbitMQ Delivery delivery 
     * 
     * @param s ???
     * @param delivery
     */
    private void callbackFeedbacks(String s, Delivery delivery) {
        feedbacks = (Map<Integer, RobotFeedback>) simpleDeserialize(delivery.getBody());
    }

    /**
     * Filters the Balls: unitizes the Balls's coordinates, 
     * defines and assigns lastKnownBall and filteredBall
     * 
     * @param balls
     * @param lastBall
     * @param feedbacks
     * @param lastFoes
     * @param timestamp
     * @return
     */
    private Ball filterBalls(List<SSL_DetectionBall> balls, Ball lastBall, Map<Integer, RobotFeedback> feedbacks,
                             Map<Integer, Robot> lastFoes, long timestamp) {

        // Initializes variables to store who has possession of the Ball
        CaptureStateCase captureStateCase = FREE;
        int captureId = 0;

        if (feedbacks != null) {
            // Loop through the Map of Feedbacks, determining who has the Ball 
            for (Map.Entry<Integer, RobotFeedback> entry : feedbacks.entrySet()) {
                Integer id = entry.getKey();
                RobotFeedback feedback = entry.getValue();
                if (feedback.getDribblerBallContact()) {
                    captureStateCase = ALLY_CAPTURE;
                    captureId = id;
                }
            }
        }

        if (balls.size() == 0) {
            Ball.Builder lastKnownBall = lastBall.toBuilder();
            lastKnownBall.setTimestamp(timestamp);
            lastKnownBall.setConfidence(0f);

            // How to act when the Ball is not in ally possession
            if (captureStateCase != ALLY_CAPTURE) {
                captureStateCase = FOE_CAPTURE;
                Robot nearestFoe = getNearestRobot(getPos(lastBall), lastFoes.values().stream().toList());
                captureId = nearestFoe.getId();
            }

            // Set the lastKnownBall to the corresponding capture state
            switch (captureStateCase) {
                case FREE -> lastKnownBall.setFree(Free.newBuilder().build());
                case ALLY_CAPTURE -> lastKnownBall.setAllyCapture(AllyCapture.newBuilder().setId(captureId).build());
                case FOE_CAPTURE -> lastKnownBall.setFoeCapture(FoeCapture.newBuilder().setId(captureId).build());
            }
            return lastKnownBall.build();
        }

        // Filtering the ball's physical coordinates to 2-D map units
        float x = 0;
        float y = 0;
        float z = 0;
        for (SSL_DetectionBall ball : balls) {
            x += ball.getX();
            y += ball.getY();
            z += ball.getZ();
        }
        x /= balls.size();
        y /= balls.size();
        z /= balls.size();

        /*
         * Setting the velocity and acceleration of the Ball using timestamps
         * and previous locations
        */
        float deltaSeconds = (timestamp - lastBall.getTimestamp()) / 1000f;
        float vx = (x - lastBall.getX()) / deltaSeconds;
        float vy = (y - lastBall.getY()) / deltaSeconds;
        float vz = (z - lastBall.getZ()) / deltaSeconds;
        float accX = (vx - lastBall.getVx()) / deltaSeconds;
        float accY = (vy - lastBall.getVy()) / deltaSeconds;
        float accZ = (vz - lastBall.getAccZ()) / deltaSeconds;

        // Defining and assigning values to the Filtered Ball object
        Ball.Builder filteredBall = lastBall.toBuilder();
        filteredBall.setTimestamp(timestamp);
        filteredBall.setConfidence(1f);
        filteredBall.setX(x);
        filteredBall.setY(y);
        filteredBall.setZ(z);
        filteredBall.setVx(vx);
        filteredBall.setVy(vy);
        filteredBall.setVz(vz);
        filteredBall.setAccX(accX);
        filteredBall.setAccY(accY);
        filteredBall.setAccZ(accZ);

        // Set the filteredBall to the corresponding capture state
        switch (captureStateCase) {
            case FREE -> filteredBall.setFree(Free.newBuilder().build());
            case ALLY_CAPTURE -> filteredBall.setAllyCapture(AllyCapture.newBuilder().setId(captureId).build());
            case FOE_CAPTURE -> filteredBall.setFoeCapture(FoeCapture.newBuilder().setId(captureId).build());
        }

        return filteredBall.build();
    }

    /**
     * Filters the Ally Robots: using lastAlly, and hasBall 
     * 
     * @param allies
     * @param lastAllies
     * @param feedbacks
     * @param timestamp
     * @return
     */
    private Map<Integer, Robot> filterAllies(List<SSL_DetectionRobot> allies,
                                             Map<Integer, Robot> lastAllies,
                                             Map<Integer, RobotFeedback> feedbacks,
                                             long timestamp) {

        // Defines the Map of filteredAllies of Integer, Robot pairs
        Map<Integer, Robot> filteredAllies = new HashMap<>();

        // Loops through Ally robots
        for (SSL_DetectionRobot ally : allies) {
            // For each ally robot, calculates the lastAlly, hasBall to define the filteredAlly
            if (ally.getRobotId() < ProgramConstants.gameConfig.numBots) {
                Robot lastAlly = lastAllies.get(ally.getRobotId());
                boolean hasBall = false;
                // System.out.println("feedbacks is null");
                if ((feedbacks != null) && (feedbacks.size() == ProgramConstants.gameConfig.numBots)){
                    hasBall = feedbacks.get(ally.getRobotId()).getDribblerBallContact();
                }
                Robot filteredAlly = filterRobot(timestamp, ally, lastAlly, hasBall);
                filteredAllies.put(ally.getRobotId(), filteredAlly);
            }
        }

        return filteredAllies;
    }

    /**
     * Filter the Foe Robots: using lastFoe and hasBall
     * 
     * @param foes
     * @param lastFoes
     * @param feedbacks
     * @param timestamp
     * @return
     */
    private Map<Integer, Robot> filterFoes(List<SSL_DetectionRobot> foes,
                                           Map<Integer, Robot> lastFoes,
                                           Map<Integer, RobotFeedback> feedbacks,
                                           long timestamp) {

        // variable for determining and storing whether allies have possession of the Ball
        boolean allyHasBall = false;
        if (feedbacks != null) {
            for (RobotFeedback feedback : feedbacks.values()) {
                if (feedback.getDribblerBallContact()) {
                    allyHasBall = true;
                    break;
                }
            }
        }

        Map<Integer, Robot> filteredFoes = new HashMap<>();
        // Loop through Foe Robots
        for (SSL_DetectionRobot foe : foes) {
            // For each foe robot, calculate the lastFoe, and hasBall for filteredFoes
            if (foe.getRobotId() < ProgramConstants.gameConfig.numBots) {
                Robot lastFoe = lastFoes.get(foe.getRobotId());
                boolean hasBall = false;

                if (!allyHasBall) {
                    // TODO
                }

                Robot filteredFoe = filterRobot(timestamp, foe, lastFoe, hasBall);
                if (allyHasBall)
                    filteredFoe = filteredFoe.toBuilder().setHasBall(false).build();
                filteredFoes.put(foe.getRobotId(), filteredFoe);
            }
        }
        return filteredFoes;
    }

    /**
     * Filters a single Robot: calculating and assigning its velocity, 
     * acceleration, change in Angle, and Angular Acceleration 
     * 
     * @param timestamp
     * @param robot
     * @param lastRobot
     * @param hasBall
     * @return
     */
    private Robot filterRobot(long timestamp, SSL_DetectionRobot robot, Robot lastRobot, boolean hasBall) {
        // Calculate Velocity, Acceleration, change in Angle, and Angular Acceleration
        float deltaSeconds = (timestamp - lastRobot.getTimestamp()) / 1000f;
        float vx = (robot.getX() - lastRobot.getX()) / deltaSeconds;
        float vy = (robot.getY() - lastRobot.getY()) / deltaSeconds;
        float angular = Vector2d.angleDifference(robot.getOrientation(), lastRobot.getOrientation()) / deltaSeconds;
        float accX = (vx - lastRobot.getVx()) / deltaSeconds;
        float accY = (vy - lastRobot.getVy()) / deltaSeconds;
        float accAngular = (angular - lastRobot.getAngular()) / deltaSeconds;

        // Filtering a robot using previously calculated variables
        Robot.Builder filteredRobot = lastRobot.toBuilder();
        filteredRobot.setTimestamp(timestamp);
        filteredRobot.setId(robot.getRobotId());
        filteredRobot.setX(robot.getX());
        filteredRobot.setY(robot.getY());
        filteredRobot.setOrientation(robot.getOrientation());
        filteredRobot.setVx(vx);
        filteredRobot.setVy(vy);
        filteredRobot.setAngular(angular);
        filteredRobot.setAccX(accX);
        filteredRobot.setAccY(accY);
        filteredRobot.setAccAngular(accAngular);
        filteredRobot.setHasBall(hasBall);

        if (hasBall && !lastRobot.getHasBall()) {
            filteredRobot.setDribbleStartX(lastRobot.getX());
            filteredRobot.setDribbleStartY(lastRobot.getY());
        }
        return filteredRobot.build();
    }

    
    @Override
    public void interrupt() {
        super.interrupt();
        publishFilteredWrapperFuture.cancel(false);
    }
}
