'''
    Board class contains all necessary information to describe a generic board. In this case,
    only PROCON boards will be supported.

    Created on Jun 22, 2016
    
    @author: gciotto
'''

from PyQt4.QtGui import QColor 

class Board:
    
    def __init__ (self, title = "Generic board", color = QColor(128, 0, 0), order = 0):
        
        self.title = title
        self.color = color
        self.order = order
    
    def getOrder(self):
        
        return self.order
    
    def getColor(self):
        
        return self.color
    
    def getColorAsString(self):
        
        return self.color.name()
    
    def getTitle(self):
        
        return self.title
    
# Some generic boards, as examples
none = Board()
LCH02 = Board("LCH02", QColor(0, 128, 0), 1)
LCV02 = Board("LCV02", QColor(0, 0, 128), 2) 
LCH03 = Board("LCH03", QColor(0, 128, 128), 3)
LCV03 = Board("LCV03", QColor(128, 0, 128), 4)
LCH04 = Board("LCH04", QColor(0, 255, 128), 5)
LCV04 = Board("LCV04", QColor(128, 128, 128), 6)

board_list = [ none, LCH02, LCV02, LCH03, LCV03, LCH04, LCV04]
