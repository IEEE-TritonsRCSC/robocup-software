# Essentially sets up how the ./tritonbot/python/module/interface_module/ai_interface.py
# can configure and understand the path
from enum import Enum

# Network config file path.
class ConfigPath(Enum):
        NETWORK_CONFIG = "network_config.yall"