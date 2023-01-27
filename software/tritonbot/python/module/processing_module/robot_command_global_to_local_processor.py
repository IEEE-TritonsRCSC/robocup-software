from math import cos, degrees, pi, radians, sin
import time

from generated_sources.proto.messages_robocup_ssl_detection_pb2 import SSL_DetectionRobot
from generated_sources.proto.ssl_simulation_robot_control_pb2 import MoveLocalVelocity, RobotCommand, RobotMoveCommand
from generated_sources.proto.ssl_simulation_robot_feedback_pb2 import RobotFeedback
from generated_sources.proto.object_with_metadata_pb2 import Robot
from messaging.exchange import Exchange
from module.module import Module


class RobotCommandGlobalToLocalProcessor(Module):
    def __init__(self):
        super().__init__()

    def load_config(self):
        super().load_config()

    def prepare(self):
        super().prepare()

    def declare_publishes(self):
        super().declare_publishes()
        self.declare_publish(exchange=Exchange.TB_LOCAL_COMMAND)
        self.declare_publish(exchange=Exchange.TB_MESSAGE)

    def declare_consumes(self):
        super().declare_consumes()
        self.declare_consume(exchange=Exchange.TB_VSION, callback=self.callback_vision)
        self.declare_consume(exchange=Exchange.TB_GLOBAL_COMMAND, callback=self.callback_global_command)

    def run(self):
        super().run()
        self.consume()

    def callback_vision(self, ch, method, properties, body):
        vision = SSL_DetectionRobot()
        vision.ParseFromString(body)
        self.latest_vision = vision

    def callback_global_command(self, ch, method, properties, body):
        if (not hasattr(self, 'latest_vision')):
            return

        global_command = RobotCommand()
        global_command.ParseFromString(body)

        vx = global_command.move_command.global_velocity.x
        vy = global_command.move_command.global_velocity.y
        angular = global_command.move_command.global_velocity.angular

        orientation = self.latest_vision.orientation

        angular_correction = 0.135
        rotation = -(orientation + angular * angular_correction) + pi / 2

        local_vx = vx * cos(rotation) - vy * sin(rotation)
        local_vy = vx * sin(rotation) + vy * cos(rotation)
        local_angular = angular

        local_command = RobotCommand()
        local_command.CopyFrom(global_command)
        local_command.move_command.local_velocity.left = -local_vx
        local_command.move_command.local_velocity.forward = local_vy
        local_command.move_command.local_velocity.angular = local_angular

        self.publish(exchange=Exchange.TB_LOCAL_COMMAND, object=local_command)