# How to Get Started

Read the rest of this file to get a sense of how our code works and the software setup you must complete in order to contribute. Reference the diagrams that have been created (especially for behavior tree design) under ```software/figures```. These will be references throughout the ```Software Overview``` section. Look through the onboarding slides in detail. Reference the most recent Team Description Paper (TDP), which can be found in the ```#resources``` channel of our team Discord. 

A few important guidelines/recommendations are:
* ALWAYS follow best practices when writing code. This includes commenting and documenting ALL code that you write, using short and appropriate variable lines, having each line not exceed 100 characters, and generally keeping in mind that your code will need to be read by others and updated often. All pull requests MUST conform to these standards in order to be accepted after 2 code reviews by fellow team members are completed.
* If you are assigned to complete a code review, do so ASAP. You must ensure that the code you are reviewing conforms to all standards listed above and maintains compatibility. NEVER blindly accept code or write no comments. Be thorough and constructive in your comments.
* Use design patterns as general frameworks to write good code. Not only will this help you develop faster, these are also good practices to use and follow, and is highly desired by top software companies. Learn about design patterns here: https://refactoring.guru/design-patterns.
* Have a strong understanding of Agile development processes, in particular Scrum which is what we use. Virtually every good software team uses an Agile framework and so will we to make quick progress towards our goals and objetives. Both teams must produce a Sprint Backlog for EVERY sprint. Refer to the Product Backlog for a list of items to be completed and their respective priorities.

# Software Overview

## Modules & RabbitMQ

In order to complete numerous interdependent tasks in parallel (simultaneously), we use a number of modules. Refer to Figure 9 for the general outline of every module in our software model. Modules allow us to complete independent tasks seperately at the same time to speed up our code performance and split our codebase into smaller chunks.

We utilize RabbitMQ as a server to facilitate inter-modular communications using a publisher-subscriber model with channels. Here is an analogy: channels are like buckets, some modules have permission to place things in the bucket (this is called publishing and is like writing), and some modules have permission to see what is in the bucket (this is called subscribing and is like reading). 

A list of all usable TritonSoccerAI channels can be found in ```software/tritonsoccerai/src/main/java/messaging/Exchange.java```. A list of all usable TritonBot channels can be found in ```software/tritonbot/python/messaging/exchange.py```. 

Each module may have a method called ```declareConsumes``` which is used to declare the channels that module is subscribed to. A module can be subscribed to more than one channel. For every of these channels, there must exist a ```callback``` method, which is used to define how to process and utilize a packet recieved from a channel. Refer to modules under ```software/tritonsoccerai/src/main/java/core/module``` or ```software/tritonbot/python/module``` for examples of these.

In order to start the RabbitMQ server, use the command ```sudo service rabbitmq-server start```. To stop the RabbitMQ server, use the command ```sudo service rabbitmq-server stop```. The server MUST be running in order for the code to work.

## AI & Behavior Trees

### General

Our behavior trees have a number of types of nodes, which are defined under ```software/tritonsoccerai/src/main/java/core/ai/behaviorTree/nodes```. Most importantly are task nodes that do specific and small tasks (they are leaf nodes) and service nodes that execute at a specified frequency (look at any root node and its corresponding service to see how this works).

Both the fielder and goalkeeper trees:
* Have a referee commands branch for when a new referee command is received from the Game Controller (this is on the left side of the corresponding figures)
* Have offense and defense branches governing behavior for when our team does or does not have possession of the ball

Nodes not specific to either the fielder or goalkeeper trees are located under ```software/tritonsoccerai/src/main/java/core/ai/behaviorTree/robotTrees/basicFunctions```.

These functionalities include:
* Moving to a specified location or object
* Kicking the ball
* Checking if a given robot has possession
* Checking if a given robot is closest to the ball
* Chasing after the ball
* Catching (receiving) the ball
* Dribbling the ball in a particular direction

All behavior trees are run in parallel and one tree is run for EACH robot. This means that each robot acts INDEPENDENTLY and allows for quicker decision-making but comes with the drawback of reduced coordination.

### Fielder Tree

