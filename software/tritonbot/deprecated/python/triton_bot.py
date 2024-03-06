import argparse
import sys

from constant.runtime_constants import RuntimeConstants
from constant.team import Team
from module.processing_module.robot_command_local_to_wheel_processor import RobotCommandLocalToWheelProcessor
from module.interface_module.ai_interface import AI_Interface
from module.interface_module.simulator_robot_control_interface import \
    SimulatorRobotControlInterface
from module.processing_module.robot_command_global_to_local_processor import \
    RobotCommandGlobalToLocalProcessor
from module.processing_module.triton_bot_message_processor import \
    TritonBotMessageProcessor
from module.processing_module.vision_filter import VisionFilter

'''
main function to run tritonbot
'''
class TritonBot:
    # set up / constructor
    def __init__(self):
        print("Starting Modules for TritonBot " + str(RuntimeConstants.team) + " " + str(RuntimeConstants.id))
        self.start_modules()

    # start / turn on all listener/filter 
    def start_modules(self):
        TritonBotMessageProcessor().start()
        VisionFilter().start()
        RobotCommandGlobalToLocalProcessor().start()
        # RobotCommandLocalToWheelProcessor().start()

        AI_Interface().start()
        SimulatorRobotControlInterface().start()

# identify the team
def parseTeam(teamStr):
    for matchTeam in Team:
        if (matchTeam.value == args.team):
            return matchTeam
    return None

# define the team and the team id
parser = argparse.ArgumentParser()
parser.add_argument('--team', type=str, required=True)
parser.add_argument('--id', type=int, required=True)
args = parser.parse_args()

# error if cant find team
team = parseTeam(args.team)
if (team == None):
    print("Unable to parse team.")
    sys.exit(1)

RuntimeConstants.team = team
RuntimeConstants.id = args.id

# run
TritonBot()