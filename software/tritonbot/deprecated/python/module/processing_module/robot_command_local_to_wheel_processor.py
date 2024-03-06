from math import cos, degrees, pi, radians, sin, sqrt
import time

from generated_sources.proto.messages_robocup_ssl_detection_pb2 import SSL_DetectionRobot
from generated_sources.proto.ssl_simulation_robot_control_pb2 import MoveLocalVelocity, RobotCommand, RobotMoveCommand
from messaging.exchange import Exchange
from module.module import Module


class RobotCommandLocalToWheelProcessor(Module):
    # set up
    def __init__(self):
        super().__init__()

    def load_config(self):
        super().load_config()

    def prepare(self):
        super().prepare()

     # start subcribes to rabbitmq publisher and consumer (send and receive)
    def declare_publishes(self):
        super().declare_publishes()
        self.declare_publish(exchange=Exchange.TB_WHEEL_COMMAND)

    def declare_consumes(self):
        super().declare_consumes()
        self.declare_consume(exchange=Exchange.TB_LOCAL_COMMAND, callback=self.callback_local_command)

    #listen to the channel constantly
    def run(self):
        super().run()
        self.consume()

    # actually command the wheel (specifically) to move 
    #do we have concern about those math (?) -> talk with mech about this
    def callback_local_command(self, ch, method, properties, body):
        local_command = RobotCommand()
        local_command.ParseFromString(body)

        local_vx = -local_command.move_command.local_velocity.left
        local_vy = local_command.move_command.local_velocity.forward
        local_speed = sqrt(local_vx * local_vx + local_vy * local_vy)

        wheel_front_right = (-sqrt(2) / 2, sqrt(2) / 2)
        wheel_back_right = (sqrt(2) / 2, sqrt(2) / 2)
        wheel_back_left = (-sqrt(2) / 2, sqrt(2) / 2)
        wheel_front_left = (sqrt(2) / 2, -sqrt(2) / 2)
        
        wheel_front_right_vel = local_vx * wheel_front_right[0] + local_vy * wheel_front_right[1]
        wheel_back_right_vel = local_vx * wheel_back_right[0] + local_vy * wheel_back_right[1]
        wheel_back_left_vel = local_vx * wheel_back_left[0] + local_vy * wheel_back_left[1]
        wheel_front_left_vel = local_vx * wheel_front_left[0] + local_vy * wheel_front_left[1]

        wheel_front_right_vec = (wheel_front_right_vel * wheel_front_right[0], wheel_front_right_vel * wheel_front_right[1])
        wheel_back_right_vec = (wheel_back_right_vel * wheel_back_right[0], wheel_back_right_vel * wheel_back_right[1])
        wheel_back_left_vec = (wheel_back_left_vel * wheel_back_left[0], wheel_back_left_vel * wheel_back_left[1])
        wheel_front_left_vec = (wheel_front_left_vel * wheel_front_left[0], wheel_front_left_vel * wheel_front_left[1])

        # bot vel = (max(pos vxs) + min(neg vxs), max(pos vys) + min(neg vys))
        bot_vx = max(wheel_front_right_vec[0], wheel_back_right_vec[0], wheel_back_left_vec[0], wheel_front_left_vec[0], 0) + \
            min(wheel_front_right_vec[0], wheel_back_right_vec[0], wheel_back_left_vec[0], wheel_front_left_vec[0], 0)
        bot_vy = max(wheel_front_right_vec[1], wheel_back_right_vec[1], wheel_back_left_vec[1], wheel_front_left_vec[1], 0) + \
            min(wheel_front_right_vec[1], wheel_back_right_vec[1], wheel_back_left_vec[1], wheel_front_left_vec[1], 0)
        bot_speed = sqrt(bot_vx * bot_vx + bot_vy * bot_vy)
        scale = local_speed / bot_speed

        wheel_command = RobotCommand()
        wheel_command.CopyFrom(local_command)

        wheel_command.move_command.wheel_velocity.front_right = wheel_front_right_vel * scale
        wheel_command.move_command.wheel_velocity.back_right = wheel_back_right_vel * scale
        wheel_command.move_command.wheel_velocity.back_left = wheel_back_left_vel * scale
        wheel_command.move_command.wheel_velocity.front_left = wheel_front_left_vel * scale

        self.publish(exchange=Exchange.TB_WHEEL_COMMAND, object=wheel_command)