The nodes specific to the fielder behavior tree can be found under ```software/tritonsoccerai/src/main/java/core/ai/behaviorTree/robotTrees/fielder```. Refer to Figures 2-4 for more info about the fielder tree and the nodes and functionalities associated with it. In summary, when a new referee command is received, the appropriate action is completed. Otherwise, when the team is on offense, the robot positions itself if it does not have possession and either shoots, passes, or dribbles when it does. When the team is on defense, the robot chases the ball if it is the closest ally (one of our robots) to the ball. Otherwise, it marks a foe (opposing robot). See Figure 8 for more info about fielder coverage while on defense.

### Goalkeeper Tree

The nodes specific to the fielder behavior tree can be found under ```software/tritonsoccerai/src/main/java/core/ai/behaviorTree/robotTrees/goalkeeper```. Refer to Figure 5 for more info about the  goalkeeper tree and the nodes and functionailities associated with it. In summary, when a new referee command is received, the appropriate action is completed. Otherwise, when the team is on offense, the robot positions itself if it does not have possession and passes when it does. When the team is on defense, the robot positions itself on the correct position on the semicircular arc connecting the goalposts. See Figure 7 for more info about goalkeeper movement on defense.

### Central Coordinator

Nodes related to the central coordinator can be found under ```software/tritonsoccerai/src/main/java/core/ai/behaviorTree/robotTrees/central```. The central coordinator is responsible for coordinating certain actions like passing and formations and serves as the only central unit in an otherwise decentralized model.

## Proto & League-Wide Software

There is a lot of league-wide software that we need to be compatible with in order to compete. This software also simplifies our development significantly as it allows us to focus on our own AI, robots, firmware, and more. More info about league-wide software can be found here: https://ssl.robocup.org/league-software/.

Here are the key pieces of software to be aware of:
* ```SSL-Vision```: This is responsible for using camera data from one or more cameras located above the field to locate all robots and the ball, which is received as coordinates by teams.
* ```SSL-Game-Controller```: This is responsible for communicating referee commands to teams and serving as an interface for auto-referees and human referees. 
* Simulators (```ER-Force``` and ```grSim```): Simulators are used to simulate games on a computer without the use of any robots, which allows for the testing of AI by itself. We currently use the ER-Force simulator.

Google Protobuf is used as a common protocol that allows for the communication of information from SSL-Vision to teams and from teams to any compatible simulator. Proto files can be found under ```software/tritonsoccerai/src/main/proto``` or ```software/tritonbot/proto```. These proto files can be compiled into code by Google Protobuf in numerous languages, allowing for teams to use whatever language they prefer. Generated code in Java for TritonSoccerAI can be found under ```software/tritonsoccerai/target/generated-sources/protobuf/java/proto```. Generated code in Python for TritonBot can be found under ```software/tritonbot/python/generated_sources/proto```.

## Flow of Information

The general flow of information throughout our software is outlined by Figure 9. 

The ```Camera Interface``` acts as an intermediary between the source of match data, which is recieved from SSL-Vision in competition setup and from the simulator in the simulation setup, and our AI. All of this data is then converted from the audience perspective to our team's perspective by the ```Vision Biased Converter```. Refer to Figure 6 for more info about this. This data is then filtered for noise and metadata is added by the ```Filter Module```. The ```Game Controller Interface``` is the intermediary between the Game Controller and our AI. All of the information originating from SSL-Vision/Simulator and Game Controller that can be used by ```AI``` to make decisions is stored in the ```GameInfo``` class.

The commands generated by ```AI``` are typically sent to the ```Robot Command Audience Converter``` to be converted from our team's perspective to the audience persective. Refer to Figure 6 for more info about this. These commands are then sent to the Simulator via the ```Simulator Robot Command Interface``` in the simulation setup or to TritonBot via the ```TritonBot Message Builder``` and ```TritonBot Message Interface``` in the competition setup.

The ```UserInterface``` module also consumes from the ```Filter Module``` to display the state of the game on our display (this is what you see when you run our code).

## Pathfinding & Obstacle Avoidance

We currently model the field as a number of 2D nodes, and assign penalties to each node depending on constant obstacles (opposing box, out of bounds) and moving obstacles (other robots). This helps avoid obstacles and navigate the best path to a desired location. All code relating to pathfinding and obstacle-avoidance can be found under ```software/tritonsoccerai/src/main/java/core/search```. We hope to improve our obstacle avoidance in the future and utilize Jump Point Search (JPS) to improve our pathfinding.

