from pickle import load

from config.config_path import ConfigPath
from config.config_reader import read_config
from generated_sources.proto.ssl_simulation_robot_control_pb2 import (
    RobotCommand, RobotControl
)
from generated_sources.proto.ssl_simulation_robot_feedback_pb2 import \
    RobotControlResponse
from constant.runtime_constants import RuntimeConstants
from constant.team import Team
from messaging.exchange import Exchange
from module.module import Module
from networking.udp_client import UDP_Client

class SimulatorRobotControlInterface(Module):
    def __init__(self):
        super().__init__()
        self.last_time_stamp = 0

    def load_config(self):
        super().load_config()
        self.network_config