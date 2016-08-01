'''
Created on 01/08/2016

@author: gciotto
'''

import threading
from ClientInterface import Control_Node, Control_Node_State
from tcp_connection import ProsacDaemonConnection
import time
from PyQt4.Qt import QModelIndex, QApplication
import paramiko



class Command():
    
    READ, REBOOT, OTHER = range(3)
    
    def __init__ (self, type = READ, target = Control_Node(), command = ""):
        
        self.type = type
        self.target = target
        self.command = command
        

class BufferController(threading.Thread):
    
    def __init__(self, window_controller = None):
        
        threading.Thread.__init__(self)
        
        self.m_command = threading.Lock()

        self.connection = ProsacDaemonConnection()
        
        self.ssh_client = paramiko.SSHClient()
        self.ssh_client.set_missing_host_key_policy(paramiko.AutoAddPolicy())
        
        self.command_queue = []
        
        self.window_controller = window_controller
        
        self.stop_thread = False
        
    def enqueue_command(self, command):
        
        self.m_command.acquire()
        
        self.command_queue.append(command)
        
        self.m_command.release()

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
                    
                            self.ssh_client.connect(__next.target.ip_address, "root", "root", timeout = 3)
                        
                            stdin, stdout, stderr = self.ssh_client.exec_command(__next.command)
                        
                            self.ssh_client.close()
                            
                        except Exception:
                            
                            __next.target.changeState(Control_Node_State.DISCONNECTED)
                        
                        else: 
                    
                            __next.target.changeState(Control_Node_State.CMD_OK)
                            
                            print stdin, stdout, stderr
                            
                            
                
                self.window_controller.table_model.dataChanged.emit(QModelIndex(), QModelIndex())
       