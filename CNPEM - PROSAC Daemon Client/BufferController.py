'''

This module contains two classes. The first one, Command, represent all possible 
commands which can be sent to the SBC. The OTHER option is used if the uses chooses 
to send a command via ssh to the selected hosts.

The second class, BufferController, inherits from Thread and overrides the run() method. It also 
contains a command queue, which is filled by the other threads and emptied by that class. It corresponds
to the typical producer-consumer situation.  

Created on 01/08/2016

@author: gciotto
'''

import threading
from Control_Node import Control_Node, Control_Node_State
from tcp_connection import ProsacDaemonConnection
from PyQt4.Qt import QModelIndex
from Queue import Queue
import paramiko
import time

# Command class is equivalent to an enum type in C 
class Command():
    
    READ, REBOOT, OTHER = range(3)
    
    def __init__ (self, type = READ, target = Control_Node(), command = ""):
        
        self.type = type
        self.target = target
        self.command = command
        

# BufferController controls provides methods to control a command queue
class BufferController(threading.Thread):
    
    def __init__(self, window_controller = None):
        
        threading.Thread.__init__(self)
        
        self.m_command = threading.Lock()

        self.connection = ProsacDaemonConnection()
        
        # For other commands than REBOOT and READ, we use a SSH Client object
        self.ssh_client = paramiko.SSHClient()
        self.ssh_client.set_missing_host_key_policy(paramiko.AutoAddPolicy())
        
        self.command_queue = Queue()
        
        self.window_controller = window_controller
        
        self.stop_thread = False
        
    # Enqueues new element in the end of the queue
    def enqueue_command(self, command):
             
        self.command_queue.put(command)

    # Dequeues first element of the list
    def next_command (self):
        
        command = self.command_queue.get(block = True) 
        
        return command
    
    def is_empty(self):
                
        __r = len(self.command_queue) == 0
        
        return __r
    
    # Consumes and executes commands in the command queue 
    def run(self):
        
        while not self.stop_thread:
                        
            __next = self.next_command()
            
            if __next.type == Command.READ: 
                
                __r = self.connection.CheckStatus(__next.target.ip_address)
                
                if  not __r:
                    
                    __next.target.changeState(Control_Node_State.CONNECTED)
                    
                else:
                    __next.target.changeState(Control_Node_State.DISCONNECTED)
                    
                                    
            if __next.type == Command.REBOOT:
                
                __r = self.connection.RebootSBC(__next.target.ip_address)
                
                if  not __r:
                    
                    self.window_controller.log_signal.emit("Rebooting node %s (%s)"\
                                     % (str(__next.target.ip_address), __next.target.dns_name))
                    
                    __next.target.changeState(Control_Node_State.REBOOTING)
                    
                else:
                    __next.target.changeState(Control_Node_State.DISCONNECTED)
                    
            if __next.type == Command.OTHER:
                
                if __next.target.isConnected():
                
                    try:
                
                        __next.target.changeState(Control_Node_State.CMD_RUNNING)

                        self.ssh_client.connect(hostname = __next.target.ip_address,\
                                                username = "root", password = "root", timeout = 3)
                        
                    
                        stdin, stdout, stderr = self.ssh_client.exec_command(str(__next.command))
                    
                    except Exception, msg:
                        
                        print msg
                        
                        __next.target.changeState(Control_Node_State.DISCONNECTED)
                    
                    else: 
                
                        __next.target.changeState(Control_Node_State.CMD_OK)
                        
                        self.window_controller.log_signal.emit("Results of running '%s' @ %s (%s)"\
                                                     % (str(__next.command), str(__next.target.ip_address),\
                                                        __next.target.dns_name))
                        
                        # print normal output, if command was successful
                        for i in stdout.readlines():
                            
                            self.window_controller.log_signal.emit(str(i)[:-1])
                            
                        # print error output, if command failed
                        for i in stderr.readlines():
                            
                            self.window_controller.log_signal.emit(str(i)[:-1])
                        
                        self.window_controller.log_signal.emit("-" * 60)
                        
                    self.ssh_client.close()
                    
            # Emits signal to update table view
            self.window_controller.table_model.dataChanged.emit(QModelIndex(), QModelIndex())
