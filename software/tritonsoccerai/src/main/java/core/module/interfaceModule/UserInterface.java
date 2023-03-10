package main.java.core.module.interfaceModule;

import com.rabbitmq.client.Delivery;

import main.java.core.ai.GameInfo;
import main.java.core.module.Module;

import main.java.core.search.implementation.PathfindGrid;
import main.java.core.search.node2d.Node2d;
import main.java.core.util.Vector2d;
import main.java.core.constants.Team;

import org.apache.commons.math3.analysis.function.Sigmoid;

import proto.triton.FilteredObject;
import proto.vision.MessagesRobocupSslGeometry.SSL_FieldCircularArc;
import proto.vision.MessagesRobocupSslGeometry.SSL_FieldLineSegment;
import proto.vision.MessagesRobocupSslGeometry.SSL_GeometryFieldSize;
import proto.vision.MessagesRobocupSslGeometry.Vector2f;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static main.java.core.constants.ProgramConstants.*;
import static main.java.core.messaging.Exchange.AI_DEBUG;
import static main.java.core.messaging.Exchange.AI_FILTERED_VISION_WRAPPER;
import static main.java.core.messaging.SimpleSerialize.simpleDeserialize;
import static main.java.core.util.ObjectHelper.predictOrientation;
import static main.java.core.util.ObjectHelper.predictPos;
import static main.java.core.util.ObjectHelper.predictRobotPos;
import static java.awt.BorderLayout.*;
import static java.awt.Color.*;
import static javax.swing.BoxLayout.Y_AXIS;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

import static proto.triton.AiDebugInfo.*;
import static proto.triton.FilteredObject.Ball;
import static proto.triton.FilteredObject.FilteredWrapperPacket;

public class UserInterface extends Module {
    private static final String MAIN_FRAME_TITLE = "Triton Soccer AI";
    private JFrame frame;
    private JPanel northPanel;
    private JPanel southPanel;
    private JPanel eastPanel;
    private JPanel westPanel;
    private JPanel centerPanel;
    private FieldPanel fieldPanel;

    public UserInterface(ScheduledThreadPoolExecutor executor) {
        super(executor);
    }

    @Override
    protected void prepare() {
        prepareGUI();
    }