# Software Setup

You may set up Linux in any way that works for you! Whether that be a virtual machine or dual booting it onto your computer. Just be sure that you are able to properly install everything you need. Here are some additional resources: 
* Window's Subsystem Linux: https://docs.microsoft.com/en-us/windows/wsl/install 
* Ubuntu: https://ubuntu.com/ 
* Instructional articles on how to dual boot: 
https://opensource.com/article/18/5/dual-boot-linux, https://www.howtogeek.com/214571/how-to-dual-boot-linux-on-your-pc/ 

General Setup Instructions: https://docs.google.com/presentation/d/1tQH7DabZe_yh0HxkCa0rP3AuYi7znsym1NE0sY958sw/edit#slide=id.p

Linux is not the only setup you will need. Here is the full recommended setup sequence (in the slides above): 
1) Linux (Ubuntu 20.04 recommended)
2) Java 17 (Must be 17!!!) 
3) Python 
4) Apache Maven https://linuxize.com/post/how-to-install-apache-maven-on-ubuntu-20-04/ 
5) Python PIP https://linuxize.com/post/how-to-install-pip-on-ubuntu-20.04/ 
6) RabbitMQ https://www.rabbitmq.com/download.html 
7) Python Pika https://subscription.packtpub.com/book/application-development/9781783981526/11/ch11lvl1sec61/pika 
8) Python Protobuf https://developers.google.com/protocol-buffers 
9) Gazebo https://classic.gazebosim.org/download 
10) Our RoboCup code! https://github.com/IEEE-TritonsRCSC/robocup-2023
11) ER-Force Simulator https://github.com/robotics-erlangen/framework/tree/b342652846464c4dc789079f5ddb468d8ba3595f

For 11, make sure you download the required packages: https://github.com/robotics-erlangen/framework/blob/b342652846464c4dc789079f5ddb468d8ba3595f/COMPILE.md

E.g. for Ubuntu:
```
sudo apt-get update
sudo apt-get install cmake protobuf-compiler libprotobuf-dev qtbase5-dev libqt5opengl5-dev g++ libusb-1.0-0-dev libsdl2-dev libqt5svg5-dev libssl-dev
```

Also don't forget to make the build directory (Check below)

# A Few Pointers on Issues You May Face

### Error 1

The first error you might encounter is: 
```
FileNotFoundError: [Error 2] No such file or directory: '/home/{Your User}/Desktop/robocup-2023/framework/build/bin'
In order to fix this, you have to make sure that you create the build directory in framework. 
```

```cd``` into the framework directory 

```
mkdir build
cd build
cmake ..
make simulator-cli
```

Note: You could also just use make instead of make simulator-cli, but it is not necessary

### Error 2

The second error might be 
```Error constructing proxy for org.gnome.Terminal:/org/gnome/Terminal/Factory0: Could not connect: No such file or directory```
Use 
```sudo python3 run.py --test=false --team=both```

### Error 3

If you are able to actually open up the terminals and you don't see the actual robot things and the soccer field, you might be getting a 
```java.lang.UnsatisfiedLinkError```
 in one of the terminals.

Check if 
```libawt_xawt.so```
 exists where it should be using
```ls /usr/lib/jvm/java-17-openjdk-amd64/lib/libawt_xawt.so```


If it doesnt, try: 
```
sudo apt-get update
sudo apt-get install openjdk-17-jdk
```

There might be a mismatch in the library path due to different versions of Java.

### Error 4

For build-ai.sh you might receive this error: ```java.io.IOException: Cannot run program "/usr/local/bin/protoc": error=2, No such file or directory```, your protoc might not be located at that specific path.

Check where it is located using ```which protoc```

If it is in a different path, create a symbolic link ```sudo ln -s {Your Path Here} /usr/local/bin/protoc```
   
You can check again to make sure that it is the right path
   
(also remember to CTRL - C after u finish running the file since it keeps running)

# More Onboarding Information

https://docs.google.com/presentation/d/1VAli_Ta1lNODmwKvDTK2hvJAlG427KbK8hj_T-ndtEQ/edit?usp=sharing
