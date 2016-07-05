'''
    Created on Jun 22, 2016
    
    CommandWindow allows the user to control power supply via PROSAC commands and protocol. It inherits 
    from QT QMainWindow class.
    
    Dependencies : python-qt4, install with 'sudo apt-get install python-qt4'
    
    @author: gciotto
'''

from PyQt4.QtGui import QMainWindow, QHBoxLayout, QVBoxLayout, QGroupBox,\
    QGridLayout, QLabel, QPushButton, QColor
from PyQt4.QtCore import * 
from PyQt4.Qt import QWidget, QLineEdit
from util.Board import Board
from gui.SourceWindow import SourceWindow
from gui.QLed import QLed
from main.Requester import Requester
from random import randint

class CommandWindow (QMainWindow):
    
    # Constructs a new CommandWindow
    def __init__(self, parent = None):
        
        QMainWindow.__init__(self, parent)
        self.main_widget = QWidget()
        
        # Builds all groups
        self.build_connection_group()        
        self.build_source_windows()
        self.build_buttons_group()
        self.build_commands_group()
        self.build_log_group()
        
        # Initially the client is disconnected. Consequently, no boards
        # are available
        self.sources_group.setVisible(False)
        
        self.main_layout = QGridLayout()
        self.main_layout.setSpacing(10)
        
        # Groups all components in the main widget
        self.main_layout.addWidget(self.connect_group, 0, 0, 1, 3)
        self.main_layout.addWidget(self.commands_group, 1, 0, 1, 5)
        self.main_layout.addWidget(self.sources_group, 2, 0, 1, 5)
        self.main_layout.addWidget(self.log_group, 3, 0, 1, 5)
        
        self.main_widget.setLayout(self.main_layout)
        
        self.statusBar().showMessage("Client started.")
        self.setCentralWidget(self.main_widget)
        self.setWindowTitle('Commands Window')
        self.setFixedSize(QSize(720, 330))
        
        # Constructs the objects responsible to send and receive packets 
        # from PROSAC
        self.requester_controller = Requester(self)
        
        self.is_closed = 0
    
    # Sets a requester in the case it was not instantiated 
    def setRequester(self, r):
        self.requester_controller = r
        self.requester_controller.window_controller = self
    
    # Builds text boxes, labels and buttons required to establish a connection with PROSAC 
    def build_connection_group (self):
        
        self.connect_group = QGroupBox(parent =  self.main_widget)
        self.connect_group.setTitle('Connection')
        self.connect_group.setStyleSheet("QGroupBox { border: 1px solid gray; border-radius: 3px;  margin-top: 0.5em; } QGroupBox::title {  subcontrol-origin: margin; left: 10px; padding: 0 3px 0 3px;}")
        
        self.connect_label = QLabel("IP Address")
        self.connect_line = QLineEdit("10.0.6.65")
        self.connect_line.setFixedWidth(100)
        
        self.connect_button = QPushButton("Connect")
        self.connect_button.clicked.connect(self.connection_handler)
        
        self.port_label = QLabel("Port")
        self.port_line = QLineEdit("4000")
        self.port_line.setFixedWidth(50)
        self.led_connected = QLed(parent = self.main_widget)
        self.led_connected.turnOff()
        
        self.connect_layout = QHBoxLayout()
        
        self.connect_layout.addWidget(self.connect_label)
        self.connect_layout.addWidget(self.connect_line)
        self.connect_layout.addWidget(self.port_label)
        self.connect_layout.addWidget(self.port_line)
        self.connect_layout.addWidget(self.led_connected)
        self.connect_layout.addWidget(self.connect_button)
        
        self.connect_group.setLayout(self.connect_layout)
    
    # Builds components to show last received and sent messages
    def build_log_group (self): 
        
        self.log_group = QGroupBox(parent =  self.main_widget)
        self.log_group.setTitle('Messages')
        self.log_group.setStyleSheet("QGroupBox { border: 1px solid gray; border-radius: 3px;  margin-top: 0.5em; } QGroupBox::title {  subcontrol-origin: margin; left: 10px; padding: 0 3px 0 3px;}")
        
        self.label_sent = QLabel("Last packet sent: ")
        self.label_received = QLabel("Last packet received: ")
        
        self.log_layout = QVBoxLayout()
        self.log_layout.addWidget(self.label_received)
        self.log_layout.addWidget(self.label_sent)
        
        self.log_group.setLayout(self.log_layout)
    
    # Adds as many buttons as the number of connected power supplies.
    def build_buttons_group (self):
        
        self.sources_group = QGroupBox(parent =  self.main_widget)
        self.sources_group.setTitle('Power supply')
        self.sources_group.setStyleSheet("QGroupBox { border: 1px solid gray; border-radius: 3px;  margin-top: 0.5em; } QGroupBox::title {  subcontrol-origin: margin; left: 10px; padding: 0 3px 0 3px;}")
    
        self.sources_layout = QHBoxLayout()
    
        for button in self.board_buttons_list:
            self.sources_layout.addWidget(button)
            
        self.sources_group.setLayout(self.sources_layout)
        
    # Builds all command buttons.
    # 0x00 stands for a normal reading command, 0x01, a ADJUST command, 0x05, a acknowledge message,
    # 0xE0 is used to ENABLE CYCLE and 0xE1, to abort it.
    def build_commands_group (self):
        
        self.commands_group = QGroupBox(parent =  self.main_widget)
        self.commands_group.setTitle('Commands')
        self.commands_group.setStyleSheet("QGroupBox { border: 1px solid gray; border-radius: 3px;  margin-top: 0.5em; } QGroupBox::title {  subcontrol-origin: margin; left: 10px; padding: 0 3px 0 3px;}")
    
        self.commands_layout = QHBoxLayout()
    
        self.button_normal = QPushButton("0x00")
        self.commands_layout.addWidget(self.button_normal)
        self.button_normal.setEnabled(False)
        # Connects clicked signal to the handler
        self.button_normal.clicked.connect(self.normal_handler)
        
        self.button_adjust = QPushButton("0x01")
        self.commands_layout.addWidget(self.button_adjust)
        self.button_adjust.setEnabled(False)
        self.button_adjust.clicked.connect(self.adjust_handler)
        
        self.button_confirm = QPushButton("0x05")
        self.commands_layout.addWidget(self.button_confirm)
        self.button_confirm.setEnabled(False)
        self.button_confirm.clicked.connect(self.confirm_handler)
        
        self.button_cycle_enable = QPushButton("0xE0")
        self.commands_layout.addWidget(self.button_cycle_enable)
        self.button_cycle_enable.setEnabled(False)
        self.button_cycle_enable.clicked.connect(self.cycle_enable_handler)
        
        self.button_cycle_abort = QPushButton("0xE1")
        self.commands_layout.addWidget(self.button_cycle_abort)
        self.button_cycle_abort.setEnabled(False)
        self.button_cycle_abort.clicked.connect(self.cycle_abort_handler)
        
        self.commands_group.setLayout(self.commands_layout)
    
    # Returns how many power suppliers are connected
    def get_boards_count(self):
        
        return len(self.board_source_windows_list)
    
    # Returns how many power suppliers are connected
    def get_boards_list(self):
        
        return self.board_source_windows_list
    
    # Resets lists
    def build_source_windows(self):
        
        # board_source_windows_list is a list object which contains all references
        # to the SourceWindow objects. board_buttons_list contains the references to
        # the buttons which open the respective window 
        
        self.board_source_windows_list = list()
        self.board_buttons_list = list()
        
        # Support for a pre-built configuration (command IDENT not necessary)
        #for i in board_list:
        #    self.board_source_windows_list.append(SourceWindow(parent = self, board = i))
        #    button = QPushButton(i.getTitle())
        #    button.clicked.connect (self.source_handler)
        #    self.board_buttons_list.append(button)

    # Adds a button to the board_buttons_list and associates it to a SourceWindow object
    def add_board_source_group(self, id):
               
        self.board_source_windows_list.append(SourceWindow(parent = self, board = Board(title='Board #%d' % id, color = QColor(randint(0, 255), randint(0, 255), randint(0, 255) ), order = id)))
        button = QPushButton('Board #%d' % id)
        button.clicked.connect (self.source_handler)
        self.board_buttons_list.append(button)
        
        self.sources_layout.addWidget(button)
    
    # Resets and deletes board_source_windows_list and board_buttons_list
    def clear_source_group (self):
        
        # Resets entire source group. Clears buttons and layout
        for b in self.board_buttons_list:
            b.deleteLater()
            #self.sources_layout.removeWidget(b)
            
        self.board_buttons_list = []  # Clears button list
        
        for b in self.board_source_windows_list:
            
            # if useCanvas is True, close canvas. Such operation must be made explicitly,
            # considering that matplotlib does not handle it.
            if b.useCanvas:
                b.canvas.close()
                     
            b.close()
        
        self.board_source_windows_list = []
        
    # Updates buttons in the case of a disconnection
    def set_disconnected_state (self):
               
        self.sources_group.setVisible(False)
        
        self.button_normal.setEnabled(False)
        self.button_adjust.setEnabled(False)
        self.button_cycle_enable.setEnabled(False)
        self.button_cycle_abort.setEnabled(False)
        self.button_confirm.setEnabled(False)
        
        self.led_connected.turnOff()
                
        self.connect_button.setText("Connect")
    
    # Updates buttons in the case of a connection    
    def set_connected_state (self):
        
        self.sources_group.setVisible(True)
        
        self.button_normal.setEnabled(True)
        self.button_adjust.setEnabled(True)
        self.button_cycle_enable.setEnabled(True)
        self.button_cycle_abort.setEnabled(False)
        self.button_confirm.setEnabled(True)
        
        self.led_connected.turnOn()
        
        self.connect_button.setText("Disconnect")
        
    def set_requester_controller(self, requester):
        
        self.requester_controller = requester

    ####### HANDLERS section #######
    
    # Handles a connection / disconnection request
    def connection_handler(self):
        
        if not self.requester_controller.isConnected :
        
            port_n = int(self.port_line.text())
            
            self.led_connected.turnAlmostOn()
            
            self.statusBar().clearMessage()
            
            self.statusBar().showMessage('Trying to connect to %s through port %d' % (self.connect_line.text(), port_n))
            
            self.update()
            
            # Tries to connect to the PROSAC
            connected, message = self.requester_controller.connect(ip_address = self.connect_line.text(), port = port_n)
            
            self.statusBar().showMessage('%s' % message)
                    
            if connected:
                
                self.set_connected_state()
                                            
                # Request information about how many boards are connected (IDENT command)
                self.requester_controller.execute_command(0x02)
                self.requester_controller.execute_command(0x03)
                
                
                self.requester_controller.can_ask = True
            
            # If connection request was not successful, reset all lists 
            # and boards
            else: 
                
                self.requester_controller.isConnected = False
                self.clear_source_group()
                self.set_disconnected_state()
        
        # Disconnection request: reset all lists and updates buttons
        else :
            
            self.requester_controller.disconnect()
            
            self.clear_source_group()
            
            self.set_disconnected_state()
                        
            self.statusBar().showMessage('Client disconnected.')            
            
    # Sends a 0x00 command
    def normal_handler (self):
    
        self.requester_controller.execute_command(0x00)
    
    # Sends a 0x01 command
    def adjust_handler(self):
        
        self.requester_controller.execute_command(0x01)
    
    # Sends a 0x05 command
    def confirm_handler(self):
        
        self.requester_controller.execute_command(0x05)

    # Sends a 0xE0 command and updates states of buttons
    def cycle_enable_handler(self):
        
        self.requester_controller.execute_command(0xE0)
        
        self.button_normal.setEnabled(False)
        self.button_adjust.setEnabled(False)
        self.button_cycle_enable.setEnabled(False)
        self.button_cycle_abort.setEnabled(True)
        self.button_confirm.setEnabled(False)
    
    # Sends a 0xE1 command and updates states of buttons
    def cycle_abort_handler(self):
        
        self.requester_controller.execute_command(0xE1)
        
        self.button_normal.setEnabled(True)
        self.button_adjust.setEnabled(True)
        self.button_cycle_enable.setEnabled(True)
        self.button_cycle_abort.setEnabled(False)
        self.button_confirm.setEnabled(True)
        
    # Handles push-button event and shows the associated SourceWindow object 
    def source_handler(self):
        
        sender = self.sender()
        j = 0
        
        for i in self.board_buttons_list:
            
            if sender == i:
                break
            
            j = j + 1
            
        self.board_source_windows_list[j].show()
        
        self.statusBar().showMessage(sender.text() + ' was pressed')
    
    # Update flag in order to reader thread to acknowledge it 
    def closeEvent(self, *args, **kwargs):
        
        self.is_closed = 1
        
        return QMainWindow.closeEvent(self, *args, **kwargs)
    
    # Updates all SourceWindow objects 
    def paintEvent(self, *args, **kwargs):
        
        for b in self.board_source_windows_list:
            b.update()
        
        return QMainWindow.paintEvent(self, *args, **kwargs)