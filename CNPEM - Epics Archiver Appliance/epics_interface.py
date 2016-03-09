import sys
from numpy import *
from PySide.QtCore import *
from PySide.QtGui import *


from datetime import datetime
import time
import pytz # $ pip install pytz
from tzlocal import get_localzone

app = QApplication(sys.argv)

from traits.etsconfig.etsconfig import ETSConfig
ETSConfig.toolkit = "qt4"
from enable.api import Window
from chaco.api import ArrayPlotData, Plot, PlotAxis
from chaco.scales.api import CalendarScaleSystem
from chaco.scales_tick_generator import ScalesTickGenerator

import urllib2
import json

class Viewer(QMainWindow):
    
    def __init__(self):
        QMainWindow.__init__(self)
        
        self.create_components()
                
        data = self.json_requester.json_request_variables('*')
        
        for i in range(data.__len__()):
            self.combovariables.insertItem(i, data[i]['pvNameOnly'])
        
        self.put_components()
    
    def create_components(self):
        
        self.mainWidget = QWidget(self) # dummy widget to contain layout manager
        self.plotview = Plotter(self)
        self.setCentralWidget(self.mainWidget)
        self.json_requester = JsonRequester("http://localhost/lnls-archiver", "http://localhost:11995/mgmt/")

        self.combovariables = QComboBox()
        
        self.titulo = QLabel("Variavel");
        
        self.calendar_from = QCalendarWidget()
        self.calendar_to = QCalendarWidget()
        
        self.hours_from = QLineEdit();
        self.hours_from.setMaxLength(2)
        self.Lhours_from = QLabel("Horas");
        
        self.minutes_from = QLineEdit();
        self.minutes_from.setMaxLength(2)
        self.Lminutes_from = QLabel("Minutos");
        
        self.seconds_from = QLineEdit();
        self.seconds_from.setMaxLength(2)
        self.Lseconds_from = QLabel("Segundos");
        
        
        self.hours_to = QLineEdit();
        self.Lhours_to = QLabel("Horas");
        
        self.minutes_to = QLineEdit();
        self.Lminutes_to = QLabel("Minutos");
        
        self.seconds_to = QLineEdit();
        self.Lseconds_to = QLabel("Segundos");
        
        self.plotButton = QPushButton("Plot")
        self.plotButton.clicked.connect(self.update) 
    
        
    def put_components(self):
        
        self.widget2 = QWidget(self)
        
        self.layout2 = QGridLayout(self.widget2)
        self.layout2.addWidget(self.titulo,0, 0,1,1)
        self.layout2.addWidget(self.combovariables,0, 1,1,3)
        
        
        self.layout2.addWidget(self.calendar_from,1, 0,2,2)
        self.layout2.addWidget(self.Lhours_from,3, 0)
        self.layout2.addWidget(self.hours_from,4, 0)
        self.layout2.addWidget(self.Lminutes_from,3, 1)
        self.layout2.addWidget(self.minutes_from,4, 1)      
        self.layout2.addWidget(self.Lseconds_from,3, 2)
        self.layout2.addWidget(self.seconds_from,4, 2)
        
        self.layout2.addWidget(self.calendar_to,1, 2,2,2)
        self.layout2.addWidget(self.Lhours_to,5, 0)
        self.layout2.addWidget(self.hours_to,6, 0)
        self.layout2.addWidget(self.Lminutes_to,5, 1)
        self.layout2.addWidget(self.minutes_to,6, 1)      
        self.layout2.addWidget(self.Lseconds_to,5, 2)
        self.layout2.addWidget(self.seconds_to,6, 2)
        
        
        self.layout2.addWidget(self.plotButton, 7, 0)
        self.widget2.setLayout(self.layout2)
        
        self.layout = QGridLayout(self.mainWidget)
        
        self.layout.addWidget(self.widget2,0,0,1,3)
        self.layout.addWidget(self.plotview.widget,0,3,1,1)
        
        self.setLayout(self.layout)

     
    def update(self):
        print "Ok"
        
        variavel = str(self.combovariables.currentText())   
        
        to_date = time.strptime("%d %d %d %s %s %s" % (self.calendar_to.selectedDate().year(),
                                                         self.calendar_to.selectedDate().month(), 
                                                         self.calendar_to.selectedDate().day(), 
                                                         self.hours_to.text(), self.minutes_to.text(), 
                                                         self.seconds_to.text()),
                                     "%Y %m %d %H %M %S")
        
        
        from_date = time.strptime("%d %d %d %s %s %s" % (self.calendar_from.selectedDate().year(),
                                                         self.calendar_from.selectedDate().month(), 
                                                         self.calendar_from.selectedDate().day(), 
                                                         self.hours_from.text(), self.minutes_from.text(), 
                                                         self.seconds_from.text()),
                                     "%Y %m %d %H %M %S")
       
        secs, vals = self.json_requester.json_request_data(variavel, 
                                                           from_date, 
                                                           to_date)
        self.plotview.update_data(secs, vals)
            

class Plotter():
    def __init__(self, parent):
        self.plotdata = ArrayPlotData(x=array([]),  y=array([]))
        self.window = self.create_plot(parent)
        
        self.widget = self.window.control
    

    def update_data(self, x, y):
        self.plotdata.set_data("x", x)
        self.plotdata.set_data("y", y)
        

    def create_plot(self, parent):
        plot = Plot(self.plotdata, padding=50, border_visible=True)
        plot.plot(("x", "y"), name="data plot", color="green")
        bottom_axis = PlotAxis(plot, orientation="bottom",# mapper=xmapper,
                    tick_generator=ScalesTickGenerator(scale=CalendarScaleSystem()))
        
        plot.index_axis = bottom_axis
        return Window(parent, -1, component=plot)
    

class JsonRequester ():
    
    def __init__(self, data_retrieval_url, mgmt_url):
        self.data_retrieval_url = data_retrieval_url
        self.mgmt_url = mgmt_url
    
    def json_request_variables(self, variables_prefix):
        
        url_json = self.mgmt_url + 'bpl/getPVStatus?pv=' + variables_prefix
        req = urllib2.urlopen(url_json)
        data = json.load(req)
        
        return data
    
    def json_request_data(self, variable, from_date, to_date):    
        
        retrieval_url = self.data_retrieval_url + "/data/getData.json?"
        pv_name = ("pv=" + variable).replace(':', '%3A')
        
        to_date =   ("&to=" + time.strftime("%Y-%m-%dT%H:%M:%S",to_date) + ".000Z").replace(':', '%3A')
        from_date = ("&from=" + time.strftime("%Y-%m-%dT%H:%M:%S",from_date) + ".000Z").replace(':', '%3A')
        
        url_json = retrieval_url + pv_name + from_date + to_date
        
        print url_json
        
        req = urllib2.urlopen(url_json)
        data = json.load(req)
         
        local_tz = get_localzone() 
         
        secs = [x['secs'] + 3*3600 for x in data[0]['data']]
        vals = [x['val'] for x in data[0]['data']]
        
        return secs, vals

if __name__ == "__main__":
    
    plot = Viewer()
    plot.resize(1200, 300)
    
    plot.show()
    sys.exit(app.exec_())