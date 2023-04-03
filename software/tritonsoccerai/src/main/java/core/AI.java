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
import main.java.core.module.interfaceModule.CameraInterface;
import main.java.core.module.interfaceModule.SimulatorCommandInterface;
import main.java.core.module.interfaceModule.TritonBotMessageInterface;
import main.java.core.module.interfaceModule.UserInterface;
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

    public static void main(String[] args) throws org.apache.commons.cli.ParseException, java.lang.InterruptedException {
        if (parseArgs(args)) return;
        loadConfigs();

        AI ai = new AI();
        ai.startSupportModules();
        Thread.sleep(1000);
        if (ProgramConstants.test) {
            ai.runTests();
        } else {
            ai.startAI();
        }
    }

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

    private static void loadConfigs() {
        ProgramConstants.aiConfig = (AIConfig) readConfig(AI_CONFIG);
        ProgramConstants.displayConfig = (DisplayConfig) readConfig(DISPLAY_CONFIG);
        ProgramConstants.gameConfig = (GameConfig) readConfig(GAME_CONFIG);
        ProgramConstants.networkConfig = (NetworkConfig) readConfig(NETWORK_CONFIG);
        ProgramConstants.objectConfig = (ObjectConfig) readConfig(OBJECT_CONFIG);
    }

    public void startSupportModules() {
        startProcessingModules();
        startInterfaceModules();
    }

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

            if (test == null) {
                System.out.println("Test not found. Try again.");
                continue;
            }

            TestModule testModule = test.createNewTestModule(executor);
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

    public void startAI() {
        // core ai modules
        AIModule aiModule = new AIModule(executor);
        ProgramConstants.aiModule = aiModule;
        startModule(aiModule);
        GameInfo.setCurrState(GameState.PREPARE_KICKOFF);
        for (Robot fielder : GameInfo.getFielders()) {
            startModule(new FielderTreeModule(executor, fielder));
        }
        startModule(new GKTreeModule(executor));
        startModule(new CentralCoordinatorModule(executor));
    }

    public void startProcessingModules() {
        startModule(new VisionBiasedConverter(executor));
        startModule(new FilterModule(executor));
        startModule(new SimulatorControlAudienceConverter(executor));
        startModule(new RobotCommandAudienceConverter(executor));
        startModule(new TritonBotMessageBuilder(executor));
    }

    public void startInterfaceModules() {
        startModule(new CameraInterface(executor));
        startModule(new SimulatorCommandInterface(executor));
//        startModule(new SimulatorRobotCommandInterface(executor));
        startModule(new TritonBotMessageInterface(executor));
        startModule(new UserInterface(executor));
    }

    private AITest parseTest(String line) {
        for (AITest test : AITest.values())
            if (line.equals(test.name()) || line.equals(String.valueOf(test.ordinal())))
                return test;
        return null;
    }

    public void startModule(Module module) {
        Future<?> future = this.executor.submit(module);
        this.modules.add(module);
        this.futures.add(future);
    }

    public void startModule(Module module, List<Module> modules, List<Future<?>> futures) {
        Future<?> future = this.executor.submit(module);
        modules.add(module);
        futures.add(future);
    }

}
