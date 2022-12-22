# Drivetrain Specifications

## Motion Modelling
- Link 2 below provides arduino code that could serve as a starting point for motion modelling. It provides a limited range of movement but could be a good starting point.
- Our model should ideally be slightly more generalised and allow motion at multiple angles. If we have a generalised formula or a formula for motion in each quadrant, calcultaing teh exact speed for each motor dynamically will make quick directional changes much easier.
- Requirements for Initial modelling:
  - Have atleast 8 point motion and 360 degree rotation calculations complete
  - Come up with a set of general formulae to allow a greater rnge of motion if possible
  - Write detailed explanations for how yo uarrived at all conclusion
    - Keep track of all resources used
    - Write all fomulae used in their original form first before manipulation
    - Have all workings written out neatly with some explanations as to why you did what you did at each step

  ### Math and Physics Team Deliverables
  - All these deliverables should be included in a document called `drivetrain-calculations.md` (or another extenson), and uplaoded to the mechatronics folder in the Github Repository. Send a message on discord once that is done.
  - Formulae for directional and rotational motion using 4 omniwheel drive
  - Detailed documentation of all steps taken and formulae/theorems used to arrive at conclusions - following requirements listed above
  - A list of all resources used
  
## Structural Design
- Have the base and motor mount/retainer designs ready and uploaded to this google folder. [A LINK WILL BE INSERTED SOON]
- Each design should have a short acompanying document explaining design choices and how they may benefit the robot
- If you have the time, review some of the other designs once you upload yours and read their rationales. Add some comments about what you think about teh designs and suggest any improvements you can think of (as comments).
- These designs will be filtered and some of them will be cut using the laser cutter on wood for testing.

  ### CAD Team Deliverables
  - Base and motor retainer design files
  - Rationales explaining design decisions
  - Internal sub-team peer review of each other's designs if time permits

## Electronics
- Do some reserach on considerations for brushless dc motor connections and using lithum ion batteries (safety considerations primarily)
- Desin a schematic diagram of motor connections with ESC and the microcontroller along with any other re

## Useful Resources
1. https://www.societyofrobots.com/robot_omni_wheel.shtml: A general overview of the theory behind omniwheels
2. https://epshahrukh.blogspot.com/2020/04/4-wheel-omni-wheel-robot-based-on.html - A basic example of an onmiwheel drivetrain
