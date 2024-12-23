package main.java.core.constants;

import main.java.core.config.*;
import main.java.core.module.Module;
import main.java.core.messaging.Exchange;

public class ProgramConstants {

    public static boolean test = false;

    public static AIConfig aiConfig = null;
    public static DisplayConfig displayConfig = null;
    public static GameConfig gameConfig = null;
    public static NetworkConfig networkConfig = null;
    public static ObjectConfig objectConfig = null;

    public static Module commandPublishingModule;

    public static final int INITIAL_DELAY = 0; // initial loop delay in milliseconds
    public static final int LOOP_FREQUENCY = 20; // loops per second
    public static final long LOOP_DELAY = 1000 / LOOP_FREQUENCY; // milliseconds / loop

}
