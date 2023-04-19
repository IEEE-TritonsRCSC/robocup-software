# This file essentially is a reader for the Triton Bot to 
# understand what files are being sent 
# over the RabbitMQ channel.
#
# This file is imported and used in ./tritonbot/python/module/interface_module/ai_interface.py
# Why in the world we 1 million helper method-files is beyond me.
#
# - Kyle Trinh

import os    # importing the ability to read these proto files received from the RabbitMQ network
import yaml  # library to read the proto files and relay them to the rest of the modules on the triton bot

# returns data in config file
def read_config(config):
    # relative path to config then joined with absolute path
    print(os.getcwd())
    rel_path = config.value
    abs_file_path = os.path.join("../../config", rel_path)

    with open(abs_file_path, 'r') as file:
        data = yaml.safe_load(file)
        return data