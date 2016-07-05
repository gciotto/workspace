from __future__ import division # Floating point division 
import socket
import sys
import threading

'''
    Requester class is responsible for communicating with PROSAC and requesting updates
    in the SourceWindows objects.

    Created on Jun 21, 2016

    @author: gciotto
'''

class Requester:
   
    # Instantiates a new object and update flags. The flag can_ask allows reader thread to
    # start requesting data from this object.
    def __init__ (self, window_controller):

        self.m_request = threading.Lock()
        self.isConnected = False
        self.can_ask = False
        self.window_controller = window_controller

    # Tries to connect to PROSAC
    def connect(self, ip_address = "10.128.47.2", port = 4000):

        if not self.isConnected: 
        
            try:
                
                # instantiates socket
                self.sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
                self.sock.settimeout(5.0)
                self.sock.connect((ip_address, port))
            
            # if connection is not successful
            except Exception as exc:
                
                self.isConnected = False
                err = "Connection failed with error '%s'" % str(exc) 
            
            else:
            
                self.isConnected = True

                err = "Client connected."
        
        return self.isConnected, err
    
    # Updates flags and closes socket
    def disconnect(self):
        
        self.can_ask = False
        self.isConnected = False        
        self.sock.close()
    
    # Send all bytes contained in buffer
    def send_packet(self, buffer): 
            
        result = False
        err_c = ""    
        
        if self.isConnected:
            
            try:
                self.sock.sendall(buffer)
            
            # Verifies if all bytes were sent
            except Exception as exc: 
                
                # Else, disconnect object
                err_c = exc.strerror
                self.disconnect()
                result = False
            
            else: 
                
                err_c = "%d bytes sent successfully" % len(buffer)
                result = True
                
        else: err_c = "Client is not connected."
            
        return result, err_c
            
    # Receives bytes from PROSAC
    def receive_packet(self):
        
        err_c = ""
        
        if self.isConnected:
            
            try:
                self.sock.settimeout(5)
                buf =  bytearray(self.sock.recv(2)) # Receives two bytes related to command code and size 
                
                if len(buf) > 1:
                
                    # Reads remaining bytes
                    for b in bytearray(self.sock.recv(buf[1])):
                        
                        buf.append(b)
                        
            except Exception as exc: 
                # Disconnects if failure
                err_c = exc.strerror
                self.disconnect()
                buf = []
            
        return buf, err_c
    
    # Prepares packets to be sent, depending of the command code 
    def prepare_command (self, command = 0x0):
        
        buf = []
        
        buf.append (command) 
        
        if command == 0x1 or command == 0xE0 or command == 0xE1: 
            
            p_lenght = 5 * self.window_controller.get_boards_count()
            
            buf.append(p_lenght & 0xFF)
            
            for board in self.window_controller.get_boards_list(): 
                
                buf.append(0x0) # Priority byte, useless
                
                # If cycle, send 39, corresponding to cycle curve #7
                if board.will_cycle(): buf.append(39)
                else: buf.append (0x80)
                
                current_out = board.get_analog_adjust()
                
                # Coverts floating point number to 2-byte integer
                a_value = int (4095/20 * (current_out + 10))
                
                buf.append((a_value & 0xFF00) >> 8)
                buf.append(a_value & 0xFF)
             
                # Send digital bit 
                if board.is_on(): buf.append(0x1)
                else: buf.append(0x0)
        
        return bytearray(buf)
    
    # Prepare, send, receive and process a command
    def execute_command(self, command = 0x0): 
        
        # acquire mutex
        self.m_request.acquire()
        
        # prepare packet to be sent
        buf = self.prepare_command(command)
        sent, err_m = self.send_packet(buf)
        
        if sent:
            
            # Ignore command 0x00
            if command: 
                self.window_controller.label_sent.setText('Last packet sent: %s' % ''.join(format(x, '02x') + ' ' for x in buf))
        
        else:
            
            self.window_controller.statusBar().showMessage("Packet transmission failed with error '%s'" % err_m)
            self.window_controller.set_disconnected_state()
                       
            self.window_controller.clear_source_group()
            
            self.m_request.release()
            
            return
            
        # Receive answer
        buf, err_c  = self.receive_packet()
        
        # if err_c is an empty string, then receive_packet() was successful 
        if err_c is not "":
                        
            self.window_controller.statusBar().showMessage("Packet transmission failed with error '%s'" % err_m)
            
            self.window_controller.set_disconnected_state()
                       
            self.window_controller.clear_source_group()
            
            self.m_request.release()
            
            return
        
        # Ignore 0x00 answers
        if buf[0]:
            
            self.window_controller.label_received.setText("Last packet received: %s " %  ''.join(format(x, '02x') + ' ' for x in buf))
        
        command = buf[0]
        
        # commands supported by the client
        if command == 0x0 or command == 0xE0 or command == 0xE3:
            
            j = 2
            
            # updates all fields of each board
            for b in self.window_controller.get_boards_list() :
                
                a_in = (buf[j + 2] << 8) + buf[j + 3]
                d_in = buf[j + 4]
                
                # converts current to floating point
                current_in = 20 * a_in / 4095 - 10
                
                # Checks the range
                if current_in <= 10 and current_in >= -10:
                
                    b.set_analog_input(current_in)
                    
                    b.set_digital_inputs(d_in) 
                                        
                j = j + 5          
        
            # Updates buttons state in the case of receiving a CYCLE COMPLETED (0xE3) byte. 
            if command == 0xE3:
                
                self.window_controller.button_normal.setEnabled(True)
                self.window_controller.button_adjust.setEnabled(True)
                self.window_controller.button_cycle_enable.setEnabled(True)
                self.window_controller.button_cycle_abort.setEnabled(False)
                self.window_controller.button_confirm.setEnabled(True)
                
        
        # command 0x02 corresponds to the IDENT answer. In this case, new boards must be
        # instantiated 
        if command == 0x02:
                       
            for j in range(2, 34) :    
                if buf [j] is not 0x3F:
                    self.window_controller.add_board_source_group(j - 2)

        self.m_request.release()
        
    def isFinished(self):
            
        return self.window_controller.is_closed
        
    def canStart(self):

        return self.window_controller is not None
        