    /**
     * Set up the GUI of the simulator
     * 
     */
    private void prepareGUI() {
        frame = new JFrame(MAIN_FRAME_TITLE);
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        northPanel = new JPanel();
        northPanel.setBackground(YELLOW);

        southPanel = new JPanel();
        southPanel.setBackground(MAGENTA);

        eastPanel = new JPanel();
        eastPanel.setBackground(BLUE);

        westPanel = new JPanel();
        westPanel.setBackground(RED);

        centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, Y_AXIS));
        centerPanel.setBackground(BLACK);

        fieldPanel = new FieldPanel();
        fieldPanel.setBackground(WHITE);
        centerPanel.add(Box.createVerticalGlue());
        centerPanel.add(fieldPanel, CENTER);
        centerPanel.add(Box.createVerticalGlue());

        frame.add(northPanel, NORTH);
        frame.add(southPanel, SOUTH);
        frame.add(eastPanel, EAST);
        frame.add(westPanel, WEST);
        frame.add(centerPanel, CENTER);
        frame.setVisible(true);
    }

    @Override
    protected void declareConsumes() throws IOException, TimeoutException {
        declareConsume(AI_FILTERED_VISION_WRAPPER, this::callbackWrapper);
        declareConsume(AI_DEBUG, this::callbackDebug);
    }

    /**
     * Called when a delivery is received.
     * Creates the FilteredWrapperPacket from the delivery and set a filedPanel wrapper.
     * 
     * @param s
     * @param delivery
     */
    private void callbackWrapper(String s, Delivery delivery) {
        FilteredWrapperPacket wrapper = (FilteredWrapperPacket) simpleDeserialize(delivery.getBody());
        fieldPanel.setWrapper(wrapper);
        fieldPanel.repaint();
    }

    /**
     * Called when a delivery is received.
     * Creates the Debug object from the delivery and add Debug to a filedPanel.
     * 
     * @param s
     * @param delivery
     */
    private void callbackDebug(String s, Delivery delivery) {
        Debug debug = (Debug) simpleDeserialize(delivery.getBody());
        fieldPanel.addDebug(debug);
        fieldPanel.repaint();
    }


    private class FieldPanel extends JPanel {
        private final ReadWriteLock wrapperLock;
        private final List<Debug> debug;
        private final Map<Integer, DebugPath> alliesPaths;
        private final ReadWriteLock debugLock;
        private FilteredWrapperPacket wrapper;
        private PathfindGrid pathfindGrid;

        public FieldPanel() {
            wrapperLock = new ReentrantReadWriteLock();
            debugLock = new ReentrantReadWriteLock();

            debug = new ArrayList<>();
            alliesPaths = new HashMap<>();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D graphics2D = (Graphics2D) g;
            wrapperLock.readLock().lock();
            debugLock.readLock().lock();
            try {
                if (wrapper == null) return;
                transformGraphics(graphics2D);
                paintField(graphics2D);
                paintBall(graphics2D);
                paintAllies(graphics2D);
                paintFoes(graphics2D);
                paintDebug(graphics2D);
            } finally {
                wrapperLock.readLock().unlock();
                debugLock.readLock().unlock();
            }
        }

        /**
         * Translates graphics by half of the total width and length of the field, 
         * so that the field is centered in the component.
         * 
         * @param graphics2D
         */
        private void transformGraphics(Graphics2D graphics2D) {
            SSL_GeometryFieldSize field = wrapper.getField();

            int totalFieldDisplayWidth = field.getFieldWidth()
                    + 2 * field.getBoundaryWidth()
                    + 2 * displayConfig.fieldExtend;
            int totalFieldDisplayLength = field.getFieldLength()
                    + 2 * field.getGoalDepth()
                    + 2 * field.getBoundaryWidth()
                    + 2 * displayConfig.fieldExtend;

            float xScale = (float) getParent().getWidth() / totalFieldDisplayWidth;
            float yScale = (float) getParent().getHeight() / totalFieldDisplayLength;
            float minScale = Math.min(xScale, yScale);

            Dimension dimension = new Dimension((int) (totalFieldDisplayWidth * minScale), (int) (totalFieldDisplayLength * minScale));
            setMinimumSize(dimension);
            setMaximumSize(dimension);
            setPreferredSize(dimension);

            graphics2D.scale(minScale, -minScale);
            graphics2D.translate(totalFieldDisplayWidth / 2, -totalFieldDisplayLength / 2);
        }


        /**
         * Paint the field in the simulator
         * 
         * @param graphics2D
         */
        private void paintField(Graphics2D graphics2D) {
            SSL_GeometryFieldSize field = wrapper.getField();

            int totalFieldDisplayWidth = field.getFieldWidth()
                    + 2 * field.getBoundaryWidth()
                    + 2 * displayConfig.fieldExtend;
            int totalFieldDisplayLength = field.getFieldLength()
                    + 2 * field.getGoalDepth()
                    + 2 * field.getBoundaryWidth()
                    + 2 * displayConfig.fieldExtend;

            graphics2D.setColor(DARK_GRAY);
            graphics2D.fillRect(-totalFieldDisplayWidth / 2,
                    -totalFieldDisplayLength / 2,
                    totalFieldDisplayWidth,
                    totalFieldDisplayLength);

            for (SSL_FieldLineSegment sslFieldLineSegment : field.getFieldLinesList()) {
                Vector2f p1 = sslFieldLineSegment.getP1();
                Vector2f p2 = sslFieldLineSegment.getP2();

                graphics2D.setColor(WHITE);
                graphics2D.drawLine((int) p1.getX(),
                        (int) p1.getY(),
                        (int) p2.getX(),
                        (int) p2.getY());
            }

            for (SSL_FieldCircularArc sslFieldCircularArc : field.getFieldArcsList()) {
                Vector2f center = sslFieldCircularArc.getCenter();
                float radius = sslFieldCircularArc.getRadius();
                float a1 = sslFieldCircularArc.getA1();
                float a2 = sslFieldCircularArc.getA2();

                graphics2D.setColor(WHITE);
                graphics2D.drawArc((int) (center.getX() - radius / 2),
                        (int) (center.getY() - radius / 2),
                        (int) radius,
                        (int) radius,
                        (int) Math.toDegrees(a1),
                        (int) Math.toDegrees(a2));
            }

            graphics2D.setColor(WHITE);
            graphics2D.drawRect(-field.getGoalWidth() / 2,
                    -field.getFieldLength() / 2 - field.getGoalDepth(),
                    field.getGoalWidth(),
                    field.getGoalDepth());

            graphics2D.setColor(WHITE);
            graphics2D.drawRect(-field.getGoalWidth() / 2,
                    field.getFieldLength() / 2,
                    field.getGoalWidth(),
                    field.getGoalDepth());
        }

        /**
         * Paint a ball in the simulator
         * 
         * @param graphics2D
         */
        private void paintBall(Graphics2D graphics2D) {
            Ball ball = GameInfo.getBall();

            float radius = objectConfig.objectToCameraFactor * objectConfig.ballRadius;

            if (ball.getConfidence() == 1f) {
                graphics2D.setColor(MAGENTA);
                graphics2D.fillArc((int) (ball.getX() - radius),
                        (int) (ball.getY() - radius),
                        (int) radius * 2,
                        (int) radius * 2,
                        0,
                        360);
            }

            graphics2D.setColor(BLACK);
            graphics2D.drawArc((int) (ball.getX() - radius),
                    (int) (ball.getY() - radius),
                    (int) radius * 2,
                    (int) radius * 2,
                    0,
                    360);

            if (displayConfig.showPrediction) {
                Vector2d predictPos = predictPos(ball, displayConfig.predictionDelta);
                graphics2D.setColor(MAGENTA);
                graphics2D.drawArc((int) (predictPos.x - radius),
                        (int) (predictPos.y - radius),
                        (int) radius * 2,
                        (int) radius * 2,
                        0,
                        360);
            }
        }

        /**
         * Paint Allies in the simulator
         * 
         * @param graphics2D
         */
        private void paintAllies(Graphics2D graphics2D) {
            Map<Integer, FilteredObject.Robot> allies = wrapper.getAlliesMap();

            for (FilteredObject.Robot ally : allies.values()) {
                Color fillColor;
                switch (GameInfo.getTeamColor()) {
                    case YELLOW -> fillColor = ORANGE;
                    case BLUE -> fillColor = BLUE;
                    default -> throw new IllegalStateException("Unexpected team value");
                }
                paintRobot(graphics2D, ally, fillColor, GREEN);
            }
        }

        /**
         * Paint Foes in the simulator
         * 
         * @param graphics2D
         */
        private void paintFoes(Graphics2D graphics2D) {
            Map<Integer, FilteredObject.Robot> foes = wrapper.getFoesMap();

            for (FilteredObject.Robot foe : foes.values()) {
                Color fillColor;
                switch (GameInfo.getTeamColor()) {
                    case YELLOW -> fillColor = BLUE;
                    case BLUE -> fillColor = ORANGE;
                    default -> throw new IllegalStateException("Unexpected team value");
                }
                paintRobot(graphics2D, foe, fillColor, RED);
            }
        }

        /**
         * Paint debug information and path lines in the simulator
         * 
         * @param graphics2D
         */
        private void paintDebug(Graphics2D graphics2D) {
            SSL_GeometryFieldSize field = wrapper.getField();
            Map<Integer, FilteredObject.Robot> allies = wrapper.getAlliesMap();
            Map<Integer, FilteredObject.Robot> foes = wrapper.getFoesMap();

            if (pathfindGrid == null)
                pathfindGrid = new PathfindGrid(field);

            if (displayConfig.showNodeGrid) {
                pathfindGrid.updateObstacles(allies, foes, null);

                if (displayConfig.showOnlyObstacles) {
                    List<Node2d> obstacles = pathfindGrid.getObstacles();
                    obstacles.forEach(node -> {
                        paintNode(graphics2D, node);
                    });
                } else {
                    Map<Vector2d, Node2d> nodeMap = pathfindGrid.getNodeMap();
                    nodeMap.forEach((pos, node) -> {
                        paintNode(graphics2D, node);
                    });
                }
            }

            //Draw path line
            alliesPaths.forEach((id, path) -> {
                if (displayConfig.showRoute) {
                    List<DebugVector> nodes = path.getNodesList();
                    graphics2D.setColor(YELLOW);
                    for (int i = 1; i < nodes.size(); i++) {
                        DebugVector prevNode = nodes.get(i - 1);
                        DebugVector currentNode = nodes.get(i);
                        graphics2D.drawLine((int) prevNode.getX(),
                                (int) prevNode.getY(),
                                (int) currentNode.getX(),
                                (int) currentNode.getY());
                    }
                }

                DebugVector fromPos = path.getFromPos();
                DebugVector toPos = path.getToPos();
                DebugVector nextPos = path.getNextPos();

                if (displayConfig.showNext) {
                    graphics2D.setColor(GREEN);
                    graphics2D.drawLine((int) fromPos.getX(),
                            (int) fromPos.getY(),
                            (int) nextPos.getX(),
                            (int) nextPos.getY());
                }

                if (displayConfig.showTo) {
                    graphics2D.setColor(RED);
                    graphics2D.drawLine((int) nextPos.getX(),
                            (int) nextPos.getY(),
                            (int) toPos.getX(),
                            (int) toPos.getY());
                }
            });
        }

        /**
         * Paint a robot in the simulator
         * 
         * @param graphics2D
         * @param robot
         * @param fillColor
         * @param outlineColor
         */
        private void paintRobot(Graphics2D graphics2D, FilteredObject.Robot robot, Color fillColor, Color outlineColor) {
            float radius = objectConfig.objectToCameraFactor * objectConfig.robotRadius;

            graphics2D.setColor(fillColor);
            graphics2D.fillArc((int) (robot.getX() - radius),
                    (int) (robot.getY() - radius),
                    (int) radius * 2,
                    (int) radius * 2,
                    0,
                    360);

            graphics2D.setColor(outlineColor);
            graphics2D.drawArc((int) (robot.getX() - radius),
                    (int) (robot.getY() - radius),
                    (int) radius * 2,
                    (int) radius * 2,
                    0,
                    360);

            graphics2D.setColor(BLACK);
            float orientation = robot.getOrientation();
            graphics2D.drawLine((int) robot.getX(),
                    (int) robot.getY(),
                    (int) (robot.getX() + radius * Math.cos(orientation)),
                    (int) (robot.getY() + radius * Math.sin(orientation)));

            if (displayConfig.showPrediction) {
                graphics2D.setColor(outlineColor);
                Vector2d predictPos = predictRobotPos(robot, displayConfig.predictionDelta);
                graphics2D.drawArc((int) (predictPos.x - radius),
                        (int) (predictPos.y - radius),
                        (int) radius * 2,
                        (int) radius * 2,
                        0,
                        360);

                float predictOrientation = predictOrientation(robot, displayConfig.predictionDelta);
                graphics2D.drawLine((int) predictPos.x,
                        (int) predictPos.y,
                        (int) (predictPos.x + radius * Math.cos(predictOrientation)),
                        (int) (predictPos.y + radius * Math.sin(predictOrientation)));
            }

            graphics2D.setColor(WHITE);
            setFont(new Font(displayConfig.robotIdFontName, Font.BOLD, displayConfig.robotIdFontSize));
            AffineTransform orgi = graphics2D.getTransform();
            graphics2D.translate(robot.getX(), robot.getY());
            graphics2D.scale(1, -1);
            graphics2D.drawString(String.valueOf(robot.getId()), 0, 0);
            graphics2D.setTransform(orgi);
        }


        /**
         * Paint nodes in the simulator
         * 
         * @param graphics2D
         * @param node
         */
        private void paintNode(Graphics2D graphics2D, Node2d node) {
            Sigmoid sigmoid = new Sigmoid();
            float scaled = (float) sigmoid.value(node.getPenalty() / aiConfig.penaltyScale);
            Color color = Color.getHSBColor(scaled, scaled, scaled);
            graphics2D.setColor(color);

            float x = node.getPos().x;
            float y = node.getPos().y;
            float radius = aiConfig.nodeRadius;
            graphics2D.drawArc((int) (x - radius),
                    (int) (y - radius),
                    (int) radius * 2,
                    (int) radius * 2,
                    0,
                    360);
        }

        /**
         * Add debug information to the component
         * 
         * @param debug
         */
        public void addDebug(Debug debug) {
            debugLock.writeLock().lock();
            try {
                this.debug.add(debug);
                if (debug.hasPath()) {
                    DebugPath path = debug.getPath();
                    alliesPaths.put(path.getId(), path);
                }
            } finally {
                debugLock.writeLock().unlock();
            }
        }

        public void setWrapper(FilteredWrapperPacket wrapper) {
            this.wrapper = wrapper;
        }
    }
}
