#!/usr/bin/python
# -*- coding: utf-8 -*-

# Modulo que realiza o dialogo entre um multimetro HP232 e um cliente PROSAC.
# Ele possui 3 threads, sendo elas: 
#     (i)  prosac_server :    escuta requisicoes realizadas via rede no protocolo especificado
#                             na página intracont
#     (ii)  scan         :    envia requisicoes de leitura ao multimetro periodicamente
#     (iii) process      :    atende as requisicoes realizadas pelas duas threads acima
# Necessita de um conversor USB - RS232.
#
# Baseado no programa SI-NCL, escrito por Eduardo PEREIRA COELHO.
# Autor: Gustavo CIOTTO PINTON


from pcaspy import Driver, Alarm, Severity, SimpleServer
import serial
from Queue import PriorityQueue
import threading
import logging
import socket
import traceback
import time

BOARD_ID = 15


PVs = {
    "Measure" : { "type" : "float" },
    "Unit"    : { "type" : "string"}
}

logging.basicConfig(filename = 'pyHP232.log', \
                    filemode = 'w', \
                    format = '%(asctime)s %(message)s', \
                    datefmt = '%d/%m/%Y %H:%M:%S', \
                    level = logging.INFO)

class PROSACommand:
    NORMAL, ADJUST, IDENT, END_IDENT, BOOT, MSG_CONFIRM = range(6)
    
    @staticmethod
    def toString (command):
        
        s = ""
        for i in range(len(command)) :
            s = s + "%02X " % ord(command[i])
        return s       
        

