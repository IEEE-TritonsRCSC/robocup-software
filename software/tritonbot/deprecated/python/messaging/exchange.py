from enum import Enum
import enum

'''
set up of the channel for rabbitmq listener
'''
class Exchange(Enum):
    TB_MESSAGE = enum.auto()
    TB_RAW_VISION = enum.auto()
    TB_VSION = enum.auto()
    TB_GLOBAL_COMMAND = enum.auto()         
    TB_LOCAL_COMMAND = enum.auto()
    TB_WHEEL_COMMAND = enum.auto()
    TB_FEEDBACK = enum.auto()
