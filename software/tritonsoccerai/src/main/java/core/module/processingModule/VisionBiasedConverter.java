package main.java.core.module.processingModule;

import com.rabbitmq.client.Delivery;
import main.java.core.module.Module;
import main.java.core.util.ConvertCoordinate;
import main.java.core.util.Vector2d;
import proto.vision.MessagesRobocupSslDetection.SSL_DetectionFrame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeoutException;

import static main.java.core.messaging.Exchange.AI_BIASED_VISION_WRAPPER;
import static main.java.core.messaging.Exchange.AI_VISION_WRAPPER;
import static main.java.core.messaging.SimpleSerialize.simpleDeserialize;
import static proto.vision.MessagesRobocupSslDetection.SSL_DetectionBall;
import static proto.vision.MessagesRobocupSslDetection.SSL_DetectionRobot;
import static proto.vision.MessagesRobocupSslGeometry.*;
import static proto.vision.MessagesRobocupSslWrapper.SSL_WrapperPacket;


/**
 * Class for converting from audience biased vision to robot biased vision
 */
public class VisionBiasedConverter extends Module {
    
    
    public VisionBiasedConverter(ScheduledThreadPoolExecutor executor) {
        super(executor);
    }

    @Override
    protected void prepare() {

    }

    /**
     * Consumes from AI_VISION_WRAPPER and runs callbackWrapper
     */
    @Override
    protected void declareConsumes() throws IOException, TimeoutException {
        declareConsume(AI_VISION_WRAPPER, this::callbackWrapper);
    }

    /**
     * Takes a message containing an audience biased wrapper and publishes 
     * robot biased wrapper to the AI_BIASED_VISION_WRAPPER exchange.
     * @param s - param not used
     * @param delivery - Message containing audience biased wrapper
     */
    private void callbackWrapper(String s, Delivery delivery) {
        SSL_WrapperPacket wrapper = (SSL_WrapperPacket) simpleDeserialize(delivery.getBody());
        SSL_WrapperPacket biasedWrapper = wrapperAudienceToBiased(wrapper);
        publish(AI_BIASED_VISION_WRAPPER, biasedWrapper);
    }

    /**
     * Converts an audience biased wrapper to a robot biased wrapper
     * @param wrapper - audience biased wrapper
     * @return robot biased wrapper
     */
    private static SSL_WrapperPacket wrapperAudienceToBiased(SSL_WrapperPacket wrapper) {
        SSL_WrapperPacket.Builder biasedWrapper = wrapper.toBuilder();
        biasedWrapper.setDetection(detectionAudienceToBiased(wrapper.getDetection()));
        biasedWrapper.setGeometry(geometryAudienceToBiased(wrapper.getGeometry()));
        return biasedWrapper.build();
    }

    /**
     * Converts an audience biased detection frame to a robot biased detection frame
     * @param detection - audience biased detection frame
     * @return robot biased detection frame
     */
    private static SSL_DetectionFrame detectionAudienceToBiased(SSL_DetectionFrame detection) {
        SSL_DetectionFrame.Builder biasedDetection = detection.toBuilder();

        biasedDetection.clearBalls();
        biasedDetection.addAllBalls(ballsAudienceToBiased(detection.getBallsList()));

        biasedDetection.clearRobotsYellow();
        biasedDetection.addAllRobotsYellow(robotsAudienceToBiased(detection.getRobotsYellowList()));

        biasedDetection.clearRobotsBlue();
        biasedDetection.addAllRobotsBlue(robotsAudienceToBiased(detection.getRobotsBlueList()));

        return biasedDetection.build();
    }

    /**
     * Converts an audience biased geometry to a robot biased geometry
     * @param geometry - audience biased geometry
     * @return robot biased geometry
     */
    private static SSL_GeometryData geometryAudienceToBiased(SSL_GeometryData geometry) {
        SSL_GeometryData.Builder biasedGeometry = geometry.toBuilder();
        biasedGeometry.setField(fieldAudienceToBiased(geometry.getField()));
        return biasedGeometry.build();
    }

    /**
     * Converts audience biased balls to robot biased balls
     * @param line - audience biased 
     * @return robot biased 
     */
    private static Iterable<SSL_DetectionBall> ballsAudienceToBiased(List<SSL_DetectionBall> balls) {
        List<SSL_DetectionBall> biasedBalls = new ArrayList<>();
        balls.forEach(ball -> biasedBalls.add(ballAudienceToBiased(ball)));
        return biasedBalls;
    }

