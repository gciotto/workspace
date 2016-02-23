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
        self.variavel = QLineEdit()

        #=======================================================================
        # self.year = QLineEdit();
        # self.Lyear = QLabel("Ano");
        # 
        # self.month = QLineEdit();
        # self.Lmonth = QLabel("Mes");
        # 
        # self.day = QLineEdit();
        # self.Lday = QLabel("Dia");
        #=======================================================================
        
        self.calendar = QCalendarWidget()
        
        
        self.hours = QLineEdit();
        self.Lhours = QLabel("Horas");
        
        self.minutes = QLineEdit();
        self.Lminutes = QLabel("Minutos");
        
        self.seconds = QLineEdit();
        self.Lseconds = QLabel("Segundos");
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
        self.layout2.addWidget(self.combovariables,0, 1,1,2)
        #=======================================================================
        # self.layout2.addWidget(self.Lday,1, 0)
        # self.layout2.addWidget(self.day,2, 0)
        # self.layout2.addWidget(self.Lmonth,1,1)
        # self.layout2.addWidget(self.month,2,1)
        # self.layout2.addWidget(self.Lyear,1, 2)
        # self.layout2.addWidget(self.year,2, 2)
        #=======================================================================
        
        self.layout2.addWidget(self.calendar,1, 0,2,3)
        
        self.layout2.addWidget(self.Lhours,3, 0)
        self.layout2.addWidget(self.hours,4, 0)
        self.layout2.addWidget(self.Lminutes,3, 1)
        self.layout2.addWidget(self.minutes,4, 1)      
        self.layout2.addWidget(self.Lseconds,3, 2)
        self.layout2.addWidget(self.seconds,4, 2)
        self.layout2.addWidget(self.plotButton, 5, 0)
        self.widget2.setLayout(self.layout2)
        
        layout = QGridLayout(self.mainWidget)
        
        layout.addWidget(self.widget2,0,0,1,3)
        layout.addWidget(self.plotview.widget,0,3,1,1)
        
        self.setLayout(layout)

     
    def update(self):
        print "Ok"
        
        variavel = str(self.combovariables.currentText())
        day = str(self.calendar.selectedDate().day())
        month = str(self.calendar.selectedDate().month())
        year =  str(self.calendar.selectedDate().year())
        hours =  self.hours.text()
        minutes =  self.minutes.text()
        seconds =  self.seconds.text()
        
        data_retrieval_url = "http://localhost/lnls-archiver/data/getData.json?pv="
        pv_name = variavel.replace(':', '%3A')
        initial_date = "&to=" + year + '-' + month + '-' + day +"T"+hours+":"+minutes+":"+seconds+".000Z".replace(':', '%3A')
        
        url_json = data_retrieval_url + pv_name + initial_date
        
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