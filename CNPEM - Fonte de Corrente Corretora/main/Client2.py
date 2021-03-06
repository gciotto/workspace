'''
    Client module is responsible to start the application. Basically, it creates two threads: 
    * the first one instantiates a CommandWindow object and opens it. 
    * the second one uses the Requester object from the CommandWindow object to send reading commands
      periodically. By default, the period was defined to 100 ms.
      
    Dependencies : python-qt4, install with 'sudo apt-get install python-qt4'
    
    Created on Jun 21, 2016

    @author: gciotto
'''
from gui.CommandWindow import CommandWindow
from Requester import Requester
from PyQt4.QtGui import QApplication
import sys
import threading

# read_command_thread: sends reading commands periodically to the boards through object a's
# Requester object. It stops after the user closes the CommandWindow window. 
def reading_command_thread(): 
    
    event = threading.Event()
    
    print 'Passou'
    
    while not requester.canStart(): 
        event.wait(1)
    
    while not requester.isFinished():
        
        if requester.isConnected:
            
            requester.execute_command(0x0)
            
            event.wait(0.1)
        
    print 'Acabou'
 
def window_thread():
    
    app = QApplication(sys.argv)

    w_command = CommandWindow()
    w_command.setRequester(requester)
        
    w_command.show()   
      
    sys.exit(app.exec_())
 
# Main 'function': instantiates a CommandWindow object and starts it. 
if __name__ == '__main__':
    
    reader = threading.Thread(target = reading_command_thread)
    w_thread = threading.Thread(target = window_thread)
    
    requester = Requester(None)
    
    w_thread.start()
    reader.start()
    
    
        
