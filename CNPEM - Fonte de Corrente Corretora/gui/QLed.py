'''
    QLed component derives from QWidget and represents a status LED. It overrides the paintEvent 
    method from its superclass in order to draw a small circle, which color depends on its status.  
    
    Created on Jun 21, 2016
    
    @author: gciotto
'''

from PyQt4.QtGui import QWidget, QPainter, QColor
from PyQt4.QtCore import QRectF

class QLed (QWidget):
    
    # 'Radius' represents the radius of the circle which will be drawn and 'status', the behavior 
    # (color) of this led. By convention, status 1 stands for the GREEN color, 0 for RED or
    # YELLOW otherwise.   
    def __init__(self, parent = None, status = 'on', radius = 5, d_color = {'on': QColor(0,255,0) ,'off' : QColor(255,0,0), 'waiting': QColor(255,255,0)}):
        
        QWidget.__init__(self, parent)
        self.status = status
        self.radius = radius
        self.d_color = d_color
    
    # Changes circle's color to GREEN
    def turnOn(self):
        
        self.status = 'on'
    
    def set_status(self, s):
        
        self.status = s
    
    # Changes circle's color to YELLOW
    def turnAlmostOn(self):
        
        self.status = 'waiting'
    
    # Changes circle's color to RED
    def turnOff(self):
        
        self.status = 'off'
    
    # Overrides superclass' paintEvent() method
    def paintEvent(self, event):
        
        painter = QPainter()
        painter.begin(self)
        
        # Selects a color from the dictionary according to the status
        painter.setBrush(self.d_color[self.status])
        
        # Places the circle in the middle of the drawing area
        rect_aux = QRectF(self.width()/2 - 5, self.height()/2 - 5 , 2 * self.radius, 2 * self.radius)
        
        # Paints circle
        painter.drawEllipse(rect_aux)
        painter.end()
        
        return QWidget.paintEvent(self, event)
        
    
    