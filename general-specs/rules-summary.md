# Summary of 2023 Rules

Link: https://robocup-ssl.github.io/ssl-rules/sslrules.html

## The Field
- Division A: 13.4 meters times 10.4 meters with a playing area of 12 meters times 9 meters
- Division B: 10.4 meters times 7.4 meters with a playing area of 9 meters times 6 meters
- Error margin in dimensions is 10%
- We will probably be in Division B since we are a new team
- The field will be a green felt mat or carpet
- All marking are in white, either paint, carpet or felt mat
- The ball is a standard orange golf ball. 
  - It weights approximately 0.046 kilograms
  - Its diameter measures 0.043 meters.

## Provided Software and Technology
- Shared software will be provided and changes made less that 3 months before contest will not break compatibility
- Each field has a shared central vision server and set of shared cameras
   - https://github.com/RoboCup-SSL/ssl-vision
   - Information packets vie ethernet share localization data
   - Teams must be able to handle potential issues such as noise, letency, ocassionlal failed detection/classification 
   - Top of robots must have standard colour paper paterns as specified in documentation of vision software
   - We could potentially use an additional tracker protocol provided
- Game controller software will require communication with our software to allow gameplay

## Robots
- 6 robots for division B, including goal keeper
- A robot must not pose danger to itself, another robot, or humans. It must not damage or modify the ball or the field.
- Must fit inside a 0.18 meters wide and 0.15 meters high cylinder at any point in time
- Dribbler:
  - Must not elevate ball from ground
  - Another robot must be able to remove the ball from the dribbler
  - Must not remove all degrees of freedom 
  - 80% of ball area must be outside convex hull around robot
  - Colour Patterns
    - Top of robot must be flat, black or grey and with a matte finish
    - The center dot color determines the team and is either blue or yellow. The other four dot colors encode the id of the robot.
    - Guaranteed to fit within a circle with a radius of 0.085 meters that is linearly cut off on the front side of the robot to a distance of 0.055 meters from the centre 
    - IDs 0-7 are more stable
  - Robots must be fully autonomous
