#!/usr/bin/python
# -*- coding: utf-8 -*-

'''
    This module contains all necessary classes and methods to communicate with a
    NTP server connected to the network. The most important modules it imports are 
    pcaspy and ntplib. The first one provides the super classes that will, in fact,
    allow users request PVs across the network. The second one provides methods to 
    communicate with NTP servers, according to its protocol. Refer NTP protocol page 
    for further info.  
        
    @author: Gustavo Ciotto Pinton
'''

from pcaspy import Driver, Alarm, Severity, SimpleServer
import os
import time
import sys
import ntplib
import datetime
import socket
import gps
import threading

_months = ['Jan.', 'Feb.', "Mar.", "Apr.", "May", "June", "July", "Aug." , "Sept", "Oct." , "Nov.", "Dec."]

# PVs provided by a NTP server instance and a GPS receiver
_pvs = {
        
    # NTP - related PVs
    "Cnt:NTP:OnOff" : { "type" : "enum", "enums" : ["Disconnected", "Connected"], "states" : [Severity.MAJOR_ALARM, Severity.NO_ALARM]},  
       
    "Cnt:NTP:Address" : { "type" : "string"},
    
    "Cnt:NTP:Day" : { "type" : "int"},
    "Cnt:NTP:Year" : { "type" : "int" },
    "Cnt:NTP:Month" : { "type" : "string"},
       
    "Cnt:NTP:Hour" : { "type" : "int", "unit" : "h" },
    "Cnt:NTP:Minute" : { "type" : "int", "unit" : "min" },
    "Cnt:NTP:Second" : { "type" : "int", "unit" : "s"},
    "Cnt:NTP:Millisecond" : { "type" : "int", "unit" : "ms"},
    
    "Cnt:NTP:Stratum" : { "type" : "int"},
    "Cnt:NTP:Leap" : { "type" : "int"},
    "Cnt:NTP:Version" : { "type" : "int"},
    "Cnt:NTP:Roundtrip" : { "type" : "float", "prec" : 3, "unit" : "ms"},
    "Cnt:NTP:Reference" : { "type" : "string"},
    #"Cnt:NTP:Offset" : { "type" : "float", "prec" : 3, "unit" : "ms"},
    
    # GPS - related PVs
    
    "Cnt:GPS:Fix" : { "type" : "enum", "enums" : ["NO FIX", "2D FIX", "3D FIX"], "states" : [Severity.MAJOR_ALARM, Severity.NO_ALARM, Severity.NO_ALARM]},
    "Cnt:GPS:Latitude" : { "type" : "float", "prec": 4, "unit" : "º"},
    "Cnt:GPS:Longitude" : { "type" : "float", "prec": 4, "unit" : "º"},
    "Cnt:GPS:Altitude" : { "type" : "float", "prec": 2, "unit" : "m"},
    
    "Cnt:GPS:UTC:Day" : { "type" : "int"},
    "Cnt:GPS:UTC:Month" : { "type" : "string"},
    "Cnt:GPS:UTC:Year" : { "type" : "int"},
    "Cnt:GPS:UTC:Hour" : { "type" : "int", "unit" : "h"},
    "Cnt:GPS:UTC:Minute" : { "type" : "int", "unit" : "min"},
    "Cnt:GPS:UTC:Second" : { "type" : "int", "unit" : "s"},

    
    "Cnt:GPS:Satellites" : { "type" : "int", "count" : 20}
    
}

# In practice, only logging operations use this function
def time_string():
    return(time.strftime("%d/%m/%Y, %H:%M:%S - ", time.localtime()))

