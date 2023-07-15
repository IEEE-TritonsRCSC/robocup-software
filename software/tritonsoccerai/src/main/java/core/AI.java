package core;

import java.lang.Thread;

import main.java.core.ai.GameInfo;
import main.java.core.ai.GameState;
import main.java.core.config.*;
import main.java.core.constants.AITest;
import main.java.core.constants.ProgramConstants;
import main.java.core.constants.Team;
import main.java.core.module.Module;
import main.java.core.module.aiModule.AIModule;
import main.java.core.module.aiModule.CentralCoordinatorModule;
import main.java.core.module.aiModule.FielderTreeModule;
import main.java.core.module.aiModule.GKTreeModule;
import main.java.core.module.interfaceModule.*;
import main.java.core.module.processingModule.*;

import static proto.triton.FilteredObject.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import static main.java.core.config.ConfigPath.*;
import static main.java.core.config.ConfigReader.readConfig;

import main.java.core.module.testModule.TestModule;
import org.apache.commons.cli.*;

public class AI {

    private final ScheduledThreadPoolExecutor executor;
    private final List<Module> modules;
    private final List<Future<?>> futures;

    private AI() {
        super();
        modules = new ArrayList<>();
        futures = new ArrayList<>();
        executor = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(1000);
        executor.setRemoveOnCancelPolicy(true);
    }

    /**
     * Starts modules and runs program in appropriate mode
     * 
     * @param args Arguments to program
     */
    public static void main(String[] args) throws org.apache.commons.cli.ParseException, java.lang.InterruptedException {
        if (parseArgs(args)) return;
        String currentDir = System.getProperty("user.dir");
        System.out.println(currentDir);
        loadConfigs();

        AI ai = new AI();
        ai.startSupportModules();
        Thread.sleep(5000);
        if (ProgramConstants.test) {
            ai.runTests();
        } else {
            ai.startAI();
        }
    }

    /**
     * Parses command line options
     * 
     * @param args Arguments to be parsed
     * @return whether ParseException occurred
     */
    private static boolean parseArgs(String[] args) throws org.apache.commons.cli.ParseException {
        Options options = new Options();
        Option teamOption = Option.builder("team")
                .longOpt("team")
                .argName("team")
                .hasArg()
                .required(true)
                .desc("set team to manage").build();
        options.addOption(teamOption);

        Option testOption = Option.builder("test")
                .longOpt("test")
                .argName("test")
                .required(false)
                .desc("whether to run in test mode").build();
        options.addOption(testOption);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter helper = new HelpFormatter();

        try {
            CommandLine cmd = parser.parse(options, args);
            if (cmd.hasOption(teamOption)) {
                if (cmd.getOptionValue(teamOption).equals(Team.YELLOW.getTeamString())) {
                    GameInfo.setTeamColor(Team.YELLOW);
                    GameInfo.setFoeTeamColor(Team.BLUE);
                } else {
                    GameInfo.setTeamColor(Team.BLUE);
                    GameInfo.setFoeTeamColor(Team.YELLOW);
                }
            }
            if (cmd.hasOption(testOption)) {
                ProgramConstants.test = true;
            }
        } catch (org.apache.commons.cli.ParseException e) {
            e.printStackTrace();
            helper.printHelp(" ", options);
            return true;
        }
        return false;
    }

    /**
     * Loads configs into ProgramConstants from config.yaml file
     */
    private static void loadConfigs() {
        ProgramConstants.aiConfig = (AIConfig) readConfig(AI_CONFIG);
        ProgramConstants.displayConfig = (DisplayConfig) readConfig(DISPLAY_CONFIG);
        ProgramConstants.gameConfig = (GameConfig) readConfig(GAME_CONFIG);
        ProgramConstants.networkConfig = (NetworkConfig) readConfig(NETWORK_CONFIG);
        ProgramConstants.objectConfig = (ObjectConfig) readConfig(OBJECT_CONFIG);
    }

