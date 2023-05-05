package main.java.core.constants;

import main.java.core.config.*;
import main.java.core.module.aiModule.AIModule;
import main.java.core.messaging.Exchange;
import static main.java.core.messaging.Exchange.AI_ROBOT_COMMAND;
import static main.java.core.messaging.Exchange.AI_BIASED_ROBOT_COMMAND;

public class ProgramConstants {

    public static boolean test = false;

    public static AIConfig aiConfig = null;
    public static DisplayConfig displayConfig = null;
    public static GameConfig gameConfig = null;
    public static NetworkConfig networkConfig = null;
    public static ObjectConfig objectConfig = null;

    public static AIModule aiModule;
    // either AI_BIASED_ROBOT_COMMAND or AI_ROBOT_COMMAND based on whether simulator is being used
    // AI_BIASED_ROBOT_COMMAND = simulator NOT in use
    public static Exchange moduleToPublishAICommands = AI_BIASED_ROBOT_COMMAND; 

    public static final int INITIAL_DELAY = 0; // initial loop delay in milliseconds
    public static final int LOOP_FREQUENCY = 1; // loops per second
    public static final long LOOP_DELAY = 1000 / LOOP_FREQUENCY; // milliseconds / loop

}
