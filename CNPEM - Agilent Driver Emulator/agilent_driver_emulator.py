import serial
import threading
import math
import numpy as np
from array import array
import random

'''
  Agilent_Driver class virtualizes the behavior of a real Agilent vacuum pump. We use some fictitious data in order
  to test the setup of the EPICS Stream Device software.

  Another version was developed, using Qt framework. Refer to Agilent Qt Driver for further information.

  Gustavo CIOTTO PINTON
  CNPEM - LNLS 
'''

class Agilent_Driver():
    
    # Initializes a new driver. It uses a serial object to send data through a virtual serial port. 
    def __init__(self):
        
        self.serial_connection = serial.Serial("/dev/pts/6", 9600)
        
        self.checksum_length = 1
        
	# Builds fictitious data

        T = 100
        self.fan_temperature = [65. +  25. * math.cos(2* math.pi * i / T) for i in range (100)]
        self.fan_indice = 0
        
        T = 500
        T2 = 200
        self.hv1_temperature = [89. +  50. * math.cos(2* math.pi * i/T) * math.cos(2* math.pi * i/T2) for i in range (1000)]
        self.hv1_indice = 0
        
        T = 8000
        T2 = 10
        self.hv2_temperature = [89. +  50. * math.cos(2* math.pi * i/T) * math.sin(2* math.pi * i/T2) for i in range (8000)]
        self.hv2_indice = 0
        
    
    # Get next fan temperature
    def getFanTemperature(self):
        
        temp =  self.fan_temperature[self.fan_indice]
        
        self.fan_indice = self.fan_indice % len(self.fan_temperature)
        
        return temp
        
    # Verifies which command was received and prepares an answer
    def processCommand(self, recv_bytes):
        
        cmd = "%c%c%c" % (recv_bytes[2], recv_bytes[3], recv_bytes[4])
        
        s_array = []        
        
        if cmd == "800" or cmd == "801"  or cmd == "802" :
            
            if cmd == "800":
                
                temp = self.fan_temperature[self.fan_indice]
                print 'Reading FAN Temperature... %4.3foC' % temp
                self.fan_indice = (self.fan_indice + 1) % 100
            
            elif cmd == "801":
                
                temp = self.hv1_temperature[self.hv1_indice]
                print 'Reading HV1 Temperature... %4.3foC' % temp
                self.hv1_indice = (self.hv1_indice + 1) % 1000
            
            elif cmd == "802":  
                
                temp = self.hv2_temperature[self.hv2_indice]
                print 'Reading HV2 Temperature... %4.3foC' % temp
                self.hv2_indice = (self.hv2_indice + 1) % 8000
                
            temp_array = array('B', "%3.1f" % temp)
                       
            for x in temp_array: s_array.append(x)
            
        elif cmd == '108':
            
            print 'Reading Baud Rate....'
            baud = 9600
            baud_array = array('B', "%06d" % baud)
            
            for x in baud_array: s_array.append(x)
        
        elif cmd == '810':
            
            vch1 = random.randint(0, 7000)
            print 'Reading Tension in CH1.... %05d' % vch1
            
            vch1_array = array('B', "%05d" % vch1)
            
            for x in vch1_array: s_array.append(x)
        
        elif cmd == '820':
            
            vch = np.int(2*math.log(random.randint(1, 7000)))
            print 'Reading Tension in CH2.... %05d' % vch
            
            vch_array = array('B', "%05d" % vch)
            
            for x in vch_array: s_array.append(x)
        
        elif cmd == '811':
            
            i = math.cosh(random.randint(80, 100))
            print 'Reading Current in CH1.... %05d' % i
            
            vch1_array = array('B', "%E" % i)
            
            for x in vch1_array: s_array.append(x)
        
        return s_array
  
    @staticmethod
    def setCheckSum(command_byte_array, check_sum_method = 'sum', check_sum_length = 1):
        
        if check_sum_method == 'sum':
                
            checksum = sum(command_byte_array)  % (2 ** (8 * check_sum_length))
            
            command_byte_array.append(np.uint8(checksum))
  
    @staticmethod
    def hasValidCheckSum(command_byte_array, check_sum_method = 'sum', check_sum_length = 1):
                
        checksum = 0
        for i in range(check_sum_length):
            checksum += command_byte_array[-(i+1)] << (8 * i)
            
        crc = 0
        
        if check_sum_method == 'crc16': 
            
            poly = 0x07
            
            i = 0
            
            print [hex(x) for x in command_byte_array[1:-2]], '!=', [hex(x) for x in command_byte_array[-3:0:-1]]
            
            for x in command_byte_array[-3:1:-1]:
            #for x in command_byte_array[1:-2]:
                crc += (x * (poly ** i))
                i += 1 
            
        
        elif check_sum_method == 'sum':
            
            crc = sum(command_byte_array[:-1]) % (2 ** 8)
        
        #print 'CRC = %d != CHECKSUM = %d' % (crc, checksum)
        
        if checksum == crc:
            return 1
        
        return 0    
    
    def processRequest(self, data): pass
      
    # Start driver's operation. Waits for bytes and waits answers according to the received commands
    def start(self):
               
        print 'Listening Streaming Device...'
        while 1:

            recv_message = []

	    # Waits the beginning-of-message byte
            recv_byte = ord(self.serial_connection.read()[0])
            while recv_byte != 0x02: 
                recv_byte = ord(self.serial_connection.read()[0])
            
            #while ord((recv_data = self.serial_connection.read(8))[0]) != 0x02: pass
            
            recv_message.append(np.uint8(0x02))
            
	    # Gets bytes until it receives a 0x03 byte
            recv_byte = ord(self.serial_connection.read()[0])
            while recv_byte != 0x03:
                
                #print 'Received ', hex(recv_byte), '...'
                recv_message.append(np.uint8(recv_byte))
                recv_byte = ord(self.serial_connection.read()[0])
                
            recv_message.append(np.uint8(0x03))
            
            checksum = self.serial_connection.read(self.checksum_length)
            #print 'Received checksum', [hex(ord (x)) for x in checksum], '...'
            
            for x in checksum: recv_message.append(np.uint8(ord(x)))
                      
            print 'Received data: ', [hex(c) for c in recv_message]
            
            if Agilent_Driver.hasValidCheckSum(recv_message, check_sum_length = self.checksum_length):
             
                send_array = self.processCommand(recv_message)
                
                Agilent_Driver.setCheckSum(send_array)
                
                self.serial_connection.write(send_array)
            
            #print 'Sent data: ', send_array
                
        self.serial_connection.close()
        print 'Exiting thread...'

    
# Builds and starts driver.
if __name__ == "__main__": 
    
    driver = Agilent_Driver()
    driver.start()
    
    
