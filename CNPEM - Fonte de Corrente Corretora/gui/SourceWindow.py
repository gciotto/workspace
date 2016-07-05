'''
    Created on Jun 21, 2016
    
    SourceWindow class inherits from QMainWindow and contains all components which reflect
    the state of a current source. This window is composed by 4 group boxes. 
    
    Dependencies : python-qt4, install with 'sudo apt-get install python-qt4'
    
    @author: gciotto
'''

from PyQt4.QtGui import QMainWindow, QHBoxLayout, QGroupBox,\
    QGridLayout, QLabel, QPushButton, QCheckBox, QDoubleValidator
from PyQt4.QtCore import *
from QLed import QLed
from PyQt4.Qt import QWidget, QLineEdit
from util.Board import none

# Canvas modules
from matplotlib.backends.backend_qt4agg import FigureCanvasQTAgg as FigureCanvas
import matplotlib.pyplot as plt
import matplotlib

import numpy
import datetime
import threading

class SourceWindow (QMainWindow):
    
    # Constructs a new SourceWindow to control power supply     
    def __init__(self, parent = None, board = none):
        
        QMainWindow.__init__(self, parent)
        
        ### According to INTRACONT page, a LOCON board must always SEND 
        #   5 bytes: - priority (useless)
        #            - cycleNumber (0x80, if it's not cycling)
        #            - most significant byte of the analog output
        #            - least significant byte of the analog output
        #            - digital output
        #  It also READS 5 bytes, which have the same meanings, but are 
        #  used as inputs.
        self.widget = QWidget()
        
        self.title_label = QLabel(board.getTitle())
        self.title_label.setStyleSheet("QLabel { font-size : 24px; background-color : " + board.getColorAsString() + " ;  color : white; border-radius: 3px;}");
        self.title_label.setAlignment(Qt.AlignCenter)
        
        # Determine if canvas should be used. If False, the canvas is not built and, consequently, not drawn.
        self.useCanvas = True
        
        if self.useCanvas:
            self.build_canvas()
        
        # Constructs all groups of components
        self.build_status_group()
        self.build_current_group()
        self.build_cycle_group()
        self.build_on_off_group()
                
        self.build_layout()        
        
        self.widget.setLayout(self.widgetbox)
        
        self.setWindowTitle(board.getTitle())  
        self.setCentralWidget(self.widget)
        
        # Adjusts window's size depending if canvas will be used.
        if self.useCanvas:
            self.setFixedSize(QSize(500, 460))
            
        else: self.setFixedSize(QSize(500, 260))
        
        self.m_show = threading.Lock()
        
        ### Variables describing board operation  
        self.isOn = False
    
    # Builds canvas components
    def build_canvas(self):
        
        # a figure instance to plot on
        self.figure = plt.figure()
        
        # this is the Canvas Widget that displays the `figure`
        # it takes the `figure` instance as a parameter to __init__
        self.canvas = FigureCanvas(self.figure)
        
        self.axes = self.figure.add_subplot(111)

        #self.axes.set_title('Power Supply Progress')
        self.axes.set_ylabel('Current (A)')
        
        self.axes.grid(True)

        self.axes.set_ylim(-11, 11)
        
        #self.figure.autofmt_xdate()
        #self.axes.xaxis.set_major_formatter(matplotlib.dates.DateFormatter('%H:%M:%S'))
        self.axes.xaxis.set_visible(False)

        # Initializes plot with no data
        self.axes_plot = self.axes.plot_date([], [], '-')[0]
        
        self.data_plot_x = []
        self.data_plot_y = []
           
        self.update_x_axis()

    # Adjusts the lowest and biggest points of x-axis. The biggest is defined as the current date and
    # the lowest, as the 50 seconds   
    def update_x_axis (self):
        
        now = datetime.datetime.now()

        x_before = now - datetime.timedelta(seconds = 50)

        dates = matplotlib.dates.date2num([x_before, now])

        # Updates x-axis
        self.axes.set_xlim(dates[0], dates[1]) 
                
    
    # Builds LEDs and button to control and inspect the current state
    def build_on_off_group(self):
        
        self.on_off_led = QLed(parent =  self.widget)
        self.on_off_label = QLabel("Status:")
        self.remote_led = QLed(parent =  self.widget)
        self.remote_label = QLabel("Remote:")
        self.on_off_button = QPushButton('Turn On')
        
        # Connects the signal to the function on_off_handler().
        self.on_off_button.clicked.connect(self.on_off_handler)
        
        self.on_off_group = QGroupBox(parent =  self.widget)
        self.on_off_group.setTitle('On/Off')
        self.on_off_group.setStyleSheet("QGroupBox { border: 1px solid gray; border-radius: 3px;  margin-top: 0.5em; } QGroupBox::title {  subcontrol-origin: margin; left: 10px; padding: 0 3px 0 3px;}")
        
        self.on_off_layout = QGridLayout()
        self.on_off_layout.addWidget(self.on_off_button, 0, 0, 1, 3)
        self.on_off_layout.addWidget(self.on_off_label, 1, 0, 1, 2)
        self.on_off_layout.addWidget(self.on_off_led, 1, 2, 1, 1)
        self.on_off_layout.addWidget(self.remote_label, 2, 0, 1, 2)
        self.on_off_layout.addWidget(self.remote_led, 2, 2, 1, 1)        
        self.on_off_group.setLayout(self.on_off_layout)
    
    # Builds LEDs to inspect the current working modes
    def build_status_group(self):
        
        self.interlock_led = QLed(parent = self.widget)
        self.interlock_led.setToolTip("Interlock")
        
        self.subtension_led = QLed(parent =  self.widget)
        self.subtension_led.setToolTip("Subtensao")
        
        self.temperature_led = QLed(parent =  self.widget)
        self.temperature_led.setToolTip("Temperatura")
        
        self.overcurrent_led = QLed(parent =  self.widget)
        self.overcurrent_led.setToolTip("Sobre-corrente")
        
        self.status_group = QGroupBox(parent =  self.widget)
        self.status_group.setTitle('Status')
        self.status_group.setStyleSheet("QGroupBox { border: 1px solid gray; border-radius: 3px;  margin-top: 0.5em; } QGroupBox::title {  subcontrol-origin: margin; left: 10px; padding: 0 3px 0 3px;}")
            
        self.statusbox = QHBoxLayout()
        self.statusbox.addWidget(self.interlock_led)
        self.statusbox.addWidget(self.subtension_led)
        self.statusbox.addWidget(self.temperature_led)
        self.statusbox.addWidget(self.overcurrent_led)
               
        self.status_group.setLayout(self.statusbox)
    
    # Builds the components to allow the power supplies to cycle. Currently, only curve #7 (code 39) 
    # is available
    def build_cycle_group(self):
        
        self.cycle_group = QGroupBox(parent =  self.widget)
        self.cycle_group.setTitle('Cycling')
        self.cycle_group.setStyleSheet("QGroupBox { border: 1px solid gray; border-radius: 3px;  margin-top: 0.5em; } QGroupBox::title {  subcontrol-origin: margin; left: 10px; padding: 0 3px 0 3px;}")
    
        self.cycle_label = QLabel("Enable cycling")
        self.cycle_combo = QCheckBox()
        self.cycle_combo.stateChanged.connect(self.adjust_text_handler)
        
        # Support for all cycle curves codes
        #self.cycle_combo.addItem("0x80")
        
        #for i in range(8):
        #   self.cycle_combo.addItem("0x%02x" % i)
            
        self.cycle_layout = QHBoxLayout()
        self.cycle_layout.addWidget(self.cycle_label)
        self.cycle_layout.addWidget(self.cycle_combo)
        self.cycle_group.setLayout(self.cycle_layout)
    
    # Builds components that allow to adjust and inspect the current provided by
    # the power supply
    def build_current_group(self):
        
        self.current_group = QGroupBox(parent =  self.widget)
        self.current_group.setTitle('Current (Amp)')
        self.current_group.setStyleSheet("QGroupBox { border: 1px solid gray; border-radius: 3px;  margin-top: 0.5em; } QGroupBox::title {  subcontrol-origin: margin; left: 10px; padding: 0 3px 0 3px;}")
        
        self.adjust_label = QLabel('Adjust: ')
        self.adjust_text = QLineEdit('0.0')
        
        v_adj = QDoubleValidator (-10, 10, 3, self)
        # QDoubleValidator class defines a method which is called after the component has lost focus. We redefine 
        # this method to change the value in case it is out of the range [-10, 10]
        v_adj.fixup = self.fix_adjust
        self.adjust_text.setValidator(v_adj)
        
        # Connects the signal corresponding to the end of changes to the method adjust_text_handler()
        self.adjust_text.editingFinished.connect(self.adjust_text_handler)
        
        self.read_label = QLabel('Input: ')
        self.read_text = QLineEdit('0')
        self.read_text.setReadOnly(True)
        self.read_text.setValidator(QDoubleValidator (-10, 10, 3, self))
        
        # Support for setting canvas visibility
        #self.graph_visible = QCheckBox("Show graph")
        #self.graph_visible.setChecked(False)
        #self.graph_visible.changeEvent = self.change_graph_state_handler
        
        self.current_layout = QGridLayout()
        self.current_layout.addWidget(self.adjust_label, 0, 0, 1, 1)
        self.current_layout.addWidget(self.adjust_text, 0, 1, 1, 1) 
        self.current_layout.addWidget(self.read_label, 1, 0, 1, 1)
        self.current_layout.addWidget(self.read_text, 1, 1, 1, 1)
        #self.current_layout.addWidget(self.graph_visible, 2, 0, 1, 1)        
    
        self.current_group.setLayout(self.current_layout)
    
    # Builds main widget layout and groups all components   
    def build_layout(self):
        
        self.widgetbox = QGridLayout()
        self.widgetbox.setSpacing(10)
        self.widgetbox.addWidget(self.title_label, 0, 0, 1, 4)
        self.widgetbox.addWidget(self.status_group, 1, 0, 1, 2)
        self.widgetbox.addWidget(self.cycle_group, 1, 2, 1, 2)
        self.widgetbox.addWidget(self.current_group, 2, 0, 1, 2)
        self.widgetbox.addWidget(self.on_off_group, 2, 2, 1, 2)
        
        if self.useCanvas:
            self.widgetbox.addWidget(self.canvas, 3, 0, 5, 4)
    
    # Returns the check box state
    def will_cycle(self):
        
        return self.cycle_combo.isChecked()
    
    # Returns text as a floating point number. Considering the use of QDoubleValidator,
    # self.adjust_text.text() will always be a valid number.  
    def get_analog_adjust(self):
        
        return float(self.adjust_text.text())
    
    # Sets text and updates canvas axis, if it is used.
    def set_analog_input(self, a_in):
        
        self.read_text.setText("%.3f" % a_in)
        
        if self.useCanvas:
            
            # mutex must be used because paintEvent() method also uses the data structures
            # which are changed in this function, notably, data_plot_y and data_plot_x
            self.m_show.acquire()
            
            now = datetime.datetime.now()
            
            # Appends a new point in canvas data structures
            self.data_plot_y.append(a_in)
            self.data_plot_x.append(now)
        
            # Verifies if those data structures need to be truncated. As defined earlier, 
            # the lowest point corresponds to 50 seconds in the past. Considering that the 
            # reading thread demands new values with a period of 0.1 seconds, we should have  
            # 500 visible points in canvas.   
            if len (self.data_plot_y) >= 500:
                self.data_plot_y = self.data_plot_y[1:]
                self.data_plot_x = self.data_plot_x[1:]
            
            self.axes_plot.set_ydata(self.data_plot_y)
            self.axes_plot.set_xdata(self.data_plot_x)
            
            # Updates x axis
            self.update_x_axis()
            
            self.m_show.release()
        
        # Schedules a paint event for processing when Qt returns to the main event loop.
        self.update()
        
    # Updates LEDs according to the 8-bit word received as parameter
    def set_digital_inputs(self, d_in):    
        
        # Selects bits corresponding to each state
        
        # Interlock corresponds to bit 0
        l_interlock = d_in & 0x1
        if l_interlock: 
            self.interlock_led.turnOn()
        else:           
            self.interlock_led.turnOff()
        
        # Remote operation corresponds to bit 1
        l_remote = (d_in & 0x2) >> 1
        if l_remote:    
            self.remote_led.turnOn()
        else:           
            self.remote_led.turnOff()
        
        # Sub tension corresponds to bit 2
        l_subtension = (d_in & 0x4) >> 2
        if l_subtension: 
            self.subtension_led.turnOn() 
        else:            
            self.subtension_led.turnOff()
        
        # Temperature corresponds to bit 3
        l_temperature = (d_in & 0x8) >> 3
        if l_temperature: 
            self.temperature_led.turnOn() 
        else:             
            self.temperature_led.turnOff()
        
        # Overcurrent corresponds to bit 5
        l_overcurrent = (d_in & 0x20) >> 5
        if l_overcurrent:
            self.overcurrent_led.turnOn()     
        else:
            self.overcurrent_led.turnOff()
        
        # Bit 7 indicates if power supply is actually on or off
        on_off = d_in & 0x80
        
        if on_off:
            
            self.isOn = True
            self.on_off_button.setText('Turn Off')
            self.on_off_led.turnOn()
            
        else: 
            
            self.isOn = False
            self.on_off_button.setText('Turn On') 
            self.on_off_led.turnOff()
        
        # Schedules a paint event for processing when Qt returns to the main event loop.
        self.update()
    
    # Returns current state
    def is_on(self):
        
        return self.isOn
    
    # Handles the event to turn the power supply on or off
    def on_off_handler (self):
        
        # Sets flag and sends an adjust command
        self.isOn = not self.isOn
        self.parent().adjust_handler()
        
    # Handles changing events in the current QLineEdit component
    def adjust_text_handler (self):
        
        # sends a ADJUST command to PROSAC
        self.parent().adjust_handler()

    # Sets canvas visibility. Currently not is use
    def change_graph_state_handler(self, event):
        
        self.is_graph_visible = not self.is_graph_visible
        
        self.canvas.setVisible(self.is_graph_visible)

    # Validates user's input. It must be in the range [-10, 10]
    def fix_adjust(self, input):
        
        if len(input) is 0:
            self.adjust_text.setText("0.0")
            
        else:
            if float(input) < -10 :
                self.adjust_text.setText("-10.000")
                
            if float(input) > 10 :
                self.adjust_text.setText("10.000")
            
        # self.adjust_text_handler()
    
    # Paints and draws canvas and all others components
    def paintEvent(self, *args, **kwargs):
        
        # If useCanvas is set, acquire mutex and draw canvas. Refer to set_analog_input()
        # for further information
        if self.useCanvas:
            self.m_show.acquire()
            self.canvas.draw()
            self.m_show.release()
           
        return QMainWindow.paintEvent(self, *args, **kwargs)
    