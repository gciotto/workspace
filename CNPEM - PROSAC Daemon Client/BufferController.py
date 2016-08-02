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
import paramiko

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
        
        self.command_queue = []
        
        self.window_controller = window_controller
        
        self.stop_thread = False
        
    # Enqueues new element in the end of the queue
    def enqueue_command(self, command):
        
        self.m_command.acquire()
        
        self.command_queue.append(command)
        
        self.m_command.release()

    # Dequeues first element of the list
    def next_command (self):
        
        self.m_command.acquire()
        
        command = self.command_queue.pop(0)
        
        self.m_command.release()
        
        return command
    
    def is_empty(self):
        
        self.m_command.acquire()
        
        __r = len(self.command_queue) == 0
        
        self.m_command.release()
        
        return __r
    
    # Consumes and executes commands in the command queue 
    def run(self):
        
        while not self.stop_thread:
        
            if not self.is_empty():
                
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
                        
                        __next.target.changeState(Control_Node_State.REBOOTING)
                        
                    else:
                        __next.target.changeState(Control_Node_State.DISCONNECTED)
                        
                if __next.type == Command.OTHER:
                    
                    if __next.target.isConnected():
                    
                        try:
                    
                            __next.target.changeState(Control_Node_State.CMD_EXECUTING)
                            
                            self.ssh_client.connect(hostname = __next.target.ip_address,\
                                                    username = "root", password = "root", timeout = 3)
                        
                        
                            stdin, stdout, stderr = self.ssh_client.exec_command(str(__next.command))
                        
                        except Exception, msg:
                            
                            print msg
                            
                            __next.target.changeState(Control_Node_State.DISCONNECTED)
                        
                        else: 
                    
                            __next.target.changeState(Control_Node_State.CMD_OK)
                            
                            self.window_controller.log_signal.emit("Results of running '%s' @ %s"\
                                                         % (str(__next.command), str(__next.target.ip_address)))
                            
                            for i in stdout.readlines():
                                
                                self.window_controller.log_signal.emit(str(i)[:-1])
                                
                            for i in stderr.readlines():
                                
                                self.window_controller.log_signal.emit(str(i)[:-1])
                            
                            self.window_controller.log_signal.emit("===================================")
                            
                        self.ssh_client.close()
                        
                        
                self.window_controller.table_model.dataChanged.emit(QModelIndex(), QModelIndex())
       