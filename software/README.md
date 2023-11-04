# Software Setup

You may set up Linux in any way that works for you! Whether that be a virtual machine or dual booting it onto your computer. Just be sure that you are able to properly install everything you need. Here are some additional resources: 
Window's Subsystem Linux: https://docs.microsoft.com/en-us/windows/wsl/install 
Ubuntu: https://ubuntu.com/ 
Instructional articles on how to dual boot: 
https://opensource.com/article/18/5/dual-boot-linux https://www.howtogeek.com/214571/how-to-dual-boot-linux-on-your-pc/ 

Linux is not the only setup you will need. Here is the full recommended setup sequence: 
1) Linux 
2) Java 17 (Must be 17!!!) 
3) Python 
4) Apache Maven https://linuxize.com/post/how-to-install-apache-maven-on-ubuntu-20-04/ 
5) Python PIP https://linuxize.com/post/how-to-install-pip-on-ubuntu-20.04/ 
6) RabbitMQ https://www.rabbitmq.com/download.html 
7) Python Pika https://subscription.packtpub.com/book/application-development/9781783981526/11/ch11lvl1sec61/pika 
8) Python Protobuf https://developers.google.com/protocol-buffers 
9) Gazebo https://classic.gazebosim.org/download 
10) Our RoboCup code! https://github.com/IEEE-TritonsRCSC/robocup-2023
11) ER-Force Simulator https://github.com/robotics-erlangen/framework/tree/b342652846464c4dc789079f5ddb468d8ba3595f

# A Few Pointers on Issues You May Face (courtesy of Huaming)

1. The first error you might encounter is: 
```
FileNotFoundError: [Error 2] No such file or directory: '/home/{Your User}/Desktop/robocup-2023/framework/build/bin'
In order to fix this, you have to make sure that you create the build directory in framework.
```

```cd``` into the framework directory 

(The download will take a long time) 

```
mkdir build
cd build
cmake ..
make
```

2. The second error might be 
```Error constructing proxy for org.gnome.Terminal:/org/gnome/Terminal/Factory0: Could not connect: No such file or directory```
Use 
```sudo python3 run.py --test=false --team=both```


3. If you are able to actually open up the terminals and you don't see the actual robot things and the soccer field, you might be getting a 
```java.lang.UnsatisfiedLinkError```
 in one of the terminals.

Check if 
```libawt_xawt.so```
 exists where it should be using
```ls /usr/lib/jvm/java-17-openjdk-amd64/lib/libawt_xawt.so```


If it doesnt, try: 
```
sudo apt-get update
sudo apt-get install openjdk-17-jdk
```

There might be a mismatch in the library path due to different versions of Java.

# More Onboarding Information

https://docs.google.com/presentation/d/1VAli_Ta1lNODmwKvDTK2hvJAlG427KbK8hj_T-ndtEQ/edit?usp=sharing
