import os
import yaml

# returns data in config file
def read_config(config):
    #relative path to config then joined with absolute path
    rel_path = config.value
    abs_file_path = os.path.join("../../../config", rel_path)

    with open(abs_file_path, 'r') as file:
        data = yaml.safe_load(file)
        return data