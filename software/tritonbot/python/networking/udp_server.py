import socket
from threading import Thread

'''
UDP server that receive all the message/command
'''
class UDP_Server(Thread):
    LOCALHOST_ADDRESS = "localhost"
    BUF_SIZE = 9999

    # set up 
    def __init__(self, server_port, callback):
        super().__init__()
        self.server_port = server_port
        self.callback = callback

        self.socket = socket.socket(family=socket.AF_INET, type=socket.SOCK_DGRAM)
        self.socket.bind((UDP_Server.LOCALHOST_ADDRESS, server_port))

    def run(self):
        super().run()
        while (True):
            self.send(self.receive())

    # set up the receiver of the server
    def receive(self):
        packet = self.socket.recvfrom(UDP_Server.BUF_SIZE)
        self.client_address_port = packet[1]
        return self.callback(bytes=packet[0])

    # set up sender of the server
    def send(self, bytes):
        if (bytes == None or self.client_address_port == None):
            return
            
        self.socket.sendto(bytes, self.client_address_port)