# This class inherits from Driver
class NTPDriver(Driver):

    def __init__(self):

        Driver.__init__(self)
        
        self._ntp_scan_thread = threading.Thread(target = self._poll_ntp)
        self._ntp_scan_thread.setDaemon(True)
        
        self._gpsd_scan_thread = threading.Thread(target = self._poll_gpsd)
        self._gpsd_scan_thread.setDaemon(True)
        
        self._gpsd_poll_thread = threading.Thread(target = self._update_gpsd)
        self._gpsd_poll_thread.setDaemon(True)
        
        # Instantiate object which communicates with ntp server 
        self._ntpclient = ntplib.NTPClient()
        
        self._gpsdclient = gps.gps(mode = gps.WATCH_ENABLE)  #starting the stream of info)
        
        # Change here to modify ip address of the server to be requested
        self.setParam("Cnt:NTP:Address", "10.0.6.60")
        self.setParamStatus("Cnt:NTP:Address", Alarm.NO_ALARM, Severity.NO_ALARM)
        
        #self.setParam("Cnt:NTP:Address", socket.gethostbyname(socket.gethostname()))
        
        self.setParam("Cnt:GPS:Fix", 0)
        
        # Log file initialization
        self.log_file = open(os.path.dirname(os.path.realpath(sys.argv[0])) + "/GPS-PV-Server.log", "a")
        self.log_file.write(time_string() + "GPS PVs Server initialized.\n")
        self.log_file.flush()
        
        print 'PV server initialized.'

        self._ntp_scan_thread.start()
        self._gpsd_poll_thread.start()
        self._gpsd_scan_thread.start()

    
    def _poll_ntp(self):
        
        while True:
            
            try:
                
                _server_answer = self._ntpclient.request('10.0.6.60', version = 4);
            
            except: 
                
                self.setParam("Cnt:NTP:OnOff", 0)
                               
                for key in _pvs:
                    if key != "Cnt:NTP:OnOff" and "NTP" in key:
                        self.setParamStatus(key, Alarm.READ_ALARM, Severity.INVALID_ALARM)
                
            else:                 
                
                self.setParam("Cnt:NTP:OnOff", 1)
                
                _server_date = datetime.datetime.fromtimestamp(_server_answer.tx_time)
                
                # Updates day, month and year
                self.setParam("Cnt:NTP:Day", _server_date.day)
                self.setParam("Cnt:NTP:Month", _months[_server_date.month - 1])
                self.setParam("Cnt:NTP:Year", _server_date.year) 
                
                # Updates time
                self.setParam("Cnt:NTP:Hour", _server_date.hour)
                self.setParam("Cnt:NTP:Minute", _server_date.minute)
                self.setParam("Cnt:NTP:Second", _server_date.second)
                self.setParam("Cnt:NTP:Millisecond", _server_date.microsecond/1000) 
                
                _reference_id = ""
                
                # Converts 4 bytes related to the reference source to string 
                for i in range(24, -1, -8):
                    
                    _byte_i = (_server_answer.ref_id & (0xFF << i)) >> i
                    _reference_id = _reference_id + chr(_byte_i)
                    
                if _reference_id == "%c%c%c%c" % (127, 127, 0, 1):
                    _reference_id = "LOCL"
          
                self.setParam("Cnt:NTP:Stratum", _server_answer.stratum)
                self.setParam("Cnt:NTP:Leap", _server_answer.leap)
                self.setParam("Cnt:NTP:Version", _server_answer.version)
                self.setParam("Cnt:NTP:Reference", _reference_id)
                self.setParam("Cnt:NTP:Roundtrip", _server_answer.root_delay * 1000)
                #self.setParam("Cnt:NTP:Offset", _server_answer.offset * 1000)
                
                
                for key in _pvs:
                    if "NTP" in key:
                        self.setParamStatus(key, Alarm.NO_ALARM, Severity.NO_ALARM)
        
            self.updatePVs()
            
            time.sleep(1)            
    
    def _update_gpsd(self):
        
        while True:
            self._gpsdclient.next()
            time.sleep(0.1)
    
    def _poll_gpsd(self):
        
        
        while True:
            
            #self._gpsdclient.next()
            
            _gpsd_status = self._gpsdclient.fix.mode
            
            # print self._gpsdclient.fix.mode
            
            if _gpsd_status != gps.MODE_NO_FIX:
            
                # print 'GPS has a fix'
                
                self.setParam("Cnt:GPS:Fix", self._gpsdclient.fix.mode - 1) 
            
                self.setParam("Cnt:GPS:Latitude", self._gpsdclient.fix.latitude)
                self.setParam("Cnt:GPS:Longitude", self._gpsdclient.fix.longitude)
                self.setParam("Cnt:GPS:Altitude", self._gpsdclient.fix.altitude)
                                
                _utc_as_str =  str(self._gpsdclient.utc)
                
                _date_utc = datetime.datetime.strptime(_utc_as_str, '%Y-%m-%dT%H:%M:%S.000Z')
                
                # print _utc_as_str
                self.setParam("Cnt:GPS:UTC:Day", _date_utc.day)
                self.setParam("Cnt:GPS:UTC:Month", _months[_date_utc.month - 1])
                self.setParam("Cnt:GPS:UTC:Year", _date_utc.year)
                self.setParam("Cnt:GPS:UTC:Hour", _date_utc.hour)
                self.setParam("Cnt:GPS:UTC:Minute", _date_utc.minute)
                self.setParam("Cnt:GPS:UTC:Second", _date_utc.second)
                
                # print str(self._gpsdclient.satellites)
                
                _satellites_codes = []
                
                for s in self._gpsdclient.satellites:
                    if s.used:
                        _satellites_codes.append(s.PRN)
                
                self.setParam("Cnt:GPS:Satellites", _satellites_codes)
                
                for key in _pvs:
                    if "GPS" in key:
                        self.setParamStatus(key, Alarm.NO_ALARM, Severity.NO_ALARM)

                
            else:
                
                for key in _pvs:
                    if key != "Cnt:GPS:Fix" and "GPS" in key:
                        self.setParamStatus(key, Alarm.READ_ALARM, Severity.INVALID_ALARM)
                
            
            
            self.updatePVs()
            time.sleep(1)

    # Overrides superclass read () method to print which variable will be read    
    def read(self, reason):
        
        print 'Reading "%s"' % reason
        
        return Driver.read(self, reason)

# Main function. Instantiates a new server and a new driver.
if __name__ == "__main__":

    CAserver = SimpleServer()
    CAserver.createPV("", _pvs)
    driver = NTPDriver()

    # Processes request each 100 ms
    while (True):
        CAserver.process(0.1)
