#!/usr/bin/python

'''

Main module. It launches all three threads (GUI, buffer consumer and producer).

Created on 01/08/2016

@author: gciotto
'''

from ClientInterface import ClientInterface
from PyQt4.QtGui import QApplication
import sys
from BufferController import BufferController, Command
import threading
import time
from Control_Node import Network_Nodes

# This thread produces READ commands and enqueues them in the command queue (refer to the BufferController
# module) every 'refresh_delay' seconds.
def reading_command_thread():
    
    while w_command.still_on:
        
        if w_command.should_monitor:
            
            for __node in Network_Nodes.nodes:
                b_controller.enqueue_command(Command(Command.READ, __node))
            
        
        time.sleep(w_command.refresh_delay)

# Main 'function': instantiates a ClientInterface object and starts it. 
if __name__ == '__main__':
    
    reader = threading.Thread(target = reading_command_thread)
    reader.setDaemon(True)
    
    app = QApplication(sys.argv)

    w_command = ClientInterface()
    
    b_controller = BufferController(window_controller = w_command)
    b_controller.setDaemon(True)
        
    w_command.buffer_controller = b_controller    
        
    w_command.show()   
    
    b_controller.start()
    reader.start()
    
    sys.exit(app.exec_())
