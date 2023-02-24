package java.core.messaging;

public enum Exchange {
    // Received from the camera
    AI_VISION_WRAPPER,

    // Feedback sent back after sending a command, tells whether the robot is in contact with the ball
    AI_ROBOT_FEEDBACKS,

    // Camera data processed into a biased team perspective
    AI_BIASED_VISION_WRAPPER,

    // Camera data processed into a biased team perspective, then filtered to reduce noise
    AI_FILTERED_VISION_WRAPPER,

    // Commands sent from our team perspective
    AI_BIASED_SIMULATOR_CONTROL,
    AI_BIASED_ROBOT_CONTROL,
    AI_BIASED_ROBOT_COMMAND,

    // Commands sent to the simulator
    AI_SIMULATOR_CONTROL,   // Commands the simulator, teleport robots/balls around
    AI_SIMULATOR_CONFIG,    // Commands the simulator, set robot specs, geometry, etc.
    AI_ROBOT_COMMAND,       // Controls robots in the simulator

    // Commands sent to triton bot
    AI_TRITON_BOT_MESSAGE,  // A combined message sent to a triton bot containing vision and commands for that triton bot

    AI_DEBUG,
}