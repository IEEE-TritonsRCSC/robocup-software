# This import is not being used. We are probably missing some sort of
# instructions for how the tritonbot recieves information from the 
# ai to move the motors a certain amount
from math import degrees

# Not sure what exactly this import does, but something about 
# it being similar to Perl extensions?
from re import L

# The above two imports here are used to read the .proto files.
# Pretty much it (these files are very short)
from config.config_path import ConfigPath 
from config.config_reader import read_config

# These import certain Constants (file is completely empty)
# and also imports the Teams to differentiate between "our" and "their" bots.
from constant.runtime_constants import RuntimeConstants
from constant.team import Team

# The actual, fun, cool part of the program.
# These read the messages from the generated python files from the proto
# the triton bot well then execuete these commands and then "consume"
# the message, on which the Rabbit MQ server learns of this and then
# returns it to the AI. Like a feedback loop essentially.
# -KT
from generated_sources.proto.triton_bot_communication_pb2 import TritonBotMessage
from generated_sources.proto.ssl_simulation_robot_feedback_pb2 import RobotFeedback
from messaging.exchange import Exchange

# Missing, not sure what needs to be here.
from module.module import Module

# How it connects to the UDP Server (RMQ)
from networking.udp_server import UDP_Server

'''
interface to communicate with AI
'''
class AI_Interface(Module):
    message_sent_count = 1
    
    # Send Commands to triton bot
    def __init__(self):
        super().__init__()

    def load_config(self):
        super().load_config()
        self.network_config = read_config(
            ConfigPath.NETWORK_CONFIG
        )

    # set up
    def prepare(self):
        super().prepare()

        self.feedback = RobotFeedback()
        self.feedback.id = RuntimeConstants.id
        self.feedback.dribbler_ball_contact = False

        self.setup_client()

    # Subcribe to the channel to receive and send message to AI through rabbitmq
    def declare_publishes(self):
        super().declare_publishes()
        self.declare_publish(exchange=Exchange.TB_MESSAGE)
        
    def declare_consumes(self):
        super().declare_consumes()
        self.declare_consume(exchange=Exchange.TB_FEEDBACK, callback=self.callback_feedback)


    # listen to message constantly
    def run(self):
        super().run()
        self.consume()
    
    # Send commands from triton bot to sim
    def setup_client(self):
        if (RuntimeConstants.team == Team.YELLOW):
            server_port = self.network_config['tritonBotPortBaseYellow'] + \
                RuntimeConstants.id * self.network_config['tritonBotPortIncr']
        else:
            server_port = self.network_config['tritonBotPortBaseBlue'] + \
                RuntimeConstants.id * self.network_config['tritonBotPortIncr']

        self.server = UDP_Server(
            server_port=server_port, callback=self.callback_message)
        print("UDP server started")
        self.server.start() 

    # receive feedback from robot (why is it in AI interface then?)
    def callback_feedback(self, ch, method, properties, body):
        feedback = RobotFeedback()
        feedback.ParseFromString(body)
        self.feedback = feedback

    # send message to channel
    def callback_message(self, bytes):
        #print("Sending message " + str(self.message_sent_count) + " from TB AI interface")
        self.message_sent_count += 1
        message = TritonBotMessage()
        message.ParseFromString(bytes)
        self.publish(exchange=Exchange.TB_MESSAGE, object=message)
        return self.feedback.SerializeToString()

