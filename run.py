from ast import arg
import subprocess 
import os
import time
import argparse

parser = argparse.ArgumentParser(description="Run programs.")
parser.add_argument('--test', choices=('true', 'false'))
parser.add_argument('--team', choices=('yellow', 'blue', 'both'))
args = parser.parse_args()

def run_cmd(cmd, path, mode='current'):
    # annoying lengthy path is removed from the print out
    prt_cmd_str = (' '.join(map(str, cmd))).replace(triton_bot_path,"")

    if (mode == 'current'):
        print(">>> running " + prt_cmd_str + " in current terminal")
        subprocess.Popen(cmd, cwd=path)
    if (mode == 'background'):
        print(">>> running " + prt_cmd_str + " as background process")
        subprocess.Popen(cmd, cwd=path, stdout=subprocess.PIPE) 
    if (mode == 'tab'):
        print(">>> running " + prt_cmd_str + " in a new terminal tab")
        subprocess.Popen(["gnome-terminal", "--tab", "--"] + cmd, cwd=path)
        print(path)


# setup paths
dir_path = os.path.dirname(os.path.realpath(__file__))

simulator_path = dir_path + "/framework/build/bin"
simulator = "./simulator-cli"

game_controller_path = dir_path + "/game-controller"
game_controller = "./ssl-game-controller_v2.13.0_linux_amd64"

ssl_vision_client_path = dir_path + "/ssl-vision-client"
ssl_vision_client = "./ssl-vision-client_v1.6.0_linux_amd64"

triton_soccer_ai_jar_path = dir_path + "/software/tritonsoccerai/target/"
triton_soccer_ai_jar = "triton-soccer-ai-1.0-SNAPSHOT-jar-with-dependencies.jar"

triton_soccer_ai_py_path = dir_path + "/triton-soccer-ai/src/main/python"
triton_soccer_ai_py = ""

triton_bot_path = dir_path + "/software/tritonbot/python/"
triton_bot = "triton_bot.py"

# run cmds
run_cmd([simulator, "-g", "2020B", "--realism", "RC2021"], simulator_path, "tab")
time.sleep(0.1)

run_cmd([game_controller], game_controller_path, "tab")
time.sleep(0.1)

run_cmd([ssl_vision_client], ssl_vision_client_path, "tab")
time.sleep(0.1)

num_bots = 6

if (args.team == 'yellow' or args.team == 'both'):
    for i in range(num_bots):
        id = str(i)
        run_cmd(["python3", triton_bot, "--team=yellow", "--id", id], triton_bot_path, "tab")
        time.sleep(0.1)

if (args.team == 'blue' or args.team == 'both'):
    for i in range(num_bots):
        id = str(i)
        run_cmd(["python3", triton_bot, "--team=blue", "--id", id], triton_bot_path, "tab")
        time.sleep(0.1)

if (args.team == 'yellow' or args.team == 'both'):
    if (args.test == 'true'):
        run_cmd(["java", "-jar", triton_soccer_ai_jar, "--team=yellow", "--test"], triton_soccer_ai_jar_path, "tab")
        time.sleep(0.1)
    else:
        run_cmd(["java", "-jar", triton_soccer_ai_jar, "--team=yellow"], triton_soccer_ai_jar_path, "tab")
        time.sleep(0.1)

if (args.team == 'blue' or args.team == 'both'):
    if (args.test == 'true'):
        run_cmd(["java", "-jar", triton_soccer_ai_jar, "--team=blue", "--test"], triton_soccer_ai_jar_path, "tab")
        time.sleep(0.1)
    else:
        run_cmd(["java", "-jar", triton_soccer_ai_jar, "--team=blue"], triton_soccer_ai_jar_path, "tab")
        time.sleep(0.1)