class PROSAC2HP232(Driver):

    def __init__(self):

        Driver.__init__(self)

        self.serial = serial.Serial('/dev/ttyUSB0', baudrate = 9600, bytesize = 8, parity = 'N', dsrdtr = True)
        
        # Limpa qualquer dado que esteja no buffer, evitando um 'bip' de erro. 
        self.serial.timeout = 1
        self.serial.readline()
        self.serial.flushInput()
        self.serial.flushOutput()
        self.serial.timeout = None
        
        self.event = threading.Event()
        
        # Resposta do multimetro eh constituida de 17 bytes, no formato abaixo
        self.HP232_readbytes = "+9.99999999E-99\r\n"

        # scan_delay regula o intervalo de tempo em que a thread scan realiza requisicoes 
        self.scan_delay = 1
        self.scan = threading.Thread(target = self.scanThread)
        self.process = threading.Thread(target = self.processThread)
        self.prosac_server = threading.Thread (target = self.prosacServer)
        
        self.scan.setDaemon(True)
        self.process.setDaemon(True)
        self.prosac_server.setDaemon(True)

        # Operacoes de AJUSTE sempre terao mais prioridade que operacoes
        # de leitura
        self.queue = PriorityQueue()
        self.queue.put((0, [PROSACommand.ADJUST, 10]))

        logging.info("class pyHP232 PROSAC2Serial inicializada.")

        self.process.start()
        self.scan.start()
        self.prosac_server.start()

    def prosacServer (self):
        
        logging.debug("Thread PROSAC2Serial inicializada.")
        
        while True :
            
            try:
            
                # Configuracao do socket
                server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
                server_socket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
                server_socket.bind(("", 4000))
                server_socket.listen(1)
                logging.info("Servidor TCP/IP na porta 4000 para compatibilidade com o sistema de controle do UVX iniciado.")
            
                while True:
                                        
                    logging.info("Esperando por uma conexão.")
                    
                    # Espera conexao de clientes
                    connection, client_address = server_socket.accept()
                    
                    logging.info("Cliente " + client_address[0] + ":" + str(client_address[1]) + " conectado.")
                    
                    while True:
                     
                        data = connection.recv(256)
                        
                        first_answer_byte = "\x00"
                        
                        if data:
                                                 
                            command = ord(data[0])
                            
                            if command == PROSACommand.NORMAL : 
                                
                                is_identifying = False
                            
                            elif command == PROSACommand.ADJUST : 
                                
                                is_identifying = False
                                
                                logging.info ("Recebeu um comando de AJUSTE: " + PROSACommand.toString(data))
                                
                                msg_length = ord(data[1])
                                
                                # O bloco de escrita das placas seriais eh constituido por 3 bytes:
                                #    (i) Prioridade, (ii) Flag (sempre 0x80 para esta placa) e (iii) Reset da escala 
                                
                                if (msg_length == len(data) - 2 and msg_length == 3):
                                    
                                    is_identifying = False
                                    
                                    self.queue.put((0, [PROSACommand.ADJUST, ord(data[4])]))
                            
                            elif command == PROSACommand.BOOT:
                                
                                logging.info ("Recebeu um comando de BOOT: " + PROSACommand.toString(data))
                                
                                is_identifying = False
                                # reset fundo de escala do multimetro
                                self.queue.put((0, [PROSACommand.ADJUST, 1]))
                            
                            elif command == PROSACommand.IDENT:
                                
                                logging.info ("Recebeu um comando de IDENTIFICACAO: " + PROSACommand.toString(data))
                                
                                is_identifying = True
                                first_answer_byte = chr(PROSACommand.IDENT)
                                
                            elif command == PROSACommand.MSG_CONFIRM or command == PROSACommand.END_IDENT: 
                            
                                first_answer_byte = "\x00"
                                is_identifying = False
                                
                            answer = ""                        
                            if is_identifying:
                                
                                # Resposta de identificacao consiste em 32 bytes contendo os ids das placas,
                                # seguidos pela versao do PROSAC
                                answer = list("\x02\x26" + (32 * "\x3F") + "\x50\x03\x32\x11\x06\x0E")
            
                                answer [BOARD_ID + 2] = "\x19" # id da placa Serial HP232 é 25 (0x19)
                                
                                answer = "".join(answer)
                            
                            else:
                                # bloco de leitura consiste de 17 bytes mais prioridade e flags
                                answer = first_answer_byte + chr(2 + 17) + "\x00\x80" + self.HP232_readbytes 
                            
                            connection.sendall(answer)
                           
                        else:
                            
                            logging.info("Cliente " + client_address[0] + ":" + str(client_address[1]) + " desconectado.")
                            break
            
            except Exception as exc:
                
                server_socket.close()
                
                logging.exception("Servidor capturou exceção:\n")
                traceback.print_exc()
        

    def scanThread(self):
        
        logging.info("scanThread inicializada.")
        
        while True:
            # Requisicoes de leitura periodicas
            self.queue.put((1, [PROSACommand.NORMAL]))
            self.event.wait(self.scan_delay)

    def processThread(self):
        
        logging.info("processThread inicializada.")
        
        # Comandos para multimetro na serial RS232 (PSICO v53)
        # STR232_RST            "*RST",CR,LF,0
        # STR232_CLS            "*CLS",CR,LF,0
        # STR232_SYSTREM        ":SYST:RWL",CR,LF,0
        # STR232_CONFV100       ":SYST:RWL; *CLS; :CONF:VOLT:DC 10, MIN", CR,LF,0
        # STR232_CONFV10        ":SYST:RWL; *CLS; :CONF:VOLT:DC 10,0.00001", CR,LF,0
        # STR232_READ           "*CLS;:SYST:RWL;*CLS;:READ?",CR,LF,0
        
        while True:
            
            item = self.queue.get(block = True)
            item = item[1]
            
            if item[0] == PROSACommand.NORMAL:
                
                try :
                
                    self.sendSerialCommand("*CLS;:SYST:RWL;*CLS;:READ?\x0D\x0A", 0.100)
                
                    self.HP232_readbytes = self.serial.readline()
                
                    # Atualiza variavies EPICS
                    self.setParam("Measure", float (self.HP232_readbytes[:-2]))
                
                    self.setParam("Unit", "V")
                    
                except Exception as exp:
                    
                    traceback.print_exc()            
            
            
            elif item[0] == PROSACommand.ADJUST or item[0] == PROSACommand.BOOT :
                
                self.sendSerialCommand ("SYST:RWL\x0D\x0A", 0.300)  
                
                prosac_cmd = item[0]
                              
                # Boot ou reset
                if prosac_cmd == PROSACommand.BOOT or item[1] == 1:
                
                    # Multimetro realiza leituras rapidamente
                    self.scan_delay = 0.5
                
                    self.sendSerialCommand ("*RST\x0D\x0A", 1)  
                    
                    self.sendSerialCommand ("SYST:RWL\x0D\x0A", 0.100)  
                
                elif item[1] == 10:
                    
                    # Multimetro realiza 10 ciclos de integracao para cada leitura.
                    # Logo, cada leitura toma um tempo maior em relacao ao caso passado
                    self.scan_delay = 1
                    
                    self.sendSerialCommand(":SYST:RWL; *CLS; :CONF:VOLT:DC 10,0.00001\x0D\x0A", 0.300)
                
                elif item[1] == 100:
                    
                    # 100 ciclos de integracao por leitura, equivalendo ao caso mais lento
                    self.scan_delay = 10
                    
                    self.sendSerialCommand(":SYST:RWL; *CLS; :CONF:VOLT:DC 10, MIN\x0D\x0A", 0.300)
                    
                                                
            self.updatePVs()
    
    # Envia um comando a interface serial e espera delay segundos.
    def sendSerialCommand (self, command, delay):
        
        self.serial.write(command)
        time.sleep(delay)

if __name__ == '__main__':

    CAserver = SimpleServer()
    CAserver.createPV("Cnt:Measure:", PVs)
    driver = PROSAC2HP232()
    
    while (True):
        CAserver.process(0.1)