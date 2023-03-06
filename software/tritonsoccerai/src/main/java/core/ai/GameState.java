package main.java.core.ai;

/**
 * Defines every possible state that the game may be in at any given time
 */
public enum GameState {
    HALT,
    TIMEOUT,
    STOP,
    PREPARE_DIRECT_FREE,
    PREPARE_INDIRECT_FREE,
    PREPARE_KICKOFF,
    PREPARE_PENALTY,
    NORMAL_START,
    FORCE_START,
    BALL_PLACEMENT,
    OPEN_PLAY
}
