package com.triton.module.processing_module;

import com.rabbitmq.client.Delivery;
import com.triton.module.Module;
import com.triton.util.ConvertCoordinate;
import com.triton.util.Vector2d;
import proto.vision.MessagesRobocupSslDetection.SSL_DetectionFrame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeoutException;

import static com.triton.messaging.Exchange.AI_BIASED_VISION_WRAPPER;
import static com.triton.messaging.Exchange.AI_VISION_WRAPPER;
import static com.triton.messaging.SimpleSerialize.simpleDeserialize;
import static proto.vision.MessagesRobocupSslDetection.SSL_DetectionBall;
import static proto.vision.MessagesRobocupSslDetection.SSL_DetectionRobot;
import static proto.vision.MessagesRobocupSslGeometry.*;
import static proto.vision.MessagesRobocupSslWrapper.SSL_WrapperPacket;

public class VisionBiasedConverter extends Module {

    public VisionBiasedConverter(ScheduledThreadPoolExecutor executor) {
        super(executor);
    }

    @Override
    protected void prepare() {

    }

    @Override
    protected void declareConsumes() throws IOException, TimeoutException {
        declareConsume(AI_VISION_WRAPPER, this::callbackWrapper);
    }

    private void callbackWrapper(String s, Delivery delivery) {
        SSL_WrapperPacket wrapper = (SSL_WrapperPacket) simpleDeserialize(delivery.getBody());
        SSL_WrapperPacket biasedWrapper = wrapperAudienceToBiased(wrapper);
        publish(AI_BIASED_VISION_WRAPPER, biasedWrapper);
    }

    private static SSL_WrapperPacket wrapperAudienceToBiased(SSL_WrapperPacket wrapper) {
        SSL_WrapperPacket.Builder biasedWrapper = wrapper.toBuilder();
        biasedWrapper.setDetection(detectionAudienceToBiased(wrapper.getDetection()));
        biasedWrapper.setGeometry(geometryAudienceToBiased(wrapper.getGeometry()));
        return biasedWrapper.build();
    }

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

    private static SSL_GeometryData geometryAudienceToBiased(SSL_GeometryData geometry) {
        SSL_GeometryData.Builder biasedGeometry = geometry.toBuilder();
        biasedGeometry.setField(fieldAudienceToBiased(geometry.getField()));
        return biasedGeometry.build();
    }

    private static Iterable<SSL_DetectionBall> ballsAudienceToBiased(List<SSL_DetectionBall> balls) {
        List<SSL_DetectionBall> biasedBalls = new ArrayList<>();
        balls.forEach(ball -> biasedBalls.add(ballAudienceToBiased(ball)));
        return biasedBalls;
    }

    private static Iterable<SSL_DetectionRobot> robotsAudienceToBiased(List<SSL_DetectionRobot> robots) {
        List<SSL_DetectionRobot> biasedRobots = new ArrayList<>();
        robots.forEach(robot -> biasedRobots.add(robotAudienceToBiased(robot)));
        return biasedRobots;
    }

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

    private static SSL_FieldLineSegment lineAudienceToBiased(SSL_FieldLineSegment fieldLine) {
        SSL_FieldLineSegment.Builder biasedFieldLine = fieldLine.toBuilder();
        biasedFieldLine.setP1(vector2fAudienceToBiased(fieldLine.getP1()));
        biasedFieldLine.setP2(vector2fAudienceToBiased(fieldLine.getP2()));
        return biasedFieldLine.build();
    }

    private static SSL_FieldCircularArc arcAudienceToBiased(SSL_FieldCircularArc fieldCircularArc) {
        SSL_FieldCircularArc.Builder biasedFieldCircularArc = fieldCircularArc.toBuilder();
        biasedFieldCircularArc.setCenter(vector2fAudienceToBiased(fieldCircularArc.getCenter()));
        biasedFieldCircularArc.setA1(0);
        biasedFieldCircularArc.setA2(360);
        return biasedFieldCircularArc.build();
    }

    private static Vector2f vector2fAudienceToBiased(Vector2f vector) {
        Vector2f.Builder biasedVector2f = vector.toBuilder();
        Vector2d biasedVector = ConvertCoordinate.audienceToBiased(vector.getX(), vector.getY());
        biasedVector2f.setX(biasedVector.x);
        biasedVector2f.setY(biasedVector.y);
        return biasedVector2f.build();
    }
}
