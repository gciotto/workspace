import sys
from numpy import *
from PySide.QtCore import *
from PySide.QtGui import *
import datetime
import time

app = QApplication(sys.argv)

from traits.etsconfig.etsconfig import ETSConfig
ETSConfig.toolkit = "qt4"
from enable.api import Window
from chaco.api import ArrayPlotData, Plot

import urllib2
import json

class Viewer(QMainWindow):
    def __init__(self):
        QMainWindow.__init__(self)
        self.mainWidget = QWidget(self) # dummy widget to contain layout manager
        self.plotview = Plotter(self)
        self.setCentralWidget(self.mainWidget)

        self.combovariables = QComboBox()
        
        self.titulo = QLabel("Variavel");

        #=======================================================================
        #self.variavel = QLineEdit()
        
        # self.year = QLineEdit();
        # self.Lyear = QLabel("Ano");
        # 
        # self.month = QLineEdit();
        # self.Lmonth = QLabel("Mes");
        # 
        # self.day = QLineEdit();
        # self.Lday = QLabel("Dia");
        #=======================================================================
        
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
                
        data_retrieval_url = "http://localhost/lnls-archiver/data/getData.json?pv="
        pv_name = "MBTemp2:Channel1".replace(':', '%3A')
        initial_date = "&from=2016-02-22T12:30:00.000Z".replace(':', '%3A')
        
        #2016-02-22T12:30:00.000Z
        
        url_json = data_retrieval_url + pv_name + initial_date
                
        req = urllib2.urlopen(url_json)
        data = json.load(req)
        secs = [datetime.datetime.fromtimestamp(x['secs']) for x in data[0]['data']]
        teste = [float(x.day) + (float(x.hour) + float(x.minute)/100 + float(x.second)/10000)/100 for x in secs]
        
        for i in range(secs.__len__()):
            print "%s - %.6f" % (secs[i], teste[i])    
        
        vals = [x['val'] for x in data[0]['data']]
        self.plotview.update_data(teste, vals)

        url_json = 'http://localhost:11995/mgmt/bpl/getPVStatus?pv=*'
        req = urllib2.urlopen(url_json)
        data = json.load(req)
        
        for i in range(data.__len__()):
            self.combovariables.insertItem(i, data[i]['pvNameOnly'])
        
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
        
        layout = QGridLayout(self.mainWidget)
        
        layout.addWidget(self.widget2,0,0,1,3)
        layout.addWidget(self.plotview.widget,0,3,1,1)
        
        self.setLayout(layout)

     
    def update(self):
        print "Ok"
        
        variavel = str(self.combovariables.currentText())
        day_from = "%02d" % self.calendar_from.selectedDate().day()
        month_from = "%02d" %  self.calendar_from.selectedDate().month()
        year_from =  "%02d" %  self.calendar_from.selectedDate().year()
        hours_from =  self.hours_from.text()
        minutes_from =  self.minutes_from.text()
        seconds_from =  self.seconds_from.text()
        
        day_to = "%02d" % self.calendar_to.selectedDate().day()
        month_to = "%02d" %  self.calendar_to.selectedDate().month()
        year_to =  "%02d" %  self.calendar_to.selectedDate().year()
        hours_to =  self.hours_to.text()
        minutes_to =  self.minutes_to.text()
        seconds_to =  self.seconds_to.text()
        
        data_retrieval_url = "http://localhost/lnls-archiver/data/getData.json?pv="
        pv_name = variavel.replace(':', '%3A')
        to_date = "&to=" + year_to + '-' + month_to + '-' + day_to +"T"+hours_to+":"+minutes_to+":"+seconds_to+".000Z".replace(':', '%3A')
        from_date = "&from=" + year_from + '-' + month_from + '-' + day_from +"T"+hours_from+":"+minutes_from+":"+seconds_from+".000Z".replace(':', '%3A')
        
        
        url_json = data_retrieval_url + pv_name + from_date + to_date
        
        print url_json
        
        req = urllib2.urlopen(url_json)
        data = json.load(req)
        secs = [datetime.datetime.fromtimestamp(x['secs']) for x in data[0]['data']]
        teste = [float(x.day) + (float(x.hour) + float(x.minute)/100 + float(x.second)/10000)/100 for x in secs]
        vals = [x['val'] for x in data[0]['data']]
        self.plotview.update_data(teste, vals)
            

class Plotter():
    def __init__(self, parent):
        self.plotdata = ArrayPlotData(x=array([]),  y=array([]))
        self.window = self.create_plot(parent)
        
        self.widget = self.window.control
    

    def update_data(self, x, y):
        self.plotdata.set_data("x", x)
        self.plotdata.set_data("y", y)
        

    def create_plot(self, parent):
        plot = Plot(self.plotdata, padding=35, border_visible=True)
        plot.plot(("x", "y"), name="data plot", color="green")
        return Window(parent, -1, component=plot)


if __name__ == "__main__":
    
    plot = Viewer()
    plot.resize(1200, 300)
    
    plot.show()
    sys.exit(app.exec_())