    /**
     * Starts support (non-AI) modules
     */
    public void startSupportModules() {
        startProcessingModules();
        startInterfaceModules();
    }

    /**
     * Runs test mode by repeatedly prompting user for test to run and running that test
     */
    private void runTests() {
        List<Module> testModules = new ArrayList<>();
        List<Future<?>> testFutures = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Available tests:");
            for (AITest test : AITest.values())
                System.out.println("- " + test.ordinal() + ". " + test.name() + ":\n\t" + test.getDesc());

            System.out.print("Choose a test:\t");
            AITest test = parseTest(scanner.nextLine());
            System.out.println(test);

            if (test == null) {
                System.out.println("Test not found. Try again.");
                continue;
            }

            TestModule testModule = test.createNewTestModule(executor);
            ProgramConstants.commandPublishingModule = testModule;
            startModule(testModule, testModules, testFutures);

            while (!testModules.isEmpty()) {
                System.out.print("Running test, type 'q' to stop:\t");
                if (scanner.nextLine().equals("q")) {
                    testFutures.forEach(testFuture -> testFuture.cancel(false));
                    testModules.forEach(Module::interrupt);
                    testModules.clear();
                    testFutures.clear();
                }
            }
        }
    }

    /**
     * Starts AIModule, FielderTreeModule (x5), GKTreeModule, and CentralCoordinatorModule
     */
    public void startAI() {
        // core ai modules
        AIModule aiModule = new AIModule(executor);
        ProgramConstants.commandPublishingModule = aiModule;
        startModule(aiModule);
        GameInfo.setCurrState(GameState.OPEN_PLAY);
        for (int id = 1; id <= GameInfo.getFielders().size(); id++) {
            startModule(new FielderTreeModule(executor, id));
            System.out.println("Fielder module started for robot " + id);
        }
        startModule(new GKTreeModule(executor));
        System.out.println("GK module started");
        startModule(new CentralCoordinatorModule(executor));
        System.out.println("Central module started");
    }

    /**
     * Starts processing modules (refer to software diagram for info on the roles
     * of these modules)
     */
    public void startProcessingModules() {
        startModule(new VisionBiasedConverter(executor));
        startModule(new FilterModule(executor));
        startModule(new SimulatorControlAudienceConverter(executor));
        startModule(new RobotCommandAudienceConverter(executor));
        startModule(new TritonBotMessageBuilder(executor));
        System.out.println("Processing modules started");
    }

    /**
     * Starts interface modules (refer to software diagram for info on the roles
     * of these modules)
     */
    public void startInterfaceModules() {
        startModule(new CameraInterface(executor));
        startModule(new SimulatorCommandInterface(executor));
        startModule(new SimulatorRobotCommandInterface(executor));
        startModule(new TritonBotMessageInterface(executor));
        startModule(new UserInterface(executor));
        System.out.println("Interface modules started");
    }

    /**
     * Parses user input on test to be run and returns appropriate test
     * if valid test exists
     * 
     * @param line user input
     * @return applicable test if valid test exists
     */
    private AITest parseTest(String line) {
        for (AITest test : AITest.values())
            if (line.equals(test.name()) || line.equals(String.valueOf(test.ordinal())))
                return test;
        return null;
    }

    /**
     * Submits module runnable to executor to be run as a thread
     * and adds module and its future to appropriate lists
     * 
     * @param module Module (must be runnable) to be executed
     */
    public void startModule(Module module) {
        Future<?> future = this.executor.submit(module);
        this.modules.add(module);
        this.futures.add(future);
    }

    /**
     * Submits module runnable to executor to be run as a thread
     * and adds module and its future to appropriate lists
     * 
     * @param module Module (must be runnable) to be executed
     * @param modules List of modules to add module to
     * @param futures List of futures to add module's future to
     */
    public void startModule(Module module, List<Module> modules, List<Future<?>> futures) {
        Future<?> future = this.executor.submit(module);
        modules.add(module);
        futures.add(future);
    }

}
