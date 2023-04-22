# robocup-2023
Team Repository for IEEE TritonsRCSC 2023

Internal Documentation Website: https://ieee-tritonsrcsc.github.io/robocup-2023/

Build AI:
```
cd software/tritonsoccerai
bash build-ai.sh
cd ../..
```

Start RabbitMQ server: `sudo service rabbitmq-server start`

Run run.py in test mode: `python3 run.py --test=true --team=blue`

Run run.py in non-test mode: `python3 run.py --test=false --team=blue`

Stop RabbitMQ server: `sudo service rabbitmq-server stop`