
from imp import source_from_cache
from pickle import loads
from config.config_path import ConfigPath
from config.config_reader import read_config
from generated_sources.proto.triton_bot_communication_pb2 import TritonBotMessage

# Imports Exchange file, which contains all RabbitMQ exchanges
from messaging.exchange import Exchange
# Imports Module file, which contains all RabbitMQ related functions
from module.module import Module

'''
message system to communicate with the AI
Process AI message 
'''
class TritonBotMessageProcessor(Module):
    # set up
    def __init__(self):
        super().__init__()

    def load_config(self):
        super().load_config()
        self.network_config = read_config(
            ConfigPath.NETWORK_CONFIG)

    def prepare(self):
        super().prepare()

    # start subcribes to rabbitmq publisher and consumer (send and receive)
    def declare_publishes(self):
        super().declare_publishes()
        self.declare_publish(Exchange.TB_RAW_VISION)
        self.declare_publish(Exchange.TB_GLOBAL_COMMAND)
        self.declare_publish(Exchange.TB_LOCAL_COMMAND)
        self.declare_publish(Exchange.TB_WHEEL_COMMAND)

    # Subscribes as consumer to exchange TB_MESSAGE
    def declare_consumes(self):
        super().declare_consumes()
        self.declare_consume(Exchange.TB_MESSAGE, self.callback_message)

    # listen to the channel constantly
    def run(self):
        super().run()
        self.consume()

    # take info from the raw vision then submit command/message to the chanel
    def callback_message(self, ch, method, properties, body):
        message = TritonBotMessage()
        message.ParseFromString(body)
        
        #how is this different from vision filter (?)
        self.publish(exchange=Exchange.TB_RAW_VISION, object=message.vision)
        
        # check for move command then send the message to the chanel 
        # to command the robot to move accordingly
        if (message.HasField('command')):
            exchange = Exchange.TB_WHEEL_COMMAND
            if (message.command.HasField('move_command')):
                if (message.command.move_command.HasField('global_velocity')):
                    exchange = Exchange.TB_GLOBAL_COMMAND
                elif (message.command.move_command.HasField('local_velocity')):
                    exchange = Exchange.TB_LOCAL_COMMAND
            self.publish(exchange=exchange, object=message.command)
