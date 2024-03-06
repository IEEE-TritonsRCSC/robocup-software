
# Essentially sets up how the ./tritonbot/python/module/interface_module/ai_interface.py
# can configure and understand the path


from enum import Enum

# Network config path class for passing into config reader
class ConfigPath(Enum):
        NETWORK_CONFIG = "network_config.yaml"