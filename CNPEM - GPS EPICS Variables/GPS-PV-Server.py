#!/usr/bin/python
# -*- coding: utf-8 -*-

'''
    This module contains all necessary classes and methods to communicate with a
    NTP server connected to the network. The most important modules it imports are 
    pcaspy and ntplib. The first one provides the super classes that will, in fact,
    allow users request PVs across the network. The second one provides methods to 
    communicate with NTP servers, according to its protocol. Refer NTP protocol page 
    for further info.  
    
    TO DO: implement communication with GPS directly. Use libgps provided by GPSD main page.
    
    @author: Gustavo Ciotto Pinton
'''

from pcaspy import Driver, Alarm, Severity, SimpleServer
import os
import time
import sys
import ntplib
import datetime
import socket

Months = ['Jan.', 'Feb.', "Mar.", "Apr.", "May", "June", "July", "Aug." , "Sept", "Oct." , "Nov.", "Dec."]

# PVs provided by a NTP server instance
PVs = {
    "GPS:OnOff" : { "type" : "enum"},  
       
    "GPS:Address" : { "type" : "string"},
    
    "GPS:Day" : { "type" : "int"},
    "GPS:Year" : { "type" : "int" },
    "GPS:Month" : { "type" : "string"},
       
    "GPS:Hour" : { "type" : "int", "unit" : "h" },
    "GPS:Minute" : { "type" : "int", "unit" : "min" },
    "GPS:Second" : { "type" : "int", "unit" : "s", "scan" : 1},
    "GPS:Millisecond" : { "type" : "int", "unit" : "ms"},
    
    "GPS:SERVER:Stratum" : { "type" : "int"},
    "GPS:SERVER:Leap" : { "type" : "int"},
    "GPS:SERVER:Version" : { "type" : "int"},
    "GPS:SERVER:RoundTrip" : { "type" : "int", "unit" : "ms"},
    "GPS:SERVER:Reference" : { "type" : "string"}    
}

# In practice, only logging operations use this function
def time_string():
    return(time.strftime("%d/%m/%Y, %H:%M:%S - ", time.localtime()))

# This class inherits from 
class NTPDriver(Driver):

    def __init__(self):

        Driver.__init__(self)
        
        self.ntpclient = ntplib.NTPClient()
        
        # Change here to modify ip address of the server to be requested
        self.setParam("GPS:Address", "10.0.6.60")
        
        #self.setParam("GPS:Address", socket.gethostbyname(socket.gethostname()))
        
        # Log file initialization
        self.log_file = open(os.path.dirname(os.path.realpath(sys.argv[0])) + "/GPS-PV-Server.log", "a")
        self.log_file.write(time_string() + "GPS PVs Server initialized.\n")
        self.log_file.flush()

    # Overrides superclass read () method and implements the communication with 
    # the NTP server
    def read(self, reason):
        
        try:
            
            server_answer = self.ntpclient.request('10.0.6.60', version = 4);
        
        except: 
            
            self.setParam("GPS:OnOff", 0)
            
        else:                 
            
            self.setParam("GPS:OnOff", 1)
            
            server_date = datetime.datetime.fromtimestamp(server_answer.tx_time)
            
            self.setParam("GPS:Day", server_date.day)
            self.setParam("GPS:Month", Months[server_date.month - 1])
            self.setParam("GPS:Year", server_date.year) 
            
            self.setParam("GPS:Hour", server_date.hour)
            self.setParam("GPS:Minute", server_date.minute)
            self.setParam("GPS:Second", server_date.second)
            self.setParam("GPS:Millisecond", server_date.microsecond/1000) 
                  
            byte_a = (server_answer.ref_id & 0xFF000000) >> 24
            byte_b = (server_answer.ref_id & 0xFF0000) >> 16
            byte_c = (server_answer.ref_id & 0xFF00) >> 8
            byte_d = (server_answer.ref_id & 0xFF)
            
            if byte_a == 127 and byte_b == 127 and byte_c == 1 and byte_d == 0:    
                aux = "LOCL" 
            else:
                aux = chr(byte_a) + chr(byte_b) + chr(byte_c) + chr(byte_d)    
            
            self.setParam("GPS:SERVER:Stratum", server_answer.stratum)
            self.setParam("GPS:SERVER:Leap", server_answer.leap)
            self.setParam("GPS:SERVER:Version", server_answer.version)
            self.setParam("GPS:SERVER:Reference", aux)
            self.setParam("GPS:SERVER:RoundTrip", server_answer.root_delay)
                    
        self.updatePVs()
        
        return self.getParam(reason)

# Main function. Instantiates a new server and a new driver.
if __name__ == "__main__":

    CAserver = SimpleServer()
    CAserver.createPV("", PVs)
    driver = NTPDriver()

    # Processes request each 100 ms
    while (True):
        CAserver.process(0.1)
