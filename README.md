# robocup-2023
Team Repository for IEEE TritonsRCSC 2023

Internal Documentation Website: https://ieee-tritonsrcsc.github.io/robocup-2023/

## Run Instructions

Build Framework:
```
sudo apt-get update
sudo apt-get install cmake protobuf-compiler libprotobuf-dev qtbase5-dev libqt5opengl5-dev g++ libusb-1.0-0-dev libsdl2-dev libqt5svg5-dev libssl-dev
sh build-framework.sh
```

Build Simulator:
```
sh build-simulator.sh
```

Build AI:
```
sh build-ai.sh
```

Start RabbitMQ server: `sudo service rabbitmq-server start`

Run run.py in test mode: `python3 run.py --test=true --team=blue`

Run run.py in non-test mode: `python3 run.py --test=false --team=blue`

Stop RabbitMQ server: `sudo service rabbitmq-server stop`

## Resources

RobocupSSL Website: https://ssl.robocup.org/
