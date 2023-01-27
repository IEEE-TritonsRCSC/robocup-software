
from imp import source_from_cache
from pickle import loads
from config.config_path import ConfigPath
from config.config_reader import read_config
from generated_sources.proto.triton_bot_communication_pb2 import TritonBotMessage

# Imports Exchange file, which contains all RabbitMQ exchanges
from messaging.exchange import Exchange
# Imports Module file, which contains all RabbitMQ related functions
from module.module import Module


class TritonBotMessageProcessor(Module):
    def __init__(self):
        super().__init__()

    def load_config(self):
        super().load_config()
        self.network_config = read_config(
            ConfigPath.NETWORK_CONFIG)

    def prepare(self):
        super().prepare()

    # Subscribes as publisher to exchanges TB_RAW_VISION, TB_GLOBAL_COMMAND, TB_LOCAL_COMMAND, TB_WHEEL_COMMAND
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

    def run(self):
        super().run()
        self.consume()

    def callback_message(self, ch, method, properties, body):
        message = TritonBotMessage()
        message.ParseFromString(body)
        
        self.publish(exchange=Exchange.TB_RAW_VISION, object=message.vision)
        
        if (message.HasField('command')):
            exchange = Exchange.TB_WHEEL_COMMAND
            if (message.command.HasField('move_command')):
                if (message.command.move_command.HasField('global_velocity')):
                    exchange = Exchange.TB_GLOBAL_COMMAND
                elif (message.command.move_command.HasField('local_velocity')):
                    exchange = Exchange.TB_LOCAL_COMMAND
            self.publish(exchange=exchange, object=message.command)
