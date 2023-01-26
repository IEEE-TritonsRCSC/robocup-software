from os import wait
import socket
from threading import Thread
from queue import Empty, Queue

class UDP_Client(Thread):
    BUF_SIZE = 9999
    QUEUE_CAPACITY = 5

    def __init__(self, server_address, server_port, callback, timeout=0):
        super().__init__()
        self.server_address = server_address
        self.server_port = server_port
        self.callback = callback

        self.socket = socket.socket(family=socket.AF_INET, type=socket.SOCK_DGRAM)
        self.socket.settimeout(timeout)
        self.send_queue = Queue(5)
    
    def run(self):
        super().run()
        while (True):
            self.recieve(self.send())

    def send(self):
        try:
            bytes = self.send_queue.get()
        except Empty:
            return False

        self.socket.sendto(bytes, (self.server_address, self.server_port))
        return True
    
    def receive(self, receive):
        if (not receive or self.callback == None):
            return
        
        try:
            packet = self.socket.recvfrom(UDP_Client.BUF_SIZE)
        except socket.timeout:
            return
        except socket.error:
            return

        self.callback(bytes=packet[0])

    def add_send(self, bytes):
        if (self.send_queue.full()):
            self.send_queue.get()
        self.send_queue.put(bytes)