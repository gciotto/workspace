'''

ClientInterface inherits from  QT QMainWindow class and represents a graphical
interface to control all nodes in the network.

Created on 01/08/2016

@author: gciotto
'''
from PyQt4.QtGui import QMainWindow,\
    QGridLayout, QLabel, QPushButton, QTableView, QAbstractItemView, QInputDialog,\
    QTextEdit
from PyQt4.QtCore import *
from PyQt4.Qt import QWidget, QLineEdit, QColor, QBrush
from Control_Node import Control_Node_State, Network_Nodes
from BufferController import Command

class ClientInterface (QMainWindow):
     
    log_signal = pyqtSignal(str, name="logChanged")
     
    def __init__(self, parent = None, buf_controller = None):
        
        QMainWindow.__init__(self, parent)
        
        self.should_monitor = False
        self.still_on = True
        
        
        self.buffer_controller = buf_controller
         
        self.widget = QWidget()
        
        self.widgetbox = QGridLayout()
        self.widgetbox.setSpacing(10) 
        
        self.start_monitoring = QPushButton("Start", parent = self)
        self.start_monitoring.clicked.connect(self.start_monitoring_nodes)
        
        self.reboot_nodes = QPushButton("Reboot Nodes", parent = self)
        self.reboot_nodes.setEnabled(False)
        self.reboot_nodes.clicked.connect(self.reboot_selected_nodes)
        
        self.send_cmd = QPushButton("Send command", parent = self)
        self.send_cmd.setEnabled(False)
        self.send_cmd.clicked.connect(self.send_cmd_nodes)
        
        self.node_list = Network_Nodes.nodes
                
        self.table_model = TableModel(self, self.node_list)

        self.table_view = QTableView(self)
        self.table_view.setSelectionBehavior(QAbstractItemView.SelectRows)
        self.table_view.setModel(self.table_model)
        self.table_view.verticalHeader().hide()
        
        self.log_label = QLabel("Command log:", parent = self)
        self.log_label.setMaximumHeight(20)
        self.log_text = QTextEdit(parent = self)
        self.log_text.setMaximumHeight(150)
        self.clear_log = QPushButton("Clear log", parent = self)
        self.clear_log.clicked.connect(self.clear_log_text)
        
        # Since that log_text should be changed in other thread, we must use signal
        # to communicate between them
        self.log_signal.connect(self.append_log)
        
        self.label_delay = QLabel("Refresh delay (s): ", self)
        self.label_delay.setMinimumWidth(200)
        self.refresh_delay = 5
        self.time_edit = QLineEdit("5", self)
        self.time_edit.setMaximumWidth(60)
        self.time_edit.editingFinished.connect(self.update_refresh_delay)
        
        self.widgetbox.addWidget(self.table_view, 0,0, 5, 3)
        self.widgetbox.addWidget(self.start_monitoring, 0, 3, 1, 2)
        self.widgetbox.addWidget(self.reboot_nodes, 1, 3, 1, 2)
        self.widgetbox.addWidget(self.label_delay, 2, 3, 1, 2)
        self.widgetbox.addWidget(self.time_edit, 2, 4, 1, 1)
        self.widgetbox.addWidget(self.send_cmd, 3, 3, 1, 2)
        
        self.widgetbox.addWidget(self.log_label, 5, 0, 1, 2)
        self.widgetbox.addWidget(self.clear_log, 5, 4, 1, 1)
        self.widgetbox.addWidget(self.log_text, 6, 0, 1, 5)
            
        self.widget.setLayout(self.widgetbox)
        
        self.setWindowTitle("PROSAC Daemon Client")  
        self.setCentralWidget(self.widget)
        
        self.setFixedSize(QSize(530, 800))
    
    def clear_log_text (self):
        
        self.log_text.clear()
    
    def append_log (self, str_text):
        
        self.log_text.append(str_text)
    
    def send_cmd_nodes(self): 
        
        _cmd, ok = QInputDialog.getText(self, "Type in the command",\
                "Type in the command which will be executed in selected SBCs:")
        
        if ok:
            
            __selection = self.table_view.selectionModel()
            
            for i in __selection.selectedRows():
            
                __command = Command(type = Command.OTHER, target = Network_Nodes.nodes[i.row()], command = _cmd)
            
                self.buffer_controller.enqueue_command(__command) 
    
    
    def reboot_selected_nodes(self):
        
        __selection = self.table_view.selectionModel()
        
        for i in __selection.selectedRows():
            
            __command = Command(type = Command.REBOOT, target = Network_Nodes.nodes[i.row()])
            
            self.buffer_controller.enqueue_command(__command) 
    
    def update_refresh_delay (self):
        
        self.refresh_delay = float (self.time_edit.text())
        
    def start_monitoring_nodes(self):
        
        if not self.should_monitor:
            self.reboot_nodes.setEnabled(True)
            self.send_cmd.setEnabled(True)
            self.start_monitoring.setText("Stop")
            
        else:
            self.reboot_nodes.setEnabled(False)
            self.send_cmd.setEnabled(False)
            self.start_monitoring.setText("Start")
        
        self.should_monitor = not self.should_monitor
        
    def closeEvent(self, *args, **kwargs):
        
        self.still_on = False
        
        self.buffer_controller.stop_thread = True
        
        return QMainWindow.closeEvent(self, *args, **kwargs)

    def paintEvent(self, *args, **kwargs):
                
        return QMainWindow.paintEvent(self, *args, **kwargs)

# Model to the QTableView widget. It is responsible to fill up the table with the
# corrected data and paint each row according to its state.
class TableModel (QAbstractTableModel):
    
    def __init__ (self, parent = None, node_list = []):
        
        self._data = node_list
        
        QAbstractTableModel.__init__(self, parent)
    
    def rowCount(self, *args, **kwargs):
    
        return len(self._data)
    
    def columnCount(self, *args, **kwargs):
        return 3
    
    def data(self, index, role):
        
        __row = index.row()
        __col = index.column()
        __node = self._data[__row]
        
        if role == Qt.BackgroundRole :
           
            if __node.state == Control_Node_State.DISCONNECTED:
                return QBrush(QColor(229, 85, 94))
            
            if __node.state == Control_Node_State.CONNECTED:
                return QBrush(QColor(92, 255, 130))
            
            return QBrush(QColor(255, 255, 0))
        
        
        if role == Qt.TextAlignmentRole:
            return Qt.AlignCenter | Qt.AlignVCenter;
        
        if role == Qt.DisplayRole:
            
            if __col == 0:
                return QVariant(__node.network_id)
            
            if __col == 1:
                return QVariant(__node.node_id)
            
            return QVariant(Control_Node_State.toString(__node.state))
        
        return QVariant()
    
    def headerData(self, section, orientation, role=Qt.DisplayRole):
        # if role == Qt.TextAlignmentRole:
        #     if orientation == Qt.Horizontal:
        #         return QVariant(int(Qt.AlignLeft|Qt.AlignVCenter))
        #     return QVariant(int(Qt.AlignRight|Qt.AlignVCenter))
        
        if role != Qt.DisplayRole:
            return QVariant()
        
        if orientation == Qt.Horizontal:
            if section == 0:
                return QVariant("Rede")
            
            elif section == 1:
                return QVariant("No")
            
            elif section == 2:
                return QVariant("Status")
            
        return QVariant()
        