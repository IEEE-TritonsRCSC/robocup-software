from generated_sources.proto.filtered_object_pb2 import Robot
from generated_sources.proto.messages_robocup_ssl_detection_pb2 import SSL_DetectionRobot
from messaging.exchange import Exchange
from module.module import Module

'''
camera information send to the triton bot global to local processor
'''
class VisionFilter(Module):
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
        self.declare_publish(Exchange.TB_VSION)

    def declare_consumes(self):
        super().declare_consumes()
        self.declare_consume(Exchange.TB_RAW_VISION, self.callback_vision)

    # listen to the channel constantly 
    def run(self):
        super().run()
        self.consume()
    
    # send message to the channel if dectect something in vision filter
    def callback_vision(self, ch, method, properties, body):
        vision = SSL_DetectionRobot()
        vision.ParseFromString(body)
        self.publish(exchange=Exchange.TB_VSION, object=vision)