    /**
     * Converts an audience biased list of robots to a biased list of robots
     * @param line - audience biased field line
     * @return robot biased field line
     */
    private static Iterable<SSL_DetectionRobot> robotsAudienceToBiased(List<SSL_DetectionRobot> robots) {
        List<SSL_DetectionRobot> biasedRobots = new ArrayList<>();
        robots.forEach(robot -> biasedRobots.add(robotAudienceToBiased(robot)));
        return biasedRobots;
    }

    /**
     * Converts audience biased field to robot biased field
     * @param field - audience biased field
     * @return robot biased field
     */
    private static SSL_GeometryFieldSize fieldAudienceToBiased(SSL_GeometryFieldSize field) {
        SSL_GeometryFieldSize.Builder biasedField = field.toBuilder();

        biasedField.clearFieldLines();
        for (SSL_FieldLineSegment fieldLine : field.getFieldLinesList())
            biasedField.addFieldLines(lineAudienceToBiased(fieldLine));

        biasedField.clearFieldArcs();
        for (SSL_FieldCircularArc fieldArc : field.getFieldArcsList())
            biasedField.addFieldArcs(arcAudienceToBiased(fieldArc));

        return biasedField.build();
    }

    /**
     * Converts audience biased ball to robot biased ball
     * @param ball - audience biased ball
     * @return robot biased ball
     */
    private static SSL_DetectionBall ballAudienceToBiased(SSL_DetectionBall ball) {
        SSL_DetectionBall.Builder biasedBall = ball.toBuilder();

        Vector2d biasedBallPos = ConvertCoordinate.audienceToBiased(ball.getX(), ball.getY());
        biasedBall.setX(biasedBallPos.x);
        biasedBall.setY(biasedBallPos.y);

        Vector2d biasedBallPixel = ConvertCoordinate.audienceToBiased(ball.getPixelX(), ball.getPixelY());
        biasedBall.setPixelX(biasedBallPixel.x);
        biasedBall.setPixelY(biasedBallPixel.y);

        return biasedBall.build();
    }

    /**
     * Converts audience biased robot to robot biased robot
     * @param robot - audience biased robot
     * @return robot biased robot
     */
    private static SSL_DetectionRobot robotAudienceToBiased(SSL_DetectionRobot robot) {
        SSL_DetectionRobot.Builder biasedRobot = robot.toBuilder();

        Vector2d biasedRobotPos = ConvertCoordinate.audienceToBiased(robot.getX(), robot.getY());
        biasedRobot.setX(biasedRobotPos.x);
        biasedRobot.setY(biasedRobotPos.y);

        biasedRobot.setOrientation(ConvertCoordinate.audienceToBiased(robot.getOrientation()));

        Vector2d biasedRobotPixel = ConvertCoordinate.audienceToBiased(robot.getPixelX(), robot.getPixelY());
        biasedRobot.setPixelX(biasedRobotPixel.x);
        biasedRobot.setPixelY(biasedRobotPixel.y);

        return biasedRobot.build();
    }

    /**
     * Converts an audience biased line to a robot biased line
     * @param fieldLine - audience biased field line
     * @return  robot biased field line
     */
    private static SSL_FieldLineSegment lineAudienceToBiased(SSL_FieldLineSegment fieldLine) {
        SSL_FieldLineSegment.Builder biasedFieldLine = fieldLine.toBuilder();
        biasedFieldLine.setP1(vector2fAudienceToBiased(fieldLine.getP1()));
        biasedFieldLine.setP2(vector2fAudienceToBiased(fieldLine.getP2()));
        return biasedFieldLine.build();
    }

    /**
     * Converts an audience biased arc to a robot biased arc
     * @param fieldCircularArc - audience biased field arc
     * @return robot biased field arc
     */
    private static SSL_FieldCircularArc arcAudienceToBiased(SSL_FieldCircularArc fieldCircularArc) {
        SSL_FieldCircularArc.Builder biasedFieldCircularArc = fieldCircularArc.toBuilder();
        biasedFieldCircularArc.setCenter(vector2fAudienceToBiased(fieldCircularArc.getCenter()));
        biasedFieldCircularArc.setA1(0);
        biasedFieldCircularArc.setA2(360);
        return biasedFieldCircularArc.build();
    }

    /**
     * Converts an audience biased vector to a robot biased vector
     * @param vector - audience biased vector
     * @return robot biased vector
     */
    private static Vector2f vector2fAudienceToBiased(Vector2f vector) {
        Vector2f.Builder biasedVector2f = vector.toBuilder();
        Vector2d biasedVector = ConvertCoordinate.audienceToBiased(vector.getX(), vector.getY());
        biasedVector2f.setX(biasedVector.x);
        biasedVector2f.setY(biasedVector.y);
        return biasedVector2f.build();
